package com.genaku.maskededittext.maskcharacters

internal class MaskCharacterFabric {

    fun buildCharacter(ch: Char): MaskCharacter = when (ch) {
        ANYTHING_KEY -> AnyCharacter()
        DIGIT_KEY -> DigitCharacter()
        UPPERCASE_KEY -> UpperCaseCharacter()
        LOWERCASE_KEY -> LowerCaseCharacter()
        ALPHA_NUMERIC_KEY -> AlphaNumericCharacter()
        CHARACTER_KEY -> LetterCharacter()
        HEX_KEY -> HexCharacter()
        CYRILLIC_KEY -> CyrillicCharacter()
        else -> FixedCharacter(ch)
    }

    companion object {
        private const val ANYTHING_KEY = '*'
        private const val DIGIT_KEY = '#'
        private const val UPPERCASE_KEY = 'U'
        private const val LOWERCASE_KEY = 'L'
        private const val ALPHA_NUMERIC_KEY = 'A'
        private const val CHARACTER_KEY = '?'
        private const val HEX_KEY = 'H'
        private const val CYRILLIC_KEY = 'R'
    }
}

