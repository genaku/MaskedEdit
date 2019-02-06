package com.genaku.maskededittext

import org.mym.plog.PLog

class MaskedHolder(fmtString: String) {

    private var formatter = MaskedFormatter(fmtString)
    private var mask: Mask = Mask(fmtString)

    var unmasked = ""

    val formatted: String
        get() = formatter.maskedString(unmasked)

    fun getPosition(index: Int, isDeletion: Boolean) =
        mask.getNextPosition(index, isDeletion)

    fun getLastInputPosition(): Int {
        return formatted.length
    }

    fun setMask(fmtString: String) {
        mask = Mask(fmtString)
        formatter = MaskedFormatter(fmtString)
    }

    fun deleteChars(start: Int, end: Int) {
        val len = unmasked.length
        if (start < len) {
            val last = Math.min(len, end)
            unmasked = unmasked.removeRange(start, last)
        }
    }

    fun replaceChars(start: Int, end: Int, chars: CharSequence) {
        PLog.d("replace chars [$start-$end] {$chars}")
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
    }

    fun deleteChars(input: String, start: Int, end: Int) {
        val trimmed = input.substring(0, getLastInputPosition())
        val unmaskedStart = mask.convertPos(trimmed, start, true)
        val unmaskedEnd = mask.convertPos(trimmed, end, true)
        deleteChars(unmaskedStart, unmaskedEnd)
    }

    fun replaceChars(input: String, start: Int, end: Int, chars: CharSequence) {
        val trimmed = input.substring(0, getLastInputPosition())
        val unmaskedStart = mask.convertPos(trimmed, start)
        val unmaskedEnd = mask.convertPos(trimmed, end)
        replaceChars(unmaskedStart, unmaskedEnd, chars)
    }
}