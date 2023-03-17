package hn.single.imageapp.common.bases

import android.view.View

object ViewExt {

    fun View?.show() {
        this?.visibility = View.VISIBLE
    }

    fun View?.gone() {
        this?.visibility = View.GONE
    }

    fun View?.invisible() {
        this?.visibility = View.INVISIBLE
    }
}