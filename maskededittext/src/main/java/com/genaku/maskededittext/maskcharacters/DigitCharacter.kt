package com.genaku.maskededittext.maskcharacters

/**
 * Digital mask character
 *
 *  @author Gena Kuchergin
 */
class DigitCharacter : MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.isDigit(ch)

    override val viewChar: Char = '*'
}
