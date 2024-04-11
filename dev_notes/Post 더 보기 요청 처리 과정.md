# Post 더 보기 요청 처리 과정

## 무한스크롤 시 서버 컴포넌트를 사용할 수 없음

<img src="images/스크린샷 2024-04-01 오전 7.53.11.png" width="300" />

Post 리스트 컴포넌트를 서버 컴포넌트로 구성했다. 첫 요청에 10개의 포스트를 가져와서 서버 컴포넌트로 렌더링 하는 것까진 문제가 없어보였다. 그런데 사용자가 스크롤을 끝까지 내리면 다음 10개의 포스트를 요청하게 되는데 기존에 렌더링된 10개의 포스트와 새로 요청한 추가 10개의 포스트를 어떻게 합쳐야되는지 고민이 생겼다.

서버 컴포넌트는 사용자와 상호작용하는 게 불가능하다. 오직 새로운 요청이 있어야 페이지를 렌더할 수 있다. 포스트 페이지를 서버 컴포넌트를 구현하는 방법은 다음과 같을 것이다.

- 1p, 2p, 3p.. 식으로 페이지를 나눠서 사용자가 페이지를 클릭하면 다음 페이지를 렌더한다.
- 처음 포스트를 서버 컴포넌트로 렌더하고 추가되는 포스트는 클라이언트 컴포넌트를 붙인다. 같은 Post 컴포넌트인데 서버 컴포넌트, 클라이언트 컴포넌트 두 가지 버전이 필요하다.

### 서버 컴포넌트의 장점

서버 컴포넌트의 장점은 nextjs 매뉴얼에 나온대로

- 검색엔진 최적화
  - 이것때문에 nextjs 를 사용하려고 애쓰고 있다.
  - 클라이언트 컴포넌트는 자바스크립트가 코드를 실행한 뒤에 비로소 페이지가 보이기 때문에 검색엔진 최적화에 불리하다.
- 캐쉬
  - 정적 route 는 모두 빌드타임에 캐쉬되기 때문에 다시 렌더할 필요가 없다.
  - /[...]/page.tsx 같은 다이내믹 route 는 요청이 들어오면 렌더하고 캐쉬한다.
- 성능
  - 클라이언트가 자바스크립트를 다운받고 이를 실행시켜서 렌더링하는 과정을 최소화할 수 있다.

### 페이지를 나눠서 스크롤을 구현해보려고 해봄

Post Page 에 request parameter 를 설정했다. Props로 parameter를 확인하고 페이지만큼의 오프셋을 적용한 포스트 리스트를 받아온다. 이 때 현재 오프셋 전의 데이터도 `fetch`로 모두 가져온다.

만약, 사용자가 3페이지를 요청했다면, Post 서버 컴포넌트는 서버에 1,2,3 페이지 모두 요청을 날린다. 그런데 1,2 페이지는 nextjs 에 캐쉬 데이터가 있으므로 실제 요청은 3페이지 요청만 나간다.

### Nextjs Fetch 캐쉬

https://nextjs.org/docs/app/api-reference/functions/fetch

Nextjs 는 Fetch Api 를 확장해서 자동으로 fetch 로 가져온 데이터를 캐쉬한다. 중첩된 컴포넌트에서 반복적으로 같은 api 요청을 해도 걱정할 필요가 없다. 같은 url 이라면 한 번만 요청이 나간다.

```ts
// server action
export async function getPost({...}) {
  const param: GetPostPayload = {
    username,
    latitude,
    longitude,
    offset,
  };

  const response = await fetch(
    `${process.env.SERVER_BASE_URL}/post?${stringify(param)}`,
    {
      method: 'GET',
      cache: 'force-cache',
    }
  );
  //...
}
```

`fetch` 를 날릴 때 `cache` 옵션을 설정할 수 있다.
nextjs 매뉴얼에선 아무 옵션도 지정하지 않으면 기본적으로 cache한다고 나와있는데 자동으로 적용이 안 돼서 직접 cache 옵션을 `force-cache`로 설정했다. 내 추측으로는 아마 개발 환경에서는 자동으로 캐쉬하지 않는 것 같다.

### 스크롤 위치를 해결하지 못함

여기까진 좋았다. 해결했다는 생각에 흐뭇했는데 막상 구현을 하고 보니 사용자의 스크롤 위치를 정확하게 계산할 수가 없었다.

무한 스크롤을 구현하려면, 다음 페이지를 요청할 때 현재 현재 스크롤 위치도 파라미터로 넘겨주고, 다음 페이지가 렌더될 때 사용자를 그 스크롤 위치로 보내줘야 한다. 이 때 스크롤 위치가 약간씩 튀는 현상이 발생할 수 밖에 없었다. 사용자는 계속 휠을 돌리고 있기 때문에 새로고침이 일어날 때 정확한 위치로 보내주기는 힘들어 보인다.

## 결국 클라이언트 컴포넌트로 구현

```tsx
// Post 서버 컴포넌트
import Post from '@/components/post/post';
import { getPost } from '@/lib/actions/post';
import { auth } from 'auth';

export default async function Home() {
  const session = await auth();
  const param = {
    username: session?.user?.username || '',
    offset: 0,
  };
  const posts = await getPost(param);
  return (
    <main className="w-full h-full grow">
      <Post firstPosts={posts} />
    </main>
  );
}
```

`/post/page.tsx` 는 `Post` 컴포넌트를 가져오는 껍데기 역할만 하고 `Post` 클라이언트 컴포넌트가 거의 모든 것을 다하는 형태가 되었다. 처음 요청은 서버 컴포넌트에서 데이터를 가져와서 클라이언트 컴포넌트로 Props 로 넘겨주긴 하는데 크게 의미는 없다. 클라이언트 컴포넌트에서 server action 을 사용해도 거의 같은 효과일 것이다.

```tsx
// 클라이언트 컴포넌트
export default function Post({ firstPosts }: { firstPosts: PostType[] }) {
  const [posts, setPosts] = useState<PostType[]>([]);
  const [offset, setOffset] = useState<number>(0);

  useEffect(() => {
    setPosts([...firstPosts]);
  }, [firstPosts]);

  async function getNextPost() {
    const nextPosts = await getPost({ offset });
    setPosts([...posts, ...nextPosts]);
    setOffset(offset + 10);
  }

  return (
    <>
      {posts.length === 0 && <PostNone />}
      {posts.length > 0 &&
        posts.map((p, i) => (
          <PostContextProvider key={i} post={p}>
            <PostOne key={i} post={p} />
          </PostContextProvider>
        ))}
      <div className="h-12"></div>
      <ScrollTrigger getNextPost={getNextPost} />
    </>
  );
}
```

가장 하단에 `<ScrollTrigger>` 를 두어서 사용자가 화면을 끝까지 내리면 `getNextPost`가 발동하도록 한다.

```tsx
// ScrollTrigger
import { useCallback } from 'react';

export function ScrollTrigger({ getNextPost }: { getNextPost: () => void }) {
  const triggerRef = useCallback(
    (node: HTMLDivElement) => {
      if (!node) return;

      const observer = new IntersectionObserver((entries, observer) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            getNextPost();
          }
        });
      });

      observer.observe(node);

      return () => {
        observer.disconnect();
      };
    },
    [getNextPost]
  );

  return (
    <div ref={triggerRef} className="w-full h-32 flex justify-center items-center">
      <div className="animate-spin w-6 h-6">
        <svg width="24" height="24" viewBox="0 0 24 24">
          <g>
            <circle cx="12" cy="2.5" r="1.5" opacity=".14" />
            <circle cx="16.75" cy="3.77" r="1.5" opacity=".29" />
            <circle cx="20.23" cy="7.25" r="1.5" opacity=".43" />
            <circle cx="21.50" cy="12.00" r="1.5" opacity=".57" />
            <circle cx="20.23" cy="16.75" r="1.5" opacity=".71" />
            <circle cx="16.75" cy="20.23" r="1.5" opacity=".86" />
            <circle cx="12" cy="21.5" r="1.5" />
          </g>
        </svg>
      </div>
    </div>
  );
}
```

`IntersectionObserver` Api 를 사용해서 이 컴포넌트가 사용자에게 보이면(`isIntersecting`) getNextPost 함수가 발동하도록 한다.

## 결론

Post 페이지는 내 서비스에서 메인이라고 할 수 있기 때문에 검색엔진 최적화도 고려해서 Server Component 로 구성해보고 싶었는데 스크롤이 튀는 문제 때문에 클라이언트 컴포넌트로 구현하게 됐다. 클라이언트 컴포넌트를 사용한다고 해서 문제가 있는 것은 아니지만 Server Side Rendering 때문에 Nextjs 를 사용하는 건데 Nextjs를 사용하는 의미가 크게 없는 것 같아서 아쉽다.
