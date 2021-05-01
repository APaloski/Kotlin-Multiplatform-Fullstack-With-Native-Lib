package io.paloski.web.app

import io.ktor.client.*
import io.paloski.api.client.KtorUrlFetcherApiClient
import io.paloski.api.client.UrlFetcherApiClient
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.button
import kotlinx.css.div
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.button
import react.dom.div
import react.dom.input
import react.dom.render
import react.dom.textArea

fun main() {

    render(document.getElementById("root")) {
        child(CurlApp::class){}
    }
}

external interface CurlAppState : RState {
    var urlToFetch : String?
    var fetchingResults : String?
}

class CurlApp : RComponent<RProps, CurlAppState>() {
    override fun RBuilder.render() {
        div {
            input(type = InputType.text, name = "itemText") {
                key = "itemText"

                attrs {
                    value = state.urlToFetch.orEmpty()
                    placeholder = "Url to fetch"
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        setState {
                            urlToFetch = target.value
                        }
                    }
                }
            }

            button {
                +"CURL"
                attrs {
                    onClickFunction = {
                        if (!state.urlToFetch.isNullOrEmpty()) {
                            val urlToFetch = state.urlToFetch!!
                            GlobalScope.launch {
                                val curlResult = KtorUrlFetcherApiClient(HttpClient(), "http://172.20.128.1:8080/").fetchUrl(urlToFetch)
                                setState {
                                    fetchingResults = curlResult
                                }
                            }
                        }
                    }
                }
            }
        }
        val storedFetchRes = state.fetchingResults
        if(storedFetchRes != null) {
            textArea {
                +storedFetchRes
            }
        }
    }
}