package com.taskworld.android.restfulandroidkotlin.extensions

/**
 * Created by Kittinun Vantasin on 10/29/14.
 */

fun StringBuilder.plus(add: String?): StringBuilder {
    if (add != null) {
        append(add)
    }
    return this
}
