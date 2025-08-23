package com.itenary.shared

import com.itenary.shared.data.dto.ItineraryRequestDto
import com.itenary.shared.data.remote.ItineraryApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ItineraryApiTest {
    private fun clientWith(handler: suspend (io.ktor.client.request.HttpRequestData) -> io.ktor.client.engine.mock.MockHttpResponse) =
        HttpClient(MockEngine { request -> handler(request) }) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    }
                )
            }
        }

    @Test
    fun success_200_minimal_response() = runTest {
        val responseJson = """
            {"result":[{"day":"2025-01-01","timeline":[{
              "timeStart":"08:00","timeEnd":"09:00","destination":"A","shortDescription":"s","longDescription":"l",
              "pinpointMapsUrl":"u","lat":1.0,"lng":2.0,
              "expendEstimation":{"amount":10.0,"currency":"USD"},
              "stayDurationMin":30,"stayDurationMax":60,
              "categories":["food"],"priceBand":"$","familyFriendly":true,
              "weatherSensitivity":"indoor","source":"src","confidence":0.9
            }]}]}
        """.trimIndent()
        val client = clientWith { _ ->
            respond(
                content = responseJson,
                headers = headersOf("Content-Type", ContentType.Application.Json.toString())
            )
        }
        val api = ItineraryApiImpl(client, "https://example.com")
        val dto = ItineraryRequestDto("c","p","ID","2025-01-01","2025-01-02","en")
        val res = api.generate(dto)
        assertEquals(1, res.result.size)
        assertEquals("2025-01-01", res.result.first().day)
    }

    @Test
    fun error_400_validation_envelope() = runTest {
        val responseJson = """
            {"error":{"code":"VALIDATION","message":"invalid"}}
        """.trimIndent()
        val client = clientWith { _ ->
            respond(
                content = responseJson,
                status = io.ktor.http.HttpStatusCode.BadRequest,
                headers = headersOf("Content-Type", ContentType.Application.Json.toString())
            )
        }
        val api = ItineraryApiImpl(client, "https://example.com")
        val dto = ItineraryRequestDto("c","p","ID","2025-01-01","2025-01-02","en")
        assertFails { api.generate(dto) }
    }

    @Test
    fun error_500_server() = runTest {
        val client = clientWith { _ ->
            respond(
                content = "oops",
                status = io.ktor.http.HttpStatusCode.InternalServerError,
                headers = headersOf("Content-Type", ContentType.Text.Plain.toString())
            )
        }
        val api = ItineraryApiImpl(client, "https://example.com")
        val dto = ItineraryRequestDto("c","p","ID","2025-01-01","2025-01-02","en")
        assertFails { api.generate(dto) }
    }
}


