package com.genaku.maskededittext.maskcharacters

/**
 * Upper letter mask character
 *
 *  @author Gena Kuchergin
 */
class LetterUpperCharacter : UpperCaseCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.isLetter(ch)

    override val viewChar: Char = 'S'
}
