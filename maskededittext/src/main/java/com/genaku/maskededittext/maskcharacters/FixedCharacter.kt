package com.genaku.maskededittext.maskcharacters

/**
 * Fixed mask character (character not to be changed)
 *
 *  @author Gena Kuchergin
 */
class FixedCharacter(private val character: Char): MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean = (character == ch)

    override fun processCharacter(ch: Char): Char = character

    override val viewChar: Char = character
}