package com.genaku.maskededittext.maskcharacters

/**
 * Lower letter mask character
 *
 *  @author Gena Kuchergin
 */
class LetterLowerCharacter : LowerCaseCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.isLetter(ch)

    override val viewChar: Char = 's'
}
