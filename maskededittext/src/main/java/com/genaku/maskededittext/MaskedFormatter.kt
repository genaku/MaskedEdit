package com.genaku.maskededittext

class MaskedFormatter(fmtString: String) {

    var mMask: Mask = Mask(fmtString)

    val maskString: String
        get() = mMask.formatString

    val maskLength: Int
        get() = mMask.size

    fun setMask(fmtString: String) {
        mMask = Mask(fmtString)
    }

    fun formatString(value: String): CharSequence =
        mMask.getFormattedString(value)

    fun maskedString(value: String): String =
        formatString(value).toString()

    fun rawString(value: String): String =
        mMask.getFormattedString(value).buildRawString(value)
}
