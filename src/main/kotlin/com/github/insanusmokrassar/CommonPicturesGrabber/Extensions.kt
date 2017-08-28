package com.github.insanusmokrassar.CommonPicturesGrabber

import com.github.insanusmokrassar.IObjectK.interfaces.IObject

val andFilterName = "and"
val orFilterName = "or"
val notFilterName = "not"

fun IObject<Any>.filterTags(vararg tags: String): Boolean {
    var andResult = true
    if (keys().contains(andFilterName)) {
        val and = get<Any>(andFilterName)
        when(and) {
            is List<*> -> (and as? List<Any>)?.let { andResult = andResult.and(it.andFilter(*tags)) }
            is IObject<*> -> (and as? IObject<Any>)?.let { andResult = andResult.and(it.filterTags(*tags)) }
            else -> andResult = andResult.and(tags.contains(and))
        }
    }
    var orResult = false
    if (keys().contains(orFilterName)) {
        val or = get<Any>(orFilterName)
        when(or) {
            is List<*> -> (or as? List<Any>)?.let { orResult = orResult.or(it.orFilter(*tags)) }
            is IObject<*> -> (or as? IObject<Any>)?.let { orResult = orResult.or(it.filterTags(*tags)) }
            else -> orResult = orResult.or(tags.contains(or))
        }
    } else {
        orResult = true
    }
    var notResult = true
    if (keys().contains(notFilterName)) {
        val not = get<Any>(notFilterName)
        when(not) {
            is List<*> -> (not as? List<Any>)?.let { notResult = notResult.and(it.andFilter(*tags)) }
            is IObject<*> -> (not as? IObject<Any>)?.let { notResult = notResult.and(it.filterTags(*tags)) }
            else -> notResult = notResult.and(tags.contains(not))
        }
    } else {
        notResult = false
    }
    return andResult.and(orResult).and(!notResult)
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
