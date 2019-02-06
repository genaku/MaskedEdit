package com.genaku.maskededittext

import android.text.InputFilter
import android.text.Spanned

class MaskedInputFilter(
    val maskedHolder: MaskedHolder,
    val setSelection: (Int) -> Unit,
    val setText: (String) -> Unit
) : InputFilter {

    private var textSetup = false

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        source ?: return null
        if (textSetup) return source

//        PLog.d("source [$start-$end] [$source]")
//        PLog.d("dest [$dstart-$dend] [$dest]")

        val isDeletion = source.isEmpty()

        if (isDeletion) {
            maskedHolder.deleteChars(dest.toString(), dstart, dend)
        } else {
            maskedHolder.replaceChars(dest.toString(), dstart, dend, source)
        }
        val pos = maskedHolder.getNewPosition(dstart, isDeletion)

//        PLog.d("unmasked [${maskedHolder.unmasked}]")
//        PLog.d("masked [${maskedHolder.formatted}]")
//        PLog.d("new pos [$pos]")
//
        textSetup = true
        setText(maskedHolder.formatted)
        setSelection(pos)
        textSetup = false

        return null
    }
}
