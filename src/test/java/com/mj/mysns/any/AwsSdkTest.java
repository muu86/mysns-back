package com.mj.mysns.any;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@SpringBootTest
public class AwsSdkTest {

    @Autowired
    S3Presigner presigner;

    @Autowired
    S3Client s3Client;

    @Test
    void getFiles() {
        String key = "780be1f2-a2ce-49a5-96a1-40a71d7351d1";

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(
            builder -> builder
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(request -> request
                    .bucket("my-sns")
                    .key("134")
                    .build())
                .build());

        String url = presignedGetObjectRequest.url().toString();
        System.out.println(url);
    }
}
