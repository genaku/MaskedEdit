package com.genaku.maskededittext.maskcharacters

/**
 * Letter or digit mask character
 *
 *  @author Gena Kuchergin
 */
class AlphaNumericCharacter : MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.isLetterOrDigit(ch)

    override val viewChar: Char = 'A'
}
