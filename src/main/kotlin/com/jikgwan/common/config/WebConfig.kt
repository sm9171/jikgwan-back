package com.jikgwan.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File

@Configuration
class WebConfig(
    @Value("\${storage.local.base-path:uploads}") private val basePath: String
) : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // 프로젝트 루트 경로 기준으로 절대 경로 생성
        val projectRoot = System.getProperty("user.dir")
        val absoluteBasePath = File(projectRoot, basePath).absolutePath

        registry.addResourceHandler("/files/**")
            .addResourceLocations("file:$absoluteBasePath/")

        println("🌐 정적 파일 서빙 경로: file:$absoluteBasePath/")
    }
}
