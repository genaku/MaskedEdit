package com.genaku.maskededittext

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

class Utils {

    companion object {

        @JvmStatic
        fun getColoredText(context: Context, source: CharSequence, color: Int): Spannable {
            val wordToSpan = SpannableString(source)
            wordToSpan.setSpan(
                ForegroundColorSpan(context.resources.getColor(color)),
                0, source.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return wordToSpan
        }

        @JvmStatic
        fun min(vararg values: Int): Int {
            var result = values[0]
            for (i in 1 until values.size) {
                result = Math.min(result, values[i])
            }
            return result
        }
    }
}