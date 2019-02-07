package com.genaku.maskededittext.maskcharacters

abstract class LowerCaseCharacter : MaskCharacter() {

    abstract fun isValidCustom(ch: Char): Boolean

    override fun isValidCharacter(ch: Char): Boolean =
        isValidCustom(ch) && Character.isLowerCase(ch)

    override fun processCharacter(ch: Char): Char =
        Character.toLowerCase(ch)
}