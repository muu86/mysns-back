package com.mj.mysns.common.file;

import com.mj.mysns.common.file.FileLocation.FileLocationType;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3FileRepository implements FileRepository {

    // docker 에서 구동할 시 Environment 에서 가져오도록 해야함
    private static final String BUCKET_NAME = "my-sns";

    private final S3Client s3;

    private final S3Presigner presigner;

    @Override
    public Optional<FileLocation> saveFile(MultipartFile file) {

//        String originalFilename = file.getOriginalFilename();
        String objectKey = UUID.randomUUID().toString();

        try {
            PutObjectResponse putObjectResponse = s3.putObject(request -> request
                    .bucket(BUCKET_NAME)
                    .key(objectKey)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
//                    .metadata(Map.of("Original-Filename", originalFilename))
                ,
                RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));

            if (putObjectResponse == null || !putObjectResponse.sdkHttpResponse().isSuccessful()) {
                return Optional.empty();
            }

        } catch (IOException e) {
            return Optional.empty();
        }

        return Optional.of(new FileLocation(objectKey, FileLocationType.S3));

    }

    @Override
    public FileUrl getFileUrl(FileLocation location) {
        String[] suffix = { "raw/%s", "lg/%s", "md/%s", "sm/%s" };

        FileUrl fileUrl = FileUrl.builder()
            .raw(getPresignedGetObjectRequest(suffix[0].formatted(location.getKey())).url()
                .toString())
            .lg(getPresignedGetObjectRequest(suffix[1].formatted(location.getKey())).url()
                .toString())
            .md(getPresignedGetObjectRequest(suffix[2].formatted(location.getKey())).url()
                .toString())
            .sm(getPresignedGetObjectRequest(suffix[3].formatted(location.getKey())).url()
                .toString())
            .build();

        return fileUrl;
    }

    private PresignedGetObjectRequest getPresignedGetObjectRequest(String key) {
        return presigner.presignGetObject(
            builder -> builder
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(request -> request
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build())
                .build());
    }
//        ResponseInputStream<GetObjectResponse> resp = s3.getObject(
//            req -> req.bucket("cdk-hnb659fds-assets-347192894377-ap-northeast-2").key("053b2d9738fcf2e57ad20d1b216f849af041607c25cfab3a39acace3d8a5117a.json"));
//
//        String content = StreamUtils.copyToString(resp, StandardCharsets.UTF_8);

//        log.info("{}", content);

//        FilenameUtils.getPathNoEndSeparator()

}
