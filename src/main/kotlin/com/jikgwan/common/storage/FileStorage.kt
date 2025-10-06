package com.jikgwan.common.storage

import org.springframework.web.multipart.MultipartFile

interface FileStorage {
    fun upload(file: MultipartFile, directory: String): String
    fun delete(fileUrl: String)
}
