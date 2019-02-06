package com.genaku.maskededittext.maskcharacters

internal class LetterCharacter : MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.isLetter(ch)

    override val viewChar: Char = '?'
}
