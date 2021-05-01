# Kotlin Multiplatform full-stack with native example

This project is intended to be a proof of concept for converting a native library to a webservice with a published API
client and web front end using kotlin/mulitplatform. 

## Goals:
1. Create a webservice that proxies a native library, using kotlin/native to implement JNI methods for kotlin/jvm code
2. Create a webservice client that can be called from kotlin/jvm (for use in unit tests), kotlin/js, and can easily be 
   ported to kotlin/native to support the normal kotlin-multiplatform iOS/Android/Web use case
3. Create a kotlin/js front end that is hosted as static content that calls through the webservice client

## Niceties
1. Pull the native library through Gradle's dependency management system - Custom ivy repository
2. Package up the kotlin/js similar to a [webjar](https://www.webjars.org/)
