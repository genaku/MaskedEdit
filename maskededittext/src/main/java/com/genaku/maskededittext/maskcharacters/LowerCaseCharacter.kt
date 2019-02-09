package com.genaku.maskededittext.maskcharacters

/**
 * Base abstract lower mask character
 *
 *  @author Gena Kuchergin
 */
abstract class LowerCaseCharacter : MaskCharacter() {

    override fun processCharacter(ch: Char): Char =
        Character.toLowerCase(ch)
}