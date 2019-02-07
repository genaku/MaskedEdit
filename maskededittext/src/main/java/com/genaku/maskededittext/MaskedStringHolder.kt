package com.genaku.maskededittext

/**
 * Class to process masked string
 *
 *  @author Gena Kuchergin
 */
class MaskedStringHolder(fmtString: String) {

    private var maskString = fmtString
    private var mask: Mask = Mask(fmtString)

    var unmasked = ""

    val isEmpty
        get() = mask.isEmpty

    val formatted: CharSequence
        get() = mask.getFormatted(unmasked)

    val fullFormatted: CharSequence
        get() = mask.getFullFormatted(unmasked)

    fun getNewPosition(index: Int, isDeletion: Boolean) =
        mask.getNextPosition(index, isDeletion)

    fun setMask(fmtString: String) {
        maskString = fmtString
        mask = Mask(fmtString)
        trimUnmasked()
    }

    fun getMask(): String {
        return maskString
    }

    fun deleteUnmaskedChars(start: Int, end: Int) {
        val len = unmasked.length
        if (start < len) {
            val last = Math.min(len, end)
            unmasked = unmasked.removeRange(start, last)
        }
    }

    fun replaceUnmaskedChars(start: Int, end: Int, chars: CharSequence) {
//        PLog.d("replace chars [$start-$end] {$chars}")
        if (start > end || end < 0) {
            return
        }

        if (!mask.isEmpty && start >= mask.lastAllowedPos) {
            return
        }

        val unmaskedLen = unmasked.length
        if (start == unmaskedLen) {
            unmasked = unmasked.plus(chars)
        } else if (start < unmaskedLen) {
            val last = if (end >= unmaskedLen) unmaskedLen - 1 else end
            val charsLen = chars.length
            var replaceLen = last - start
            if (replaceLen > charsLen) {
                replaceLen = charsLen
            } else if (replaceLen < charsLen) {
                replaceLen = charsLen
            }
            unmasked = unmasked.replaceRange(start, end, chars.subSequence(0, replaceLen))
        } // else ignore

        trimUnmasked()
    }

    private fun trimUnmasked() {
        if (!mask.isEmpty && unmasked.length > mask.lastAllowedPos) {
            unmasked = unmasked.substring(0, mask.lastAllowedPos)
        }
    }

    fun deleteChars(input: String, start: Int, end: Int) {
        val lastInputPosition = formatted.length
        val trimmed = input.substring(0, lastInputPosition)
        val unmaskedStart = mask.convertPos(trimmed, start, true)
        val unmaskedEnd = mask.convertPos(trimmed, end, true)
        deleteUnmaskedChars(unmaskedStart, unmaskedEnd)
    }

    fun replaceChars(input: String, start: Int, end: Int, chars: CharSequence) {
        val lastInputPosition = formatted.length
        val trimmed = if (input.length >= lastInputPosition)
            input.substring(0, lastInputPosition)
        else
            input
        val unmaskedStart = mask.convertPos(trimmed, start)
        val unmaskedEnd = mask.convertPos(trimmed, end)
        replaceUnmaskedChars(unmaskedStart, unmaskedEnd, chars)
    }
}