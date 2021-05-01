# Kotlin Multiplatform full-stack with native example

This project is intended to be a proof of concept for converting a native library to a webservice with a published API
client and web front end using kotlin/mulitplatform. 

## Features:
1. A webservice that proxies a native library, using kotlin/native to implement JNI methods for kotlin/jvm code
2. A webservice client that can be called from kotlin/jvm (for use in unit tests or otherwise), kotlin/js, and can easily be 
   ported to kotlin/native to support the normal kotlin-multiplatform iOS/Android/Web use case
3. A kotlin/js front end that is hosted as static content on the server, and that calls through the hosted webservice client

### Niceties:
1. The native library, CURL, is pulled through Gradle's dependency management. Allowing it to be cached by gradle, used in offline mode, etc. 
2. Package up the kotlin/js similar to a [webjar](https://www.webjars.org/). This will allow tools that expect that format to function on our JS code.
