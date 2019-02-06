package com.genaku.maskededittext

class MaskedFormatter(fmtString: String) {

    private var mask: Mask = Mask(fmtString)

    val maskString: String
        get() = mask.formatString

    val maskLength: Int
        get() = mask.size

    fun setMask(fmtString: String) {
        mask = Mask(fmtString)
    }

    fun formatString(value: String): CharSequence =
        mask.getFormattedString(value)

    fun maskedString(value: String): String =
        formatString(value).toString()

    fun rawString(value: String): String =
        mask.getFormattedString(value).buildRawString(value)
}
