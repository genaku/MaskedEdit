package com.genaku.maskededittext.maskcharacters

internal class AnyCharacter : MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean = true

    override val viewChar: Char = '*'
}
