package com.genaku.maskededittext.maskcharacters

internal class UpperCaseCharacter : MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.isUpperCase(ch)

    override fun processCharacter(ch: Char): Char =
        Character.toUpperCase(ch)

    override val viewChar: Char = 'U'

}
