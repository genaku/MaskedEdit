package com.genaku.maskededittext.maskcharacters

/**
 * Upper latin mask character
 *
 *  @author Gena Kuchergin
 */
class LatinUpperCharacter : UpperCaseCharacter() {

    override fun isValidCustom(ch: Char): Boolean =
        Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.BASIC_LATIN

    override val viewChar: Char = 'L'
}
