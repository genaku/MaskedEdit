package com.genaku.maskededittext

import com.genaku.maskededittext.maskcharacters.FixedCharacter

/**
 * Converts unmasked string into masked
 *
 *  @author Gena Kuchergin
 */
class MaskFormatter {

    companion object {

        @JvmStatic
        fun formatString(mask: Mask, rawString: String): CharSequence {
            if (rawString.isEmpty()) {
                return ""
            }

            val builder = StringBuilder()
            var maskCharIndex = prebuild(builder, mask, rawString)

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

        @JvmStatic
        fun getFullFormatted(mask: Mask, rawString: String): CharSequence {
            val builder = StringBuilder()
            var maskCharIndex = prebuild(builder, mask, rawString)

            // append the rest of mask
            while (maskCharIndex < mask.size) {
                builder.append(mask[maskCharIndex].viewChar)
                maskCharIndex++
            }

            return builder.toString()
        }

        private fun prebuild(builder: StringBuilder, mask: Mask, rawString: String): Int {
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
            return maskCharIndex
        }

        @JvmStatic
        fun buildRawString(mask: Mask, input: String): String {
            val builder = StringBuilder()
            val inputLen = Math.min(mask.size, input.length)
            for (i in 0 until inputLen) {
                val ch = input[i]
                val character = mask[i]
                if (mask.isValidFixedCharacter(ch, i) || ch == character.viewChar) {
                    continue
                }
                builder.append(ch)
            }
            return builder.toString()
        }

    }
}
