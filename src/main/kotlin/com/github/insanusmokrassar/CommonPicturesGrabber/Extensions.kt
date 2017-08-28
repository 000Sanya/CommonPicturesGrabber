package com.github.insanusmokrassar.CommonPicturesGrabber

import com.github.insanusmokrassar.IObjectK.interfaces.IObject

val andFilterName = "and"
val orFilterName = "or"
val notFilterName = "not"

fun IObject<Any>.filterTags(vararg tags: String): Boolean {
    var result = true
    if (keys().contains(andFilterName)) {
        val and = get<Any>(andFilterName)
        when(and) {
            is List<*> -> (and as? List<Any>)?.let { result = result.and(it.andFilter(*tags)) }
            is IObject<*> -> (and as? IObject<Any>)?.let { result = result.and(it.filterTags(*tags)) }
            else -> result = result.and(tags.contains(and))
        }
    }
    if (keys().contains(orFilterName)) {
        val or = get<Any>(orFilterName)
        when(or) {
            is List<*> -> (or as? List<Any>)?.let { result = result.and(it.orFilter(*tags)) }
            is IObject<*> -> (or as? IObject<Any>)?.let { result = result.and(it.filterTags(*tags)) }
            else -> result = result.and(tags.contains(or))
        }
    }
    if (keys().contains(notFilterName)) {
        val not = get<Any>(notFilterName)
        when(not) {
            is List<*> -> (not as? List<Any>)?.let { result = result.and(!it.andFilter(*tags)) }
            is IObject<*> -> (not as? IObject<Any>)?.let { result = result.and(!it.filterTags(*tags)) }
            else -> result = result.and(!tags.contains(not))
        }
    }
    return result
}

fun List<Any>.andFilter(vararg tags: String): Boolean {
    var result = true
    forEach {
        when (it) {
            is IObject<*> -> (it as? IObject<Any>)?.let { result = result.and(it.filterTags(*tags)) }
            is List<*> -> (it as? List<Any>)?.let { result = result.and(it.andFilter(*tags)) }
            else -> result = result.and(tags.contains(it))
        }
    }
    return result
}

fun List<Any>.orFilter(vararg tags: String): Boolean {
    var result = false
    forEach {
        when (it) {
            is IObject<*> -> (it as? IObject<Any>)?.let { result = result.or(it.filterTags(*tags)) }
            is List<*> -> (it as? List<Any>)?.let { result = result.or(it.andFilter(*tags)) }
            else -> result = result.or(tags.contains(it))
        }
    }
    return result
}

fun IObject<Any>.notFilter(vararg tags: String): Boolean {
    return !filterTags(*tags)
}
