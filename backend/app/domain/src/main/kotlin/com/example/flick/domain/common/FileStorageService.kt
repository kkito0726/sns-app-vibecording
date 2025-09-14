package com.example.flick.domain.common

import org.springframework.web.multipart.MultipartFile

interface FileStorageService {
    fun uploadFile(file: MultipartFile): String
}
