package com.github.insanusmokrassar.CommonPicturesGrabber.core.interfaces

import java.net.URI

interface PicturesServer {

    @Throws(IllegalStateException::class)
    fun nextImage(vararg tags: String): URI

    @Throws(IllegalStateException::class)
    fun close()
}
