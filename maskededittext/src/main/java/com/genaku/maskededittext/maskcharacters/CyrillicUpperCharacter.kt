package com.genaku.maskededittext.maskcharacters

class CyrillicUpperCharacter : UpperCaseCharacter() {

    override fun isValidCustom(ch: Char): Boolean =
        Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.CYRILLIC

    override val viewChar: Char = 'R'
}