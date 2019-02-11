package com.genaku.maskededittext

import com.genaku.maskededittext.maskcharacters.MaskCharacter
import com.genaku.maskededittext.maskcharacters.MaskCharacterFabric

/**
 * Mask handler
 *
 *  @author Gena Kuchergin
 */
class Mask(formatString: String) {

    private val maskChars: List<MaskCharacter> = buildMask(formatString)
    private val maskPositions = MaskPositions(maskChars)

    val size = maskChars.size

    val isEmpty = (size == 0)

    val lastAllowedUnmaskedPos = maskPositions.lastAllowedUnmaskedPos

    operator fun get(index: Int): MaskCharacter = maskChars[index]

    /**
     * Get next cursor position after operation
     */
    fun getNextPosition(currentPos: Int, isDeletion: Boolean): Int = when { // order of when cases is important
        currentPos > maskPositions.lastAllowedPosition -> maskPositions.lastAllowedPosition
        isDeletion -> maskPositions.posAfterDelete[currentPos]
        else -> maskPositions.posAfterAdd[currentPos]
    }

    /**
     * Convert cursor position of formatted string to cursor position of unmasked string
     */
    fun convertPos(unmaskedLen: Int, pos: Int, isDeletion: Boolean = false): Int =
        when { // order of when cases is important
            isEmpty -> pos
            (pos < 0) -> 0
            (pos >= maskPositions.unmaskPos.size) -> unmaskedLen
            isDeletion -> min(unmaskedLen - 1, maskPositions.unmaskDelPos[pos])
            else -> min(unmaskedLen, maskPositions.unmaskPos[pos])
        }

    private fun buildMask(fmtString: String): List<MaskCharacter> {
        val fabric = MaskCharacterFabric()
        return fmtString.toCharArray().mapTo(ArrayList()) { fabric.buildCharacter(it) }
    }
}
