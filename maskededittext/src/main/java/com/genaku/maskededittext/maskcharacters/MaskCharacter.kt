package com.genaku.maskededittext.maskcharacters

abstract class MaskCharacter {

    abstract fun isValidCharacter(ch: Char): Boolean

    abstract val viewChar: Char

    open fun processCharacter(ch: Char): Char = ch
}


