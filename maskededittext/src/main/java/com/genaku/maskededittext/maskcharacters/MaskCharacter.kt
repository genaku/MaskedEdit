package com.genaku.maskededittext.maskcharacters

/**
 * Base abstract mask character class
 *
 *  @author Gena Kuchergin
 */
abstract class MaskCharacter {

    abstract fun isValidCharacter(ch: Char): Boolean

    abstract val viewChar: Char

    open fun processCharacter(ch: Char): Char = ch
}


