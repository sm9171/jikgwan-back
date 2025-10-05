package com.jikgwan

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication(
    exclude = [
        io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration::class,
        org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration::class,
        org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration::class
    ]
)
@EnableCaching
class JikgwanApplication

fun main(args: Array<String>) {
    runApplication<JikgwanApplication>(*args)
}
