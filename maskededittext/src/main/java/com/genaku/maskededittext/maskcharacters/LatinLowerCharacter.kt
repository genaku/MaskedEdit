package com.genaku.maskededittext.maskcharacters

class LatinLowerCharacter : LowerCaseCharacter() {

    override fun isValidCustom(ch: Char): Boolean =
        Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.BASIC_LATIN

    override val viewChar: Char = 'l'
}
