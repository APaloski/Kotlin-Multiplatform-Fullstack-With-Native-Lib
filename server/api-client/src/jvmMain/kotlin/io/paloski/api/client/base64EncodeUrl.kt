package io.paloski.api.client

import java.util.*

actual fun base64EncodeUrl(url : String) : String {
    return Base64.getUrlEncoder().encodeToString(url.toByteArray())
}