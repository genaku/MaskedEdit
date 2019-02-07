package com.genaku.maskededittext.maskcharacters

/**
 * Upper letter mask character
 *
 *  @author Gena Kuchergin
 */
class LetterUpperCharacter : UpperCaseCharacter() {

    override fun isValidCustom(ch: Char): Boolean =
        Character.isLetter(ch)

    override val viewChar: Char = 'S'
}
