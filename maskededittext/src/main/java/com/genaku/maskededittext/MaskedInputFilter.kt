package com.genaku.maskededittext

import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import com.redmadrobot.inputmask.helper.Mask
import com.redmadrobot.inputmask.model.CaretString
import org.mym.plog.PLog
import java.util.*

class MaskedInputFilter(
    val setSelection: (Int) -> Unit,
    val setText: (String) -> Unit
) : InputFilter {

    private var isUserInput = true
    private var textSetup = false

    private var firstAllowedPosition: Int = 0
    private var lastAllowedPosition: Int = 0

    private val validCursorPositions = ArrayList<Int>()

    private var maskedHolder = MaskedHolder("")

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
        if (source is SpannableStringBuilder) return source

        PLog.d("source [$start-$end] [$source]")
        PLog.d("dest [$dstart-$dend] [$dest]")

        if (start <= end && dstart < dend) {
            maskedHolder.deleteChars(dest.toString(), dstart, dend)
        } else {
            maskedHolder.replaceChars(dest.toString(), dstart, dend, source)
        }
        val pos = maskedHolder.getPosition(dstart, source.isEmpty())

        PLog.d("unmasked [${maskedHolder.unmasked}]")
        PLog.d("masked [${maskedHolder.formatted}]")
        PLog.d("new pos [$pos]")

        textSetup = true
        setText(maskedHolder.formatted)
        setSelection(pos)
        textSetup = false


        return filteredText(dest, source, start, end, dstart, dend)
    }

    fun setMask(mask: String) {
        maskedHolder.setMask(mask)
    }

    private fun filteredText(
        dest: Spanned?,
        source: CharSequence,
        start: Int,
        end: Int,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        val filteredStringBuilder = StringBuilder()
        filteredStringBuilder.append(source.toString().toUpperCase())
        // defaultMaskedSymbols == array that tells us which symbols should be replaced by default
        // and which symbols are part of mask
        val defaultMaskedSymbols = BooleanArray(dend - dstart + 1)

//        return filteredStringBuilder.toString()


//        val m = masked(source.toString())
//        replace(0, m.length-1, m.toString())
        return null

        for (i in 0..dend - dstart) {
            defaultMaskedSymbols[i] = isCharAllowed(dstart + i)
        }

        for (i in start until end) {
            val currentChar = source[i]

            if (defaultMaskedSymbols[0]) {
                isUserInput = false
//                replace(dstart, dstart + 1, "")
                isUserInput = true
                filteredStringBuilder.append(currentChar)
                val index: Int = if (!isCharAllowed(dstart + 1))
                    dstart + 1
                else
                    dstart

                skipSymbol(index)

            } else {
                if (dstart != maskedHolder.getFmtMask().length) {
                    val index: Int = if (!isCharAllowed(dstart))
                        dstart + 1
                    else
                        dstart
                    val position = skipSymbol(index)
//                    replace(position, position, Character.toString(currentChar))
                }
            }
        }
        if (isUserInput && TextUtils.isEmpty(source)) {//deletion detection
            if (dend != 0) {
                for (i in 0 until dend - dstart) {
                    if (defaultMaskedSymbols[i]) {
                        filteredStringBuilder.append("*")
                    } else {
                        filteredStringBuilder.append(maskedHolder.getFmtMask()[dstart + i])
                    }
                }
                skipSymbolAfterDeletion(dstart)
            }

        }

        return filteredStringBuilder.toString()
    }

    private fun masked(input: String): String {
        val mask = Mask("+7 ([000]) [000] [00] [00]")
//        val input = "+71234567890"
        val result = mask.apply(
            CaretString(
                input,
                input.length
            ),
            false // you may consider disabling autocompletion for your case
        )
        return result.formattedText.string
    }

    private fun skipSymbol(index: Int): Int {
        var position = getNextAvailablePosition(index, false)
        if (position > lastAllowedPosition)
            position = lastAllowedPosition
        setSelection(position)
        return position
    }

    private fun skipSymbolAfterDeletion(index: Int) {
        val position = getNextAvailablePosition(index, true)
        setSelection(position)
    }

    private fun getNextAvailablePosition(index: Int, isDeletion: Boolean): Int {
        if (validCursorPositions.contains(index)) {
            val i = validCursorPositions.indexOf(index)
            val iterator = validCursorPositions.listIterator(i)
            if (isDeletion) {
                if (iterator.hasPrevious()) return iterator.previous() + 1
            } else {
                if (iterator.hasNext()) return iterator.next()
            }
            return index
        } else {
            return findCloserIndex(index, isDeletion)
        }
    }

    private fun findCloserIndex(index: Int, isDeletion: Boolean): Int {
        val iterator: ListIterator<Int>
        if (isDeletion) {
            iterator = validCursorPositions.listIterator(validCursorPositions.size - 1)
            while (iterator.hasPrevious()) {
                val previous = iterator.previous()
                if (previous <= index)
                    return previous + 1
            }
            return firstAllowedPosition
        } else {
            if (index > firstAllowedPosition) {
                iterator = validCursorPositions.listIterator()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next >= index)
                        return next - 1
                }
                return lastAllowedPosition
            } else {
                return firstAllowedPosition
            }
        }
    }

    private fun isCharAllowed(index: Int): Boolean =
        index < maskedHolder.getFmtMask().length && maskedHolder.getFmtMask()[index] == "*".toCharArray()[0]

}
