package com.genaku.maskededittext.maskcharacters

/**
 * Lower cyrillic mask character
 *
 *  @author Gena Kuchergin
 */
class CyrillicLowerCharacter : LowerCaseCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.CYRILLIC

    override val viewChar: Char = 'r'
}
