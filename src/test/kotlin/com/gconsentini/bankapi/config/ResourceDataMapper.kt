package com.gconsentini.bankapi.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.io.FileReader

class ResourceDataMapper {
    companion object {
        private val mapper = ObjectMapper().registerModule(KotlinModule()).findAndRegisterModules()
        private const val dataPath = "src/test/resources/"

        fun getFrom(fileName: String, requiredType: Class<out Any>): Any? {
            val filePath = File(dataPath + fileName).absolutePath.toString()
            return mapper.readValue(FileReader(filePath), requiredType)
        }

        fun getFromAsText(fileName: String): String {
            val filePath = File(dataPath + fileName).absolutePath.toString()
            return File(filePath).readText(Charsets.UTF_8)
        }
    }
}