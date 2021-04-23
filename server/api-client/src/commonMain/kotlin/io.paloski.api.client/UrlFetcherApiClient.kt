package io.paloski.api.client

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

interface UrlFetcherApiClient {

    suspend fun fetchUrl(url : String) : String

}

class KtorUrlFetcherApiClient(private val client : HttpClient, private val baseUrl : String) : UrlFetcherApiClient {
    override suspend fun fetchUrl(url: String): String {
        return client.get(Url("$baseUrl/curl/${base64EncodeUrl(url)}"))
    }
}

internal expect fun base64EncodeUrl(url : String) : String