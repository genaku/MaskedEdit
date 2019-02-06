package com.genaku.maskededittext.maskcharacters

internal class LowerCaseCharacter : MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.isLowerCase(ch)

    override fun processCharacter(ch: Char): Char =
        Character.toLowerCase(ch)

    override val viewChar: Char = 'L'
}
