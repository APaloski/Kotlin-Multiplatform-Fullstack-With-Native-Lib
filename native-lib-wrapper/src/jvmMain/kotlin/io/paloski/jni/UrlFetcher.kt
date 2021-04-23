package io.paloski.jni

import java.io.File
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths

interface UrlFetcher {
    fun fetchUrl(url : String) : String
}

class CurlException(message : String) : IOException(message)

object NativeCurlUrlFetcher : UrlFetcher {
    //A large portion of this craziness is needed because we hit file resources when running in an IDE/unit tests and jars when running in our other projects.
    // In a real project this would probably be a single static loader class
    init {
        val nativeResources = ClassLoader.getSystemClassLoader().getResource("natives").toURI()

        val files = mutableListOf<File>()
        when(nativeResources.scheme) {
            "jar" -> {
                val fileString = ClassLoader.getSystemClassLoader().getResource("natives").toURI().toASCIIString().split("!")
                val filename = fileString[0].substringAfter("jar:file:/")
                val path = fileString[1]

                val fs = FileSystems.newFileSystem(Paths.get(filename), null)
                val tempdir = Files.createTempDirectory("dlls-")
                Files.newDirectoryStream(fs.getPath(path)).forEach { jardDll ->
//                    val dll = Files.createFile()
                    val dll = Files.copy(jardDll, tempdir.resolve(jardDll.fileName.toString()))
                    files += dll.toFile()
                }
            }
            "file" -> { //IDE's!
                files += File(nativeResources.toURL().file).listFiles().filter { it.extension == "dll" }
            }
            else -> error("Unsupported natives scheme ${nativeResources.scheme}")
        }

        println("Attempting to load DLLs")
        //So we don't necessarily know the dependency order, so we need to be a little annoying with it
        val errors = mutableListOf<Throwable>()
        var oldSize : Int
        do {
            oldSize = files.size
            errors.clear()
            files.removeIf { dll ->
                println("\tTrying ${dll}")
                try {
                    System.load(dll.absolutePath)
                    true
                } catch (err : UnsatisfiedLinkError) {
                    println("\t\tUnsatisfied link, skipping for now")
                    errors += err
                    false
                }
            }

        } while (oldSize != files.size && files.isNotEmpty())
        if(files.isNotEmpty()) {
            val err =  UnsatisfiedLinkError("Could not load natives")
            errors.forEach { err.addSuppressed(it) }
            throw err
        }
        println("Done loading dlls")
    }

    external override fun fetchUrl(url: String) : String
}