package io.paloski.api.client

import kotlinx.browser.window

actual fun base64EncodeUrl(url : String) : String {
    return window.btoa(url)
}