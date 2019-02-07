package com.genaku.maskededittext.maskcharacters

internal class DigitCharacter : MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.isDigit(ch)

    override val viewChar: Char = '*'
}
