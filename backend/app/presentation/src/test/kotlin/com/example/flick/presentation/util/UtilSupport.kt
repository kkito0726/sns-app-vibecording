package com.example.flick.presentation.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

open class UtilSupport {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun createResponseBodyJson(resultActions: ResultActions): JSONObject {
        return JSONObject(resultActions.andReturn().response.contentAsByteArray.toString(Charsets.UTF_8))
    }

    fun createResponseJsonArray(resultActions: ResultActions): JSONArray {
        return JSONArray((resultActions.andReturn().response.contentAsByteArray.toString(Charsets.UTF_8)))
    }
}