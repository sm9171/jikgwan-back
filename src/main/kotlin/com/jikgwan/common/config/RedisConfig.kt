package com.jikgwan.common.config

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Redis가 사용 가능할 때 사용되는 CacheManager
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = ["spring.data.redis.host"])
    fun redisCacheManager(
        connectionFactory: RedisConnectionFactory
    ): CacheManager {
        logger.info("✅ Using Redis CacheManager")

        val cacheConfigurations = mapOf(
            "gatherings" to cacheConfiguration(Duration.ofMinutes(10)),
            "users" to cacheConfiguration(Duration.ofHours(1)),
            "profiles" to cacheConfiguration(Duration.ofHours(1))
        )

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(cacheConfiguration(Duration.ofMinutes(30)))
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }

    /**
     * Redis가 없을 때 사용되는 In-Memory CacheManager (Fallback)
     */
    @Bean
    @ConditionalOnProperty(name = ["spring.data.redis.host"], matchIfMissing = true, havingValue = "none")
    fun inMemoryCacheManager(): CacheManager {
        logger.warn("⚠️ Using In-Memory CacheManager (Redis not available)")
        return ConcurrentMapCacheManager("gatherings", "users", "profiles")
    }

    private fun cacheConfiguration(ttl: Duration): RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(ttl)
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair
                    .fromSerializer(StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair
                    .fromSerializer(GenericJackson2JsonRedisSerializer())
            )
    }
}
