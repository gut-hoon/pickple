package com.fil.pickple.application;

import com.fil.pickple.application.command.GeneratePresignedUrlCommand;
import com.fil.pickple.application.command.UploadFileCommand;
import com.fil.pickple.application.result.GeneratePresignedUrlResult;
import com.fil.pickple.application.result.UploadFileResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.time.Duration;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String BUCKET;

    protected S3Service(S3Client s3Client, S3Presigner s3Presigner, @Value("${aws.s3.bucket}") String bucket) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.BUCKET = bucket;
    }

    public UploadFileResult uploadFile(UploadFileCommand command) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(command.key())
                .contentType(command.file().getContentType())
                .build();
        
        s3Client.putObject(request, RequestBody.fromInputStream(
                command.file().getInputStream(), 
                command.file().getSize()
        ));
        
        return UploadFileResult.of(command.key());
    }

    public GeneratePresignedUrlResult generatePresignedUrl(GeneratePresignedUrlCommand command) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET)
                .key(command.key())
                .build();
        
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(command.ttlMinutes()))
                .getObjectRequest(getObjectRequest)
                .build();
        
        String presignedUrl = s3Presigner.presignGetObject(presignRequest).url().toString();
        
        return GeneratePresignedUrlResult.of(presignedUrl);
    }
}