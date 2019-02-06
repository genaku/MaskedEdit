package com.genaku.maskededittext

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MaskedEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    private var maskedInputFilter = MaskedInputFilter(
        setSelection = { pos -> this.setSelection(pos) },
        setText = { s -> this.setText(s) }
    )

    init {
        initByAttr(context, attrs)
    }

    private fun initByAttr(context: Context, attrs: AttributeSet) {
        maskedInputFilter.setMask("+7(###) ###-##-##")
        filters = arrayOf(maskedInputFilter)
    }

}