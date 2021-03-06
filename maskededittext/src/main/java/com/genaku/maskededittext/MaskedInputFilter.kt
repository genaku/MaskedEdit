package com.genaku.maskededittext

import android.text.InputFilter
import android.text.Spanned
import org.mym.plog.PLog

/**
 * Android EditText input filter for masked text
 *
 *  @author Gena Kuchergin
 */
class MaskedInputFilter(
    val maskedStringHolder: MaskedStringHolder,
    val setSelection: (Int) -> Unit,
    val setText: (CharSequence) -> Unit
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
        if (maskedStringHolder.isEmpty) return source

        PLog.d("source [$start-$end] [$source]")
        PLog.d("dest [$dstart-$dend] [$dest]")

        val isDeletion = source.isEmpty()

        if (isDeletion) {
            maskedStringHolder.deleteChars(dstart, dend)
        } else {
            maskedStringHolder.replaceChars(dstart, dend, source)
        }

        val pos = maskedStringHolder.getNewPosition(dstart, isDeletion)

        refreshText(pos)
        return null
    }

    fun refreshText(pos: Int? = null) {
        textSetup = true
        val formatted = maskedStringHolder.formatted
        val correctPos = Math.min(pos ?: formatted.length, formatted.length)
        setText(formatted)
        setSelection(correctPos)

        PLog.d("unmasked [${maskedStringHolder.unmasked}]")
        PLog.d("masked [${maskedStringHolder.formatted}]")
        PLog.d("new pos [$correctPos]")

        textSetup = false
    }
}
