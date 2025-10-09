package com.jikgwan.adapter.out.cache

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

/**
 * 토큰 블랙리스트 관리 인터페이스
 */
interface TokenBlacklistService {
    fun addToBlacklist(token: String, expirationSeconds: Long)
    fun isBlacklisted(token: String): Boolean
    fun removeFromBlacklist(token: String)
}

/**
 * Redis 기반 토큰 블랙리스트 서비스
 */
@Service
@ConditionalOnBean(RedisTemplate::class)
class RedisTokenBlacklistService(
    private val redisTemplate: RedisTemplate<String, String>
) : TokenBlacklistService {

    companion object {
        private const val BLACKLIST_PREFIX = "blacklist:token:"
    }

    override fun addToBlacklist(token: String, expirationSeconds: Long) {
        val key = BLACKLIST_PREFIX + token
        redisTemplate.opsForValue().set(
            key,
            "logout",
            Duration.ofSeconds(expirationSeconds)
        )
    }

    override fun isBlacklisted(token: String): Boolean {
        val key = BLACKLIST_PREFIX + token
        return redisTemplate.hasKey(key)
    }

    override fun removeFromBlacklist(token: String) {
        val key = BLACKLIST_PREFIX + token
        redisTemplate.delete(key)
    }
}

/**
 * In-Memory 기반 토큰 블랙리스트 서비스 (Fallback)
 * Redis가 없는 개발 환경에서 사용
 */
@Service
@ConditionalOnMissingBean(RedisTemplate::class)
class InMemoryTokenBlacklistService : TokenBlacklistService {

    private val blacklist = ConcurrentHashMap<String, Long>()

    override fun addToBlacklist(token: String, expirationSeconds: Long) {
        val expirationTime = System.currentTimeMillis() + (expirationSeconds * 1000)
        blacklist[token] = expirationTime
        cleanupExpiredTokens()
    }

    override fun isBlacklisted(token: String): Boolean {
        cleanupExpiredTokens()
        return blacklist.containsKey(token)
    }

    override fun removeFromBlacklist(token: String) {
        blacklist.remove(token)
    }

    private fun cleanupExpiredTokens() {
        val now = System.currentTimeMillis()
        blacklist.entries.removeIf { it.value < now }
    }
}
