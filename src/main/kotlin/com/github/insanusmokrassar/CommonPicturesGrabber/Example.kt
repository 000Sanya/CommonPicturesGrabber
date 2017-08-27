package com.github.insanusmokrassar.CommonPicturesGrabber

import java.io.FileInputStream
import java.util.logging.LogManager

/**
 * Need "LOGGER_CONFIG_PATH" environment variable which set the place where was put .properties file with config of logger
 */
fun loadLoggerConfig() {
    FileInputStream(System.getenv("LOGGER_CONFIG_PATH")).use {
        LogManager.getLogManager().readConfiguration(it)
    }
}

/**
 * Need file with logger config
 */
fun loadLoggerConfig(file: FileInputStream) {
    LogManager.getLogManager().readConfiguration(file)
}

fun main(args: Array<String>) {
    loadLoggerConfig()
}