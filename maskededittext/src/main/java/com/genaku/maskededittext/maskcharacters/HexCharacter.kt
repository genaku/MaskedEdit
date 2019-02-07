package com.genaku.maskededittext.maskcharacters

/**
 * Hex mask character
 *
 *  @author Gena Kuchergin
 */
class HexCharacter : MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean = ch.toUpperCase() in HEX_CHARS

    override fun processCharacter(ch: Char): Char = Character.toUpperCase(ch)

    override val viewChar: Char = 'H'

    companion object {
        private const val HEX_CHARS = "0123456789ABCDEF"
    }

}
