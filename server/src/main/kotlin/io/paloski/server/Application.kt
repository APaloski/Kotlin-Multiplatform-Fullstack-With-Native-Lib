package io.paloski.server

import io.paloski.jni.NativeCurlUrlFetcher

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.webjars.*
import java.util.*

fun Application.curlModule(testing : Boolean = false) {
    routing {
        get("/curl/{url}") {
            val encodedUrl = call.parameters["url"]
            requireNotNull(encodedUrl)
            val decodedUrl = Base64.getUrlDecoder().decode(encodedUrl).toString(Charsets.UTF_8)

            val fetcher = NativeCurlUrlFetcher
            val res = fetcher.fetchUrl(decodedUrl)
            call.respondText(res, contentType = ContentType.Text.Html)
        }
        static {
            resources("html")
        }
    }
    install(Webjars)
}