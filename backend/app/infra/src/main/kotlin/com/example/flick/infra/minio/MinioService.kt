package com.example.flick.infra.minio

import com.example.flick.domain.common.FileStorageService
import com.example.flick.infra.config.MinioProperties
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class MinioService(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties
) : FileStorageService {

    override fun uploadFile(file: MultipartFile): String {
        val filename = UUID.randomUUID().toString() + "-" + file.originalFilename
        val objectName = "flicks/" + filename

        // Ensure the bucket exists
        // This part should ideally be handled during application startup or by an admin task
        // For simplicity, we'll assume the bucket exists or is created elsewhere.
        // minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.bucketName).build())

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(minioProperties.bucketName)
                .`object`(objectName)
                .stream(file.inputStream, file.size, -1)
                .contentType(file.contentType)
                .build()
        )
        return "${minioProperties.endpoint}/${minioProperties.bucketName}/$objectName"
    }
}
