package com.fehty.moneytracker.ItemAdd

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.fehty.moneytracker.Data.DataList
import com.fehty.moneytracker.R
import kotlinx.android.synthetic.main.activity_item_add.*

class ItemAddActivity : AppCompatActivity() {
    var TYPE_KEY = "type"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_add)
        ItemAddTextWatcher(plainNameOfGood, plainPrice, textRuble, buttonAddItem)
        setSupportActionBar(itemAddToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        itemAdd()
    }

    private fun itemAdd() {
        buttonAddItem.setOnClickListener({
            val type = intent.getStringExtra(TYPE_KEY)
            val nameValue = plainNameOfGood.text.toString()
            val priceValue = plainPrice.text.toString()
            val itemD = DataList(0, nameValue, priceValue, type, "")
            val intent = Intent()
            intent.putExtra("item", itemD)
            setResult(RESULT_OK, intent)
            finish()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when {
            item?.itemId == android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}