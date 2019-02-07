package com.genaku.maskededittext.maskcharacters

internal class MaskCharacterFabric {

    fun buildCharacter(ch: Char): MaskCharacter = when (ch) {
        DIGIT_KEY -> DigitCharacter()
        LETTER_UPPER_KEY -> LetterUpperCharacter()
        LETTER_LOWER_KEY -> LetterLowerCharacter()
        CYRILLIC_UPPER_KEY -> CyrillicUpperCharacter()
        CYRILLIC_LOWER_KEY -> CyrillicLowerCharacter()
        LATIN_UPPER_KEY -> LatinUpperCharacter()
        LATIN_LOWER_KEY -> LatinLowerCharacter()
        ANYTHING_KEY -> AnyCharacter()
        ALPHA_NUMERIC_KEY -> AlphaNumericCharacter()
        HEX_KEY -> HexCharacter()
        else -> FixedCharacter(ch)
    }

    companion object {
        private const val ANYTHING_KEY = 'C'
        private const val DIGIT_KEY = '#'
        private const val LETTER_UPPER_KEY = 'S'
        private const val LETTER_LOWER_KEY = 's'
        private const val LATIN_UPPER_KEY = 'L'
        private const val LATIN_LOWER_KEY = 'l'
        private const val CYRILLIC_UPPER_KEY = 'R'
        private const val CYRILLIC_LOWER_KEY = 'r'
        private const val ALPHA_NUMERIC_KEY = 'A'
        private const val HEX_KEY = 'H'
    }
}

