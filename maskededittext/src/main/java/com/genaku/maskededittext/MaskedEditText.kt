package com.genaku.maskededittext

import android.content.Context
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import org.mym.plog.PLog

/**
 * Android EditText with mask
 *
 *  @author Gena Kuchergin
 */
class MaskedEditText(context: Context, attrs: AttributeSet) :
        AppCompatEditText(context, attrs) {

    private var emptyMaskVisible = true

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
            PLog.d("set mask [$value]")
            filters.forEach {
                PLog.d("filter: ${it.javaClass.simpleName}")
                if (it is MaskedInputFilter) {
                    PLog.d("mask: ${it.maskedStringHolder.getMask()}")
                }
            }
        }

    fun getMaskedString(): String =
            maskedHolder.formatted.toString()

    fun getUnmaskedString(): String =
            maskedHolder.unmasked

    fun setUnmaskedString(value: String) {
        maskedHolder.unmasked = value
        maskedInputFilter.refreshText()
    }

    fun clear() {
        setUnmaskedString("")
    }

    fun setEmptyMaskVisible(visible: Boolean) {
        emptyMaskVisible = visible
        maskedInputFilter.refreshText()
    }

    fun isValid(): Boolean {
        return (maskedHolder.formatted.length == mask.length)
    }

    private fun initByAttr(context: Context, attrs: AttributeSet) {
        setUnmaskedString(getNotNullText())

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaskedEditText)

        if (typedArray.hasValue(R.styleable.MaskedEditText_mask)) {
            val maskStr = typedArray.getString(R.styleable.MaskedEditText_mask)
            mask = maskStr ?: ""
        }
        typedArray.recycle()

        PLog.d("add filters")
        super.setFilters(arrayOf(maskedInputFilter))
    }

    override fun setFilters(filters: Array<out InputFilter>?) {
        PLog.d("set filters ${filters?.size}")
        val allFilters = ArrayList<InputFilter>()
        if (maskedInputFilter != null) {
            allFilters.add(maskedInputFilter)
        }
        if (filters != null) {
            allFilters.addAll(filters)
        }
        val arr = allFilters.toTypedArray()
        super.setFilters(arr)
    }

    private fun getNotNullText(): String {
        text ?: return ""
        return text.toString()
    }

    private fun setNewPos(pos: Int) {
        val lastPos = getNotNullText().length
        setSelection(Math.max(0, Math.min(pos, lastPos)))
    }

    private fun updateText(input: CharSequence) {
        val text = if (input.isEmpty() && !emptyMaskVisible) "" else maskedHolder.fullFormatted
        val coloredText = SpannableString(text)
        coloredText.setSpan(
                ForegroundColorSpan(currentHintTextColor),
                input.length,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        PLog.d("set text [$text]")
        setText(coloredText)
    }

}