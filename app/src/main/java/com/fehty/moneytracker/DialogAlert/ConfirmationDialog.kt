package com.fehty.moneytracker.DialogAlert

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.fehty.moneytracker.ItemList.ItemsFragment

@SuppressLint("ValidFragment")
class ConfirmationDialog(private var itemsFragment: ItemsFragment) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
                .setTitle("Процесс удаления")
                .setMessage("Подтвердить")
                .setPositiveButton("Да") { _, _ ->
                    itemsFragment.removeItemsApi()
                }
                .setNegativeButton("Нет") { _, _ -> }
                .create()
    }
}
