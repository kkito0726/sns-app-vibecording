package com.example.flick.presentation.health

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "ヘルスチェック", description = "アプリケーションの稼働状態を確認するAPI")
@RestController
class HealthController {

    @GetMapping("/health")
    @Operation(summary = "ヘルスチェック", description = "アプリケーションが正常に起動しているかを確認します。")
    fun healthCheck(): String {
        return "Flick backend app is running!!"
    }
}
