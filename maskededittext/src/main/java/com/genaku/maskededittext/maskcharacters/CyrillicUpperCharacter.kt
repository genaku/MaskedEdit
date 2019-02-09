package com.genaku.maskededittext.maskcharacters

/**
 * Upper cyrillic mask character
 *
 *  @author Gena Kuchergin
 */
class CyrillicUpperCharacter : UpperCaseCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.CYRILLIC

    override val viewChar: Char = 'R'
}