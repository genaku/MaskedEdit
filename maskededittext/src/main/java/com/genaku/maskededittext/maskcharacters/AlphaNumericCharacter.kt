package com.genaku.maskededittext.maskcharacters

internal class AlphaNumericCharacter : MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.isLetterOrDigit(ch)

    override val viewChar: Char = 'A'
}
