package com.mj.mysns.batch.mockdata;

import com.mj.mysns.location.entity.Address;
import com.mj.mysns.location.repository.AddressRepository;
import com.mj.mysns.post.entity.Post;
import com.mj.mysns.user.entity.User;
import com.mj.mysns.user.repository.UserRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class MockPostProcessor implements ItemProcessor<String, Post> {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    // mock data 에는 content 만 있기 때문에
    // user 와 location 값을 랜덤으로 세팅해서 넣는다.
    @Override
    public Post process(String content) throws Exception {
        Random random = new Random();

        long userCount = userRepository.count();
        long userOffset = random.nextLong(0, userCount);
        User user = userRepository.findAll(PageRequest.of((int) userOffset, 1))
            .getContent().getFirst();

        long addressCount = addressRepository.count();
        long addressOffset = random.nextLong(0, addressCount);
        Address address = addressRepository.findAll(PageRequest.of((int) addressOffset, 1))
            .getContent().getFirst();

        Post post = Post.builder().content(content).user(user).build();
        post.setAddress(address);

        return post;
    }
}
