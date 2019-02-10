package com.genaku.maskededittext

import com.genaku.maskededittext.maskcharacters.FixedCharacter
import com.genaku.maskededittext.maskcharacters.MaskCharacter
import com.genaku.maskededittext.maskcharacters.MaskCharacterFabric

/**
 * Mask handler
 *
 *  @author Gena Kuchergin
 */
class Mask(formatString: String) {

    private val fabric = MaskCharacterFabric()
    private var maskChars: List<MaskCharacter> = buildMask(formatString)

    val validCursorPositions = ArrayList<Int>()
    val posAfterDelete = ArrayList<Int>()
    val posAfterAdd = ArrayList<Int>()
    val unmaskPos = ArrayList<Int>()
    val unmaskDelPos = ArrayList<Int>()
    private var firstAllowedPosition: Int = 0
    private var lastAllowedPosition: Int = 0

    init {
        initValidCursorPositions(maskChars)
        initIds()
    }

    val size = maskChars.size

    val isEmpty = (size == 0)

    val lastAllowedUnmaskedPos
        get() = validCursorPositions.size - 1

    operator fun get(index: Int): MaskCharacter = maskChars[index]

    /**
     * Get next cursor position after operation
     */
    fun getNextPosition(currentPos: Int, isDeletion: Boolean): Int = when { // order of when cases is important
        currentPos > lastAllowedPosition -> lastAllowedPosition
        isDeletion -> posAfterDelete[currentPos]
        else -> posAfterAdd[currentPos]
    }

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

    /**
     * Convert cursor position of formatted string to cursor position of unmasked string
     */
    fun convertPos(unmaskedLen: Int, pos: Int, isDeletion: Boolean = false): Int = when { // order of when cases is important
        isEmpty -> pos
        (pos < 0) -> 0
        (pos >= unmaskPos.size) -> unmaskedLen
        isDeletion -> min(unmaskedLen - 1, unmaskDelPos[pos])
        else -> min(unmaskedLen, unmaskPos[pos])
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
        if (mask.isEmpty()) {
            firstAllowedPosition = 0
            lastAllowedPosition = 0
        } else {
            firstAllowedPosition = validCursorPositions.first()
            lastAllowedPosition = validCursorPositions.last()
        }
    }

    private fun initIds() {
        initAfterOperationPos()
        initUnmaskDelPos()
        initUnmaskPos()
    }

    private fun initAfterOperationPos() {
        for (k in 0..lastAllowedPosition) {
            posAfterAdd.add(min(lastAllowedPosition, getNextAvailablePosition(k, false)))
            posAfterDelete.add(min(lastAllowedPosition, getNextAvailablePosition(k, true)))
        }
    }

    private fun initUnmaskDelPos() {
        posAfterDelete.forEach {
            unmaskDelPos.add(validCursorPositions.indexOf(it))
        }
    }

    private fun initUnmaskPos() {
        var i = 0
        var prevPos = 0
        validCursorPositions.forEach {
            for (j in prevPos..it) {
                unmaskPos.add(i)
            }
            prevPos = it + 1
            i++
        }
    }

    private fun min(vararg values: Int): Int {
        var result = values[0]
        for (i in 1 until values.size) {
            result = Math.min(result, values[i])
        }
        return result
    }
}
