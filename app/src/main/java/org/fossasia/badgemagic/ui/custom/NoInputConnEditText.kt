package org.fossasia.badgemagic.ui.custom

import android.content.Context
import android.text.TextUtils
import android.text.Editable
import android.text.method.ArrowKeyMovementMethod
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatTextView

class NoInputConnEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = android.R.attr.editTextStyle) : AppCompatTextView(context, attrs, defStyle) {

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        return null
    }

    override fun getDefaultEditable(): Boolean {
        return true
    }

    override fun getDefaultMovementMethod(): MovementMethod {
        return ArrowKeyMovementMethod.getInstance()
    }

    override fun getText(): Editable {
        return super.getText() as Editable
    }

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, BufferType.EDITABLE)
    }

    override fun setEllipsize(ellipsis: TextUtils.TruncateAt) {
        if (ellipsis == TextUtils.TruncateAt.MARQUEE) {
            throw IllegalArgumentException("EditText cannot use the ellipsize mode " + "TextUtils.TruncateAt.MARQUEE")
        }
        super.setEllipsize(ellipsis)
    }
}