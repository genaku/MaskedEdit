package com.genaku.maskededittext.maskcharacters

/**
 * Any mask character
 *
 *  @author Gena Kuchergin
 */
class AnyCharacter : MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean = true

    override val viewChar: Char = '*'
}
