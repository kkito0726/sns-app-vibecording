package com.example.flick.presentation

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan("com.example.flick")
@OpenAPIDefinition(
    info = Info(
        title = "Flick アプリケーションAPI",
        version = "v1",
        description = "FlickソーシャルネットワーキングアプリケーションのためのRESTful APIドキュメントです。\n" +
                      "ユーザー管理、投稿、いいね、コメントなどの機能を提供します。\n" +
                      "各エンドポイントの詳細、リクエスト・レスポンスの形式については以下を参照してください。"
    )
)
class FlickApplication

fun main(args: Array<String>) {
    runApplication<FlickApplication>(*args)
}
