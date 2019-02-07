package com.genaku.maskededittext

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * Android EditText with mask
 *
 *  @author Gena Kuchergin
 */
class MaskedEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    private val maskedHolder = MaskedStringHolder("")

    private val maskedInputFilter = MaskedInputFilter(
        maskedStringHolder = maskedHolder,
        setSelection = this::setNewPos,
        setText = this::updateText
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
        maskedHolder.formatted.toString()

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

    private fun updateText(s: CharSequence) {
        val text = maskedHolder.fullFormatted
        val coloredText = SpannableString(text)
        coloredText.setSpan(
            ForegroundColorSpan(currentHintTextColor),
            s.length,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setText(coloredText)
    }

}