package com.fehty.moneytracker.Adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fehty.moneytracker.Data.DataList
import com.fehty.moneytracker.R
import java.util.*
import kotlin.collections.ArrayList

class DataAdapter : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    private var itemData: MutableList<DataList> = Collections.emptyList()
    private var itemListener: ItemsAdapterListener? = null

    fun setListener(listener: ItemsAdapterListener) {
        this.itemListener = listener
    }

    fun setData(data: MutableList<DataList>) {
        this.itemData = data
        notifyDataSetChanged()
    }

    fun addItem(item: DataList) {
        itemData.add(item)
        notifyDataSetChanged()
    }

    var selections = SparseBooleanArray()

    fun toggleSelection(position: Int) {
        when {
            selections.get(position, false) -> selections.delete(position)
            else -> selections.put(position, true)
        }
        notifyItemChanged(position)
    }

    fun clearSelections() {
        selections.clear()
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<Int> {
        val items = ArrayList<Int>(selections.size())
        (0 until selections.size()).mapTo(items) { selections.keyAt(it) }
        return items
    }

    fun remove(position: Int): DataList {
        val item = itemData.removeAt(position)
        notifyItemRemoved(position)
        return item
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(itemData[position], position, itemListener, selections.get(position, false))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemData.size
    }

    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        fun bindItems(data: DataList, position: Int, itemsAdapterListener: ItemsAdapterListener?, selected: Boolean) {
            val itemNameOfGood: TextView = itemView.findViewById(R.id.textItemNameOfGood)
            val itemPrice: TextView = itemView.findViewById(R.id.textItemPrice)
            itemNameOfGood.text = data.name
            itemPrice.text = data._price
            itemView.setOnClickListener({
                itemsAdapterListener?.onItemClick(data, position)
            })
            itemView.setOnLongClickListener({
                itemsAdapterListener?.onItemLongClick(data, position)
                true
            })
            itemView.isActivated = selected
        }

    }
}
