package org.example.trikr.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import org.example.trikr.data.storage.TokenStorage
import org.example.trikr.domain.models.AuthResponse
import org.example.trikr.domain.models.GoogleLoginRequest
import org.example.trikr.domain.models.StartTaskRequest
import org.example.trikr.domain.models.TaskHistoryResponse
import org.example.trikr.domain.models.DailyStatsResponse
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TickrApiClient(
    private val tokenStorage: TokenStorage
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private val baseUrl = "https://tickr-backend-k1ot.onrender.com/api"

    suspend fun loginWithGoogle(idToken: String): Result<AuthResponse> {
        return try {
            val response: HttpResponse = client.post("\$baseUrl/auth/google") {
                contentType(ContentType.Application.Json)
                setBody(GoogleLoginRequest(idToken))
            }
            if (response.status.isSuccess()) {
                val authResponse = response.body<AuthResponse>()
                Result.success(authResponse)
            } else {
                Result.failure(Exception("HTTP \${response.status.value}: \${response.status.description}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun startTask(name: String): Result<Unit> {
        return try {
            val token = tokenStorage.getToken() ?: return Result.failure(Exception("No auth token found"))
            val response: HttpResponse = client.post("$baseUrl/tasks/start") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(StartTaskRequest(name))
            }
            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun stopTask(id: String): Result<Unit> {
        return try {
            val token = tokenStorage.getToken() ?: return Result.failure(Exception("No auth token found"))
            val response: HttpResponse = client.post("$baseUrl/tasks/$id/stop") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getTasksHistory(startTime: String, endTime: String): Result<TaskHistoryResponse> {
        return try {
            val token = tokenStorage.getToken() ?: return Result.failure(Exception("No auth token found"))
            val response: HttpResponse = client.get("$baseUrl/tasks/history") {
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("start_time", startTime)
                parameter("end_time", endTime)
            }
            if (response.status.isSuccess()) {
                val historyResponse = response.body<TaskHistoryResponse>()
                Result.success(historyResponse)
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getDailyStats(startTime: String, endTime: String, timezone: String): Result<DailyStatsResponse> {
        return try {
            val token = tokenStorage.getToken() ?: return Result.failure(Exception("No auth token found"))
            val response: HttpResponse = client.get("$baseUrl/tasks/stats/daily") {
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("start_time", startTime)
                parameter("end_time", endTime)
                parameter("timezone", timezone)
            }
            if (response.status.isSuccess()) {
                val statsResponse = response.body<DailyStatsResponse>()
                Result.success(statsResponse)
            } else {
                Result.failure(Exception("HTTP ${response.status.value}: ${response.status.description}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
