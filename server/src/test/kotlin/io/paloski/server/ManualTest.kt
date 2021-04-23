package io.paloski.server

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.util.Identity.decode
import io.paloski.api.client.KtorUrlFetcherApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ManualTest {

    @Test
    fun test_curl() = runBlocking {
        val apiClient = KtorUrlFetcherApiClient(HttpClient(), "http://172.20.128.1:8080");
        val fetchedContent = apiClient.fetchUrl("http://example.com")
        println(fetchedContent)
    }

    @Test
    fun test_webjar() = runBlocking {
        val client = HttpClient()
        val fetchedContent = client.get<String>("http://172.20.128.1:8080/webjars/web-app/web-app.js")
        println(fetchedContent)
    }

    @Test
    fun test_html() = runBlocking {
        val client = HttpClient()
        val fetchedContent = client.get<String>("http://172.20.128.1:8080/index.html")
        println(fetchedContent)
    }

    companion object {
        const val SERVER_ADDR = ""
    }
}