package com.jikgwan

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class JikgwanApplication

fun main(args: Array<String>) {
    runApplication<JikgwanApplication>(*args)
}
