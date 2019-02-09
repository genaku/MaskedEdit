package com.genaku.maskededittext

import com.genaku.maskededittext.maskcharacters.FixedCharacter
import com.genaku.maskededittext.maskcharacters.MaskCharacter
import com.genaku.maskededittext.maskcharacters.MaskCharacterFabric
import java.util.*

/**
 * Mask handler
 *
 *  @author Gena Kuchergin
 */
class Mask(formatString: String) {

    private val fabric = MaskCharacterFabric()
    private var maskChars: List<MaskCharacter> = buildMask(formatString)

    val validCursorPositions = ArrayList<Int>()
    private var firstAllowedPosition: Int = 0
    private var lastAllowedPosition: Int = 0

    init {
        initValidCursorPositions(maskChars)
    }

    val size = maskChars.size

    val isEmpty = (size == 0)

    val lastAllowedPos
        get() = validCursorPositions.size - 1

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

    private fun initValidCursorPositions(mask: List<MaskCharacter>) {
        for (i in 0 until mask.size) {
            if (mask[i] !is FixedCharacter) {
                validCursorPositions.add(i)
            }
        }
        validCursorPositions.add(mask.size)
//        PLog.d("valid size ${validCursorPositions.size}")
        if (mask.isEmpty()) {
            firstAllowedPosition = 0
            lastAllowedPosition = 0
        } else {
            firstAllowedPosition = validCursorPositions[0]
            lastAllowedPosition = validCursorPositions[validCursorPositions.size - 1]
        }
    }

    fun getNextPosition(index: Int, isDeletion: Boolean): Int =
            min(lastAllowedPosition, getNextAvailablePosition(index, isDeletion))

    private fun getNextAvailablePosition(pos: Int, isDeletion: Boolean): Int {
        if (validCursorPositions.contains(pos)) {
            val i = validCursorPositions.indexOf(pos)
            val iterator = validCursorPositions.listIterator(i)
            if (isDeletion) {
                return iterator.next()
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

    fun convertPos(input: String, pos: Int, deletion: Boolean = false): Int {
        if (isEmpty) {
            return pos
        }
        if (pos < firstAllowedPosition) {
            return 0
        }

        if (!deletion && pos == size - 1) {
            return lastAllowedPos - 1
        }

        val str = trimInputStr(input)

        val lastStrPos = str.length - 1

        var result = -1

        if (lastStrPos < 0) {
            val inputLen = min(size, pos)
            for (i in 0..inputLen) {
                val maskChar = maskChars[i]
                if (maskChar !is FixedCharacter) {
                    result++
                }
            }
        } else {
            val inputLen = min(size, pos, lastStrPos)
            for (i in 0..inputLen) {
                val strChar = str[i]
                val maskChar = maskChars[i]
                if (maskChar !is FixedCharacter && strChar != maskChar.viewChar) {
                    result++
                }
                if (i == lastStrPos) {
                    if (deletion) {
                        if (pos > lastStrPos) {
                            result++
                        }
                    } else {
                        result++
                    }
                }
            }
        }
        return result
    }

    private fun trimInputStr(str: String): String {
        val strBuilder = StringBuilder()
        val lastPos = min(str.length, maskChars.size) - 1
        for (i in lastPos downTo 0) {
            val maskChar = maskChars[i]
            val strChar = str[i]
            if (strChar != maskChar.viewChar || maskChar is FixedCharacter) {
                strBuilder.append(strChar)
            }
        }
        return strBuilder.toString().reversed()
    }

    private fun min(vararg values: Int): Int {
        var result = values[0]
        for (i in 1 until values.size) {
            result = Math.min(result, values[i])
        }
        return result
    }
}
