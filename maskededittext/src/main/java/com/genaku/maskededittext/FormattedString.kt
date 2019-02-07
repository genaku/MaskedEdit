package com.genaku.maskededittext

import com.genaku.maskededittext.maskcharacters.FixedCharacter

class FormattedString(private val mask: Mask, rawString: String) : CharSequence {

    private val formatted = formatString(rawString)

    override fun toString(): String = formatted

    override val length: Int
        get() = toString().length

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
        toString().subSequence(startIndex, endIndex)

    override fun get(index: Int): Char = toString()[index]

    fun buildRawString(str: String): String {
        val builder = StringBuilder()
        val inputLen = Math.min(mask.size, str.length)
        for (i in 0 until inputLen) {
            val ch = str[i]
            val character = mask[i]
            if (mask.isValidFixedCharacter(ch, i) || ch == character.viewChar) {
                continue
            }
            builder.append(ch)
        }
        return builder.toString()
    }

    private fun formatString(rawString: String): String {
        if (rawString.isEmpty()) {
            return ""
        }

        val builder = StringBuilder()

        var strIndex = 0
        var maskCharIndex = 0

        while (strIndex < rawString.length && maskCharIndex < mask.size) {
            val maskChar = mask[maskCharIndex]
            val stringCharacter = rawString[strIndex]
            when {
                maskChar.isValidCharacter(stringCharacter) -> {
                    builder.append(maskChar.processCharacter(stringCharacter))
                    strIndex++
                    maskCharIndex++
                }
                maskChar is FixedCharacter -> {
                    builder.append(maskChar.processCharacter(stringCharacter))
                    maskCharIndex++
                }
                else -> strIndex++
            }
        }

        // append fixed chars if need
        var isFixedChar = true
        while (isFixedChar && maskCharIndex < mask.size) {
            val maskChar = mask[maskCharIndex]
            isFixedChar = maskChar is FixedCharacter
            if (isFixedChar) {
                builder.append(maskChar.viewChar)
            }
            maskCharIndex++
        }

        return builder.toString()
    }

}
