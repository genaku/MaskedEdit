package com.genaku.maskededittext.maskcharacters

class LetterLowerCharacter : LowerCaseCharacter() {

    override fun isValidCustom(ch: Char): Boolean =
        Character.isLetter(ch)

    override val viewChar: Char = 's'
}
