package com.faendir.om.discord.jda

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jda")
class JdaProperties {
    lateinit var token: String
}