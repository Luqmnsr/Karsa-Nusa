package com.example.karsanusa.view.customview

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

class HyperTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        // Pastikan LinkMovementMethod diatur agar ClickableSpan dapat diklik
        movementMethod = LinkMovementMethod.getInstance()
    }

    override fun performClick(): Boolean {
        // Panggil implementasi default performClick untuk aksesibilitas
        super.performClick()
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Tangani touch event agar ClickableSpan bekerja dengan benar
        val handled = super.onTouchEvent(event)
        if (event?.action == MotionEvent.ACTION_UP) {
            performClick() // Pastikan performClick dipanggil saat ACTION_UP
        }
        return handled
    }
}