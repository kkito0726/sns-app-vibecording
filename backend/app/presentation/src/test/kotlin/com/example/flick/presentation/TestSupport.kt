package com.example.flick.presentation

import com.example.flick.gen.jooq.tables.references.FLICKS
import com.example.flick.gen.jooq.tables.references.USERS
import com.example.flick.presentation.config.SecurityConfig
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.db.type.Changes
import org.assertj.db.type.Table
import org.jooq.DSLContext
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import javax.sql.DataSource

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig::class)
open class TestSupport {
    @Autowired
    lateinit var dslContext: DSLContext

    @Autowired
    lateinit var dataSource: DataSource

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun deleteAll() {
        dslContext.deleteFrom(USERS).execute()
        dslContext.deleteFrom(FLICKS).execute()
    }

    @BeforeEach
    open fun setUp() {
        deleteAll()
    }

    fun createChanges(tableNames: List<String>): Changes {
        return Changes(
            *tableNames.map {
                Table(dataSource, it)
            }.toTypedArray()
        )
    }

    fun createResponseBodyJson(resultActions: ResultActions): JSONObject {
        return JSONObject(resultActions.andReturn().response.contentAsByteArray.toString(Charsets.UTF_8))
    }

    fun createResponseJsonArray(resultActions: ResultActions): JSONArray {
        return JSONArray((resultActions.andReturn().response.contentAsByteArray.toString(Charsets.UTF_8)))
    }
}