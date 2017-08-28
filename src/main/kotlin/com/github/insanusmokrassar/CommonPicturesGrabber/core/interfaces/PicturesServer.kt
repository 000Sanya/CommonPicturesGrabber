package com.github.insanusmokrassar.CommonPicturesGrabber.core.interfaces

import com.github.insanusmokrassar.IObjectK.interfaces.IObject
import java.net.URI

interface PicturesServer {
    @Throws(IllegalStateException::class)
    fun nextImage(filter: IObject<Any>): URI
}
