package com.jikgwan.common.config

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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

    @Bean
    fun cacheManager(
        connectionFactory: RedisConnectionFactory
    ): CacheManager {
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
