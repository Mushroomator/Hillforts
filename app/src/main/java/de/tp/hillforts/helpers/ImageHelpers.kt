package de.tp.hillforts.helpers

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import de.tp.hillforts.R

fun showImagePicker(parent: Activity, id: Int) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_OPEN_DOCUMENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    val chooser = Intent.createChooser(intent, R.string.title_hillfort_details_view.toString())
    parent.startActivityForResult(chooser, id)
}