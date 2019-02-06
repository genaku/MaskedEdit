package com.genaku.maskededittext

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MaskedEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    private val maskedHolder = MaskedHolder("")

    private val maskedInputFilter = MaskedInputFilter(
        maskedHolder = maskedHolder,
        setSelection = { pos -> this.setSelection(pos) },
        setText = { s -> this.setText(s) }
    )

    init {
        initByAttr(context, attrs)
    }

    var mask: String
        get() = maskedHolder.getMask()
        set(value) = maskedHolder.setMask(value)

    private fun initByAttr(context: Context, attrs: AttributeSet) {
        maskedHolder.setMask("+7(###) ###-##-##")
        filters = arrayOf(maskedInputFilter)
    }
}