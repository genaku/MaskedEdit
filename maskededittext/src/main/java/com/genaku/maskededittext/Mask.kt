package com.genaku.maskededittext

import com.genaku.maskededittext.Utils.Companion.min
import com.genaku.maskededittext.maskcharacters.FixedCharacter
import com.genaku.maskededittext.maskcharacters.MaskCharacter
import com.genaku.maskededittext.maskcharacters.MaskCharacterFabric
import java.util.ArrayList

class Mask() {

    private var maskChars: List<MaskCharacter> = emptyList()
    private val fabric: MaskCharacterFabric =
        MaskCharacterFabric()

    var formatString: String = ""
    val validCursorPositions = ArrayList<Int>()
    private var firstAllowedPosition: Int = 0
    private var lastAllowedPosition: Int = 0

    constructor(fmtString: String) : this() {
        formatString = fmtString
        maskChars = buildMask(formatString)
        initValidCursorPositions(maskChars)
    }

    val size
        get() = maskChars.size

    operator fun get(index: Int): MaskCharacter = maskChars[index]

    fun isValidFixedCharacter(ch: Char, at: Int): Boolean {
        if (at !in 0..maskChars.size) {
            return false
        }
        val maskChar = maskChars[at]
        return maskChar is FixedCharacter && maskChar.isValidCharacter(ch)
    }

    private fun buildMask(fmtString: String): List<MaskCharacter> {
        val result = ArrayList<MaskCharacter>()
        for (ch in fmtString.toCharArray()) {
            result.add(fabric.buildCharacter(ch))
        }
        return result
    }

    fun getFormattedString(value: String) =
        FormattedString(this, value)

    private fun initValidCursorPositions(mask: List<MaskCharacter>) {
        for (i in 0 until mask.size) {
            if (mask[i] !is FixedCharacter) {
                validCursorPositions.add(i)
            }
        }
        if (mask.isEmpty()) {
            firstAllowedPosition = 0
            lastAllowedPosition = 0
        } else {
            firstAllowedPosition = validCursorPositions[0]
            lastAllowedPosition = validCursorPositions[validCursorPositions.size - 1]
        }
    }

    fun getNextPosition(index: Int, isDeletion: Boolean): Int =
        Math.min(lastAllowedPosition, getNextAvailablePosition(index, isDeletion))

    private fun getNextAvailablePosition(pos: Int, isDeletion: Boolean): Int {
        if (validCursorPositions.contains(pos)) {
            val i = validCursorPositions.indexOf(pos)
            val iterator = validCursorPositions.listIterator(i)
            if (isDeletion) {
                if (iterator.hasPrevious()) {
                    return iterator.previous() + 1
                }
            } else {
                if (iterator.hasNext()) {
                    val newPos = iterator.next()
                    if (iterator.hasNext()) {
                        return iterator.next()
                    }
                    return newPos
                }
            }
            return pos
        } else {
            return findCloserIndex(pos, isDeletion)
        }
    }

    private fun findCloserIndex(pos: Int, isDeletion: Boolean): Int {
        val iterator: ListIterator<Int>
        if (isDeletion) {
            iterator = validCursorPositions.listIterator(validCursorPositions.size - 1)
            while (iterator.hasPrevious()) {
                val previous = iterator.previous()
                if (previous <= pos) {
                    return previous
                }
            }
            return firstAllowedPosition
        } else {
            val newPos = Math.max(firstAllowedPosition, pos)
            iterator = validCursorPositions.listIterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next > newPos) {
                    return next
                }
            }
            return lastAllowedPosition
        }
    }

    fun convertPos(str: String, pos: Int, deletion: Boolean = false): Int {
        if (pos < firstAllowedPosition) {
            return 0
        }

        val lastStrPos = str.length - 1
        val inputLen = min(size, pos, lastStrPos)
        var result = -1
        for (i in 0..inputLen) {
            val strChar = str[i]
            val maskChar = maskChars[i]
            if (maskChar !is FixedCharacter && strChar != maskChar.viewChar) {
                result++
            }
            if (i == lastStrPos) {
                if (deletion) {
                    result--
                } else {
                    result++
                }
            }
        }
        return result
    }

}
