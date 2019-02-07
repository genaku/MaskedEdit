package com.genaku.maskededittext.maskcharacters

class CyrillicLowerCharacter : LowerCaseCharacter() {

    override fun isValidCustom(ch: Char): Boolean =
        Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.CYRILLIC

    override val viewChar: Char = 'r'
}
