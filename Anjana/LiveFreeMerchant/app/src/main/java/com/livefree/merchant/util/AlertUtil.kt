package com.livefree.merchant.util

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.TextView
import android.widget.Toast

object AlertUtil {

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun showToast(context: Context, msg: String, duration: Int) {
        Toast.makeText(context, msg, duration).show()
    }

    fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }

    fun showSnackBar(view: View, msg: String, duration: Int) {
        Snackbar.make(view, msg, duration).show()
    }

    fun showIndefiniteSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE).show()
    }

    fun showActionSnackBar(view: View, msg: String, action: String, listener: View.OnClickListener) {
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE).setAction(action, listener)
        val textView = snackbar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        //        textView.setMaxLines(3);
        snackbar.show()
    }

    fun showAlertDialog(context: Context, title: String, msg: String, actionName: String) {
        val alert = AlertDialog.Builder(context)
        alert.setTitle(title)
        alert.setMessage(msg)
        alert.setCancelable(true)
        alert.setPositiveButton(actionName) { dialog, which -> dialog.dismiss() }
        alert.show()
    }

    fun showActionAlertDialog(context: Context, title: String, msg: String,
                              positiveAction: String, negativeAction: String,
                              listener: DialogInterface.OnClickListener) {
        val alert = AlertDialog.Builder(context)
        alert.setTitle(title)
        alert.setMessage(msg)
        alert.setCancelable(true)
        alert.setPositiveButton(positiveAction, listener)
        alert.setNegativeButton(negativeAction) { dialog, which -> dialog.dismiss() }
        alert.show()
    }

    fun showActionNotCancelableAlertDialog(context: Context, title: String, msg: String,
                                           positiveAction: String, listener: DialogInterface.OnClickListener) {
        val alert = AlertDialog.Builder(context)
        alert.setTitle(title)
        alert.setMessage(msg)
        alert.setCancelable(true)
        alert.setPositiveButton(positiveAction, listener)
        alert.show()
    }


}