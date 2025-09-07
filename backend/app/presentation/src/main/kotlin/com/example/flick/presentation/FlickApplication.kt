package com.example.flick.presentation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.example.flick")
class FlickApplication

fun main(args: Array<String>) {
	runApplication<FlickApplication>(*args)
}
