package com.fehty.moneytracker.ItemAdd

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ItemAddTextWatcher(nameOfGood: EditText, price: EditText, ruble: TextView, buttonAddItem: Button) {init {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            buttonAddItem.isEnabled = nameOfGood.text.isNotEmpty() && price.text.isNotEmpty()
            if (price.text.isNotEmpty()) ruble.setTextColor(Color.BLACK) else ruble.setTextColor(Color.GRAY)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }
    nameOfGood.addTextChangedListener(textWatcher)
    price.addTextChangedListener(textWatcher)
}
}


