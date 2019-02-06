package com.genaku.maskededittext

import com.genaku.maskededittext.Utils.Companion.min
import com.genaku.maskededittext.maskcharacters.FixedCharacter

class MaskedHolder(private val fmtString: String) {

    private var formatter = MaskedFormatter(fmtString)
    private var mask: Mask = Mask(fmtString)

    fun getFmtMask() =
        fmtString

    var unmasked = ""

    val formatted: String
        get() = formatter.maskedString(unmasked)

    fun getPosition(index: Int, isDeletion: Boolean) =
        mask.getNextPosition(index, isDeletion)

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
        if (start > end || end < 0) {
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
        val unmaskedStart = mask.convertPos(input, start, true)
        val unmaskedEnd = mask.convertPos(input, end, true)
        deleteChars(unmaskedStart, unmaskedEnd)
    }

    fun replaceChars(input: String, start: Int, end: Int, chars: CharSequence) {
        val unmaskedStart = mask.convertPos(input, start)
        val unmaskedEnd = mask.convertPos(input, end)
        replaceChars(unmaskedStart, unmaskedEnd, chars)
    }

}