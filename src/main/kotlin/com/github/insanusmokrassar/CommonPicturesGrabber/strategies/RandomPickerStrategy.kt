package com.github.insanusmokrassar.CommonPicturesGrabber.strategies

import com.github.insanusmokrassar.CommonPicturesGrabber.core.interfaces.PicturesServer
import com.github.insanusmokrassar.CommonPicturesGrabber.iocNameField
import com.github.insanusmokrassar.CommonPicturesGrabber.serversStrategyNameField
import com.github.insanusmokrassar.IOC.core.IOC
import com.github.insanusmokrassar.IOC.core.IOCStrategy
import com.github.insanusmokrassar.IOC.core.getOrCreateIOC
import com.github.insanusmokrassar.IObjectK.interfaces.IObject
import java.util.*

private val random = Random()

/**
 * Need as config next:
 * <pre>
 *     {
 *         "ioc": "Name of IOC",
 *         "serversStrategy": "Name of servers startegy"
 *     }
 * </pre>
 */
class RandomPickerStrategy(params: IObject<Any>) : IOCStrategy {

    private val ioc = getOrCreateIOC(params.get(iocNameField))
    private val strategyName = params.get<String>(serversStrategyNameField)

    /**
     * Await first args as filter IObject<Any>
     */
    override fun <T : Any> getInstance(vararg args: Any): T {
        val servers = ioc.resolve<List<PicturesServer>>(strategyName)
        return servers[random.nextInt(servers.size)]
                .nextImage(args[0] as IObject<Any>) as T
    }
}