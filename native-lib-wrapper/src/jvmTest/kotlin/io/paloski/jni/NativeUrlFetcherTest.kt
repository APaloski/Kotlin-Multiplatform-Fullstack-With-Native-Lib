package io.paloski.jni

import junit.framework.Assert.assertNotNull
import junit.framework.Assert.fail
import org.junit.Test

class NativeUrlFetcherTest {

    @Test
    fun testSuccess() {
        val fetchUrl = NativeCurlUrlFetcher.fetchUrl("http://example.com")
        assertNotNull(fetchUrl)
    }

    @Test
    fun testException() {
        try {
            NativeCurlUrlFetcher.fetchUrl("http://127.0.0.1") //Assume this isn't open for http get calls
            fail("Did not get an expected exception :(")
        } catch (exp : CurlException) {
            //Success
        }
    }

}