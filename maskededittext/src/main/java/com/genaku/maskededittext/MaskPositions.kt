package com.genaku.maskededittext

import com.genaku.maskededittext.maskcharacters.FixedCharacter
import com.genaku.maskededittext.maskcharacters.MaskCharacter

/**
 * Masked string character positions holder
 *
 *  @author Gena Kuchergin
 */
class MaskPositions(maskChars: List<MaskCharacter>) {

    private var firstAllowedPosition: Int = 0
    var lastAllowedPosition: Int = 0
        private set

    val validCursorPositions = ArrayList<Int>()
    val posAfterDelete = ArrayList<Int>()
    val posAfterAdd = ArrayList<Int>()
    val unmaskPos = ArrayList<Int>()
    val unmaskDelPos = ArrayList<Int>()

    val lastAllowedUnmaskedPos
        get() = validCursorPositions.size - 1

    init {
        initValidCursorPositions(maskChars)
        initIds()
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
}