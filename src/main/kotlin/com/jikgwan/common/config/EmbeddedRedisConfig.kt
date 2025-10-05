package com.jikgwan.common.config

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * 로컬 및 개발 환경에서 사용할 내장 Redis 서버 설정
 * 운영 환경(prod)에서는 외부 Redis를 사용
 *
 * 현재는 Redis를 optional로 설정하여, 외부 Redis가 없어도 앱이 실행되도록 함
 * 실제로 Embedded Redis를 사용하려면 별도로 Redis를 설치하거나 Docker를 사용하세요
 */
@Configuration
@Profile("local", "dev", "test")
@ConditionalOnProperty(name = ["redis.embedded.enabled"], havingValue = "true", matchIfMissing = false)
class EmbeddedRedisConfig {

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.warn("⚠️ Embedded Redis is not configured. Please install Redis locally or use Docker.")
        logger.info("💡 To run Redis: docker run -d -p 6379:6379 redis:alpine")
    }
}
