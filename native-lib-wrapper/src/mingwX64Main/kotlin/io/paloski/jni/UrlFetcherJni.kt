package io.paloski.jni

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.cstr
import kotlinx.cinterop.invoke
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString

import lib.curl.*
import platform.posix.size_t
import kotlin.test.assertNotNull

fun curlCallback(buffer: CPointer<ByteVar>,
                 size: size_t,
                 count: size_t,
                 userdata: COpaquePointer) : Long {
    val strBuilderRef = userdata.asStableRef<StringBuilder>()
    val strBuilder = strBuilderRef.get()
    strBuilder.append(buffer.toKString())
    return strBuilder.length.toLong()
}

private fun CPointer<JNIEnvVar>.findClassByName(name : String) : jclass? = memScoped {
    pointed.pointed?.FindClass?.invoke(this@findClassByName, name.cstr.ptr)
}

private fun CPointer<JNIEnvVar>.throwNewException(cls : jclass, message: String) = memScoped {
    pointed.pointed?.ThrowNew?.invoke(this@throwNewException, cls, message.cstr.ptr)
}

//See https://kotlinlang.org/docs/curl.html#consume-the-kotlin-api
@CName("Java_io_paloski_jni_NativeCurlUrlFetcher_fetchUrl")
fun native_curlUrlFetch(env : CPointer<JNIEnvVar>, clazz : jclass, jstring: jstring) : jstring? = memScoped {
    val curl = curl_easy_init()
    val outputBuilder = StringBuilder()
    try {
        if(curl != null) {
            val inputUrl = env.pointed.pointed?.GetStringUTFChars?.invoke(env, jstring, null)?.toKString()!!
            println(inputUrl)
            curl_easy_setopt(curl, CURLOPT_URL, inputUrl)
            curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L)
            val functPtr = staticCFunction(::curlCallback)
            curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, functPtr)

            curl_easy_setopt(curl, CURLOPT_WRITEDATA, StableRef.create(outputBuilder).asCPointer())
            val res = curl_easy_perform(curl)
            if (res != CURLE_OK) {
                val curlExpClass = env.findClassByName("io/paloski/jni/CurlException")
                assertNotNull(curlExpClass, "Must be able to find java class for Curl Exception")
                env.throwNewException(curlExpClass, curl_easy_strerror(res)?.toKString() ?: "Curl to url $inputUrl failed with error code $res")
            }
        }
    } finally {
        if(curl != null) {
            curl_easy_cleanup(curl)
        }
    }
    return@memScoped env.pointed.pointed?.NewStringUTF?.invoke(env, outputBuilder.toString().cstr.ptr)
}