package com.fehty.moneytracker.Adapter

import com.fehty.moneytracker.Data.DataList

interface ItemsAdapterListener {
    fun onItemClick(item: DataList, position: Int)
    fun onItemLongClick(item: DataList, position: Int)
}
