package com.genaku.maskededittext.maskcharacters

class CyrillicCharacter : MaskCharacter() {

    override fun isValidCharacter(ch: Char): Boolean =
        Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.CYRILLIC

    override val viewChar: Char = 'R'
}