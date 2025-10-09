package com.jikgwan.adapter.`in`.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/files")
class FileController(
    @Value("\${storage.local.base-path:uploads}") private val basePath: String
) {

    private val absoluteBasePath: String

    init {
        val projectRoot = System.getProperty("user.dir")
        absoluteBasePath = File(projectRoot, basePath).absolutePath
    }

    @GetMapping("/{directory}/{filename:.+}")
    fun serveFile(
        @PathVariable directory: String,
        @PathVariable filename: String
    ): ResponseEntity<Resource> {
        val filePath = Paths.get(absoluteBasePath, directory, filename)
        val file = filePath.toFile()

        if (!file.exists() || !file.isFile) {
            return ResponseEntity.notFound().build()
        }

        val resource = FileSystemResource(file)
        val contentType = determineContentType(filename)

        return ResponseEntity.ok()
            .contentType(contentType)
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"$filename\"")
            .body(resource)
    }

    private fun determineContentType(filename: String): MediaType {
        val extension = filename.substringAfterLast(".", "").lowercase()
        return when (extension) {
            "png" -> MediaType.IMAGE_PNG
            "jpg", "jpeg" -> MediaType.IMAGE_JPEG
            "gif" -> MediaType.IMAGE_GIF
            "svg" -> MediaType.parseMediaType("image/svg+xml")
            "pdf" -> MediaType.APPLICATION_PDF
            else -> MediaType.APPLICATION_OCTET_STREAM
        }
    }
}
