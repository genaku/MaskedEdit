package com.genaku.maskededittext

fun min(vararg values: Int): Int {
    var result = values[0]
    for (i in 1 until values.size) {
        result = Math.min(result, values[i])
    }
    return result
}