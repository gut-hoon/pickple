package com.fil.pickple.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    private final String ACCESS_KEY;
    private final String SECRET_KEY;
    private final String REGION;

    protected S3Config(
            @Value("${aws.s3.access-key}") String accessKey,
            @Value("${aws.s3.secret-key}") String secretKey,
            @Value("${aws.s3.region}") String region
    ){
        this.ACCESS_KEY=accessKey;
        this.SECRET_KEY=secretKey;
        this.REGION=region;
    }

    @Bean
    public S3Client s3() {
        var creds = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        return S3Client.builder()
                .region(Region.of(REGION))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
    }

    @Bean
    public S3Presigner presigner() {
        var creds = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        return S3Presigner.builder()
                .region(Region.of(REGION))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
    }
}
