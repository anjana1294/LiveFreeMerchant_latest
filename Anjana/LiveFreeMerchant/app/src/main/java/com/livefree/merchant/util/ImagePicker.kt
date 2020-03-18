package com.livefree.merchant.util

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.FragmentManager
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.enums.EPickType


object ImagePicker {
    fun setImage(fragmentManager: FragmentManager, context: Context, returnImageUri: (bitmap: Bitmap) -> Unit) {
        val pickSetup = PickSetup()
            .setTitle("Choose")
            .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
            .setSystemDialog(true)
        PickImageDialog.build(pickSetup).setOnPickResult { result ->
            if (result.error == null)
                returnImageUri(result.bitmap)
            else
                AlertUtil.showToast(context, result.error.message!!)
        }.show(fragmentManager)

    }

}