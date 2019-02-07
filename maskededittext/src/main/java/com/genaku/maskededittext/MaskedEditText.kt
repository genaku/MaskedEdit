package com.genaku.maskededittext

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MaskedEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    private val maskedHolder = MaskedHolder("")

    private val maskedInputFilter = MaskedInputFilter(
        maskedHolder = maskedHolder,
        setSelection = { pos -> setNewPos(pos) },
        setText = { s -> this.setText(s) }
    )

    init {
        initByAttr(context, attrs)
    }

    var mask: String
        get() = maskedHolder.getMask()
        set(value) {
            maskedHolder.setMask(value)
            maskedInputFilter.refreshText()
        }

    fun getMaskedString(): String =
        maskedHolder.formatted

    fun getUnmaskedString(): String =
        maskedHolder.unmasked

    fun setUnmaskedString(value: String) {
        maskedHolder.unmasked = value
        maskedInputFilter.refreshText()
    }

    private fun initByAttr(context: Context, attrs: AttributeSet) {
        setUnmaskedString(getNotNullText())

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaskedEditText)

        if (typedArray.hasValue(R.styleable.MaskedEditText_mask)) {
            val maskStr = typedArray.getString(R.styleable.MaskedEditText_mask)
            mask = maskStr ?: ""
        }
        typedArray.recycle()

        filters = arrayOf(maskedInputFilter)
    }

    private fun getNotNullText(): String {
        text ?: return ""
        return text.toString()
    }

    private fun setNewPos(pos: Int) {
        val lastPos = getNotNullText().length
        setSelection(Math.max(0, Math.min(pos, lastPos)))
    }
}