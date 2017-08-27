package com.github.insanusmokrassar.CommonPicturesGrabber.strategies

import com.github.insanusmokrassar.CommonPicturesGrabber.core.interfaces.PicturesServer
import com.github.insanusmokrassar.CommonPicturesGrabber.iocNameField
import com.github.insanusmokrassar.CommonPicturesGrabber.serversField
import com.github.insanusmokrassar.CommonPicturesGrabber.serversStrategyNameField
import com.github.insanusmokrassar.IOC.core.*
import com.github.insanusmokrassar.IObjectK.exceptions.ReadException
import com.github.insanusmokrassar.IObjectK.interfaces.IObject

private val doMap = mapOf<String?, (serversList: PicturesServerListStrategy, args: Array<out Any>) -> Any?>(
        Pair(
                null,
                {
                    servers, _ ->
                    servers.servers
                }
        ),
        Pair(
                "add",
                {
                    servers, args ->
                    if (args[1] is IObject<*>) {
                        servers.addServer(args[1] as IObject<Any>)
                    } else {
                        false
                    }
                }
        ),
        Pair(
                "remove",
                {
                    servers, args ->
                    if (args[1] is Int) {
                        servers.servers.removeAt(args[1] as Int)
                    } else {
                        false
                    }
                }
        )
)

/**
 * Await:
 * <pre>
 *     {
 *         "ioc": "Name of ioc strategy",
 *         "servers": [
 *             {
 *                 "package": "package.to.class.which.implement.PicturesServer",
 *                 "config": any // optional field which will used as params
 *             }
 *         ]
 *     }
 * </pre>
 */
class PicturesServerListStrategy(config: IObject<Any>) : IOCStrategy {

    internal val servers = ArrayList<PicturesServer>()
    private val ioc = getOrCreateIOC(config.get(iocNameField))

    init {
        val serversConfigs = config.get<List<IObject<Any>>>(serversField)
        serversConfigs.forEach {
            addServer(it)
        }
    }

    /**
     * If args is:
     * <ul>
     *     <li>empty - return current list</li>
     *     <li>["add", IObjectK as in constructor params] - add server</li>
     *     <li>["remove", int] - remove i server</li>
     * </ul>
     */
    override fun <T : Any> getInstance(vararg args: Any): T {
        try {
            if (args.isEmpty()) {
                doMap[null]?.let { return it(this, args) as T }
            } else {
                doMap[args[1] as String]?.let { return it(this, args) as T }
            }
        } catch (e: Exception) {
            throw ResolveStrategyException("Can't resolve servers actions with params $args", e)
        }
        throw ResolveStrategyException("Can't resolve servers actions with params $args")
    }

    internal fun addServer(params: IObject<Any>) {
        servers.add(ioc.resolve(params.get(packageKey), *getConfig(params)))
    }
}