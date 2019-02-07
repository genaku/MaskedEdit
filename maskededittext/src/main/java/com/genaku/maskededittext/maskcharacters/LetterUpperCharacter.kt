package com.genaku.maskededittext.maskcharacters

internal class LetterUpperCharacter : UpperCaseCharacter() {

    override fun isValidCustom(ch: Char): Boolean =
        Character.isLetter(ch)

    override val viewChar: Char = 'S'
}
