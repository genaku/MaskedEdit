package com.genaku.maskededittext.maskcharacters

/**
 * Base abstract upper mask character
 *
 *  @author Gena Kuchergin
 */
abstract class UpperCaseCharacter : MaskCharacter() {

    override fun processCharacter(ch: Char): Char =
        Character.toUpperCase(ch)
}