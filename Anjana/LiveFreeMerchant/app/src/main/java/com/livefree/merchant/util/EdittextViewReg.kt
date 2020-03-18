package com.livefree.merchant.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.widget.EditText

@SuppressLint("AppCompatCustomView")
class EdittextViewReg : EditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeset: AttributeSet) : super(context, attributeset) {
        init()
    }

    constructor(context: Context, attributeset: AttributeSet, i: Int) : super(context, attributeset, i) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init()
    }

    private fun init() {
        if (!isInEditMode) {
            //setTypeface(Typeface.createFromAsset(getContext().getAssets(), "roboto_regular.ttf"));
            typeface = Typeface.createFromAsset(context.assets, "roboto_regular.ttf")
        }
    }
}
