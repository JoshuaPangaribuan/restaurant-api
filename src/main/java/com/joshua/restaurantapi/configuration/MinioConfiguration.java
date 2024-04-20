package com.joshua.restaurantapi.configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfiguration {
    private String accessKey;
    private String secretKey;
    private String qrBucket;
    private String endpoint;

    @Bean
    public MinioClient minioClient(){
        MinioClient client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        try {
            boolean bucketFound = client.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(qrBucket)
                            .build());

            if (!bucketFound) {
                client.makeBucket(MakeBucketArgs
                                .builder()
                                .bucket(qrBucket)
                                .build());
            }
        }catch (Exception e) {
            System.out.println("Error while instatiate minio: " + e.getMessage());
        }

        return client;
    }
}
