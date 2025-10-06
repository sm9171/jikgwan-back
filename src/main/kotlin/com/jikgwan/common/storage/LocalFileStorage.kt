package com.jikgwan.common.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Component
class LocalFileStorage(
    @Value("\${storage.local.base-path:uploads}") private val basePath: String,
    @Value("\${storage.local.base-url:http://localhost:8080/files}") private val baseUrl: String
) : FileStorage {

    private val absoluteBasePath: String

    init {
        // 프로젝트 루트 경로 기준으로 절대 경로 생성
        val projectRoot = System.getProperty("user.dir")
        absoluteBasePath = File(projectRoot, basePath).absolutePath

        // uploads 디렉토리 생성
        Files.createDirectories(Paths.get(absoluteBasePath))

        println("📁 파일 저장 경로: $absoluteBasePath")
    }

    override fun upload(file: MultipartFile, directory: String): String {
        val fileName = generateFileName(file.originalFilename ?: "file")
        val targetPath = Paths.get(absoluteBasePath, directory, fileName)

        Files.createDirectories(targetPath.parent)
        file.transferTo(targetPath.toFile())

        println("✅ 파일 업로드 완료: ${targetPath.toAbsolutePath()}")

        return "$baseUrl/$directory/$fileName"
    }

    override fun delete(fileUrl: String) {
        val relativePath = fileUrl.removePrefix("$baseUrl/")
        val path = Paths.get(absoluteBasePath, relativePath)
        Files.deleteIfExists(path)

        println("🗑️ 파일 삭제 완료: ${path.toAbsolutePath()}")
    }

    private fun generateFileName(originalFileName: String): String {
        val extension = originalFileName.substringAfterLast(".", "")
        val uuid = UUID.randomUUID().toString()
        return if (extension.isNotEmpty()) "$uuid.$extension" else uuid
    }
}
