package com.genaku.maskededittext.maskcharacters

/**
 * Base abstract upper mask character
 *
 *  @author Gena Kuchergin
 */
abstract class UpperCaseCharacter : MaskCharacter() {

    abstract fun isValidCustom(ch: Char): Boolean

    override fun isValidCharacter(ch: Char): Boolean =
        isValidCustom(ch) && Character.isUpperCase(ch)

    override fun processCharacter(ch: Char): Char =
        Character.toUpperCase(ch)
}