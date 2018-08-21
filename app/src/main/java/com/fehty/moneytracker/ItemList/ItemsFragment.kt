package com.fehty.moneytracker.ItemList

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.fehty.moneytracker.Adapter.DataAdapter
import com.fehty.moneytracker.Adapter.ItemsAdapterListener
import com.fehty.moneytracker.Api.Api
import com.fehty.moneytracker.Data.AddItemResult
import com.fehty.moneytracker.Data.DataList
import com.fehty.moneytracker.DialogAlert.ConfirmationDialog
import com.fehty.moneytracker.R
import kotlinx.android.synthetic.main.fragment_items.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemsFragment : Fragment() {

    private var dataAdapter = DataAdapter()
    private var api: Api? = null
    private var app: App? = null
    private var type: String = "type"
    private var actionMode: ActionMode? = null
    private val TYPE_KEY = "type"
    private val ADD_ITEM_REQUEST_CODE = 1
    val TYPE_EXPENSES = "expense"
    val TYPE_INCOMES = "income"

    fun createItemsFragment(type: String): ItemsFragment {
        val itemsFragment = ItemsFragment()
        val bundle = Bundle()
        bundle.putString(TYPE_KEY, type)
        itemsFragment.arguments = bundle
        dataAdapter.notifyDataSetChanged()
        actionMode?.finish()
        return itemsFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle = arguments
        type = bundle.getString(TYPE_KEY, type)
        app = activity.application as App
        api = app!!.getApi()
    }

    private fun addItem(item: DataList) {
        var call: Call<AddItemResult> = api!!.addItem(item.price, item.name, item.type, app?.getAuthToken()!!)
        call.enqueue(object : Callback<AddItemResult> {
            override fun onResponse(call: Call<AddItemResult>?, response: Response<AddItemResult>?) {
                var result: AddItemResult = response?.body()!!
                if (result.status == "success") {
                    item.id = result.id
                    dataAdapter.addItem(item)
                }
            }
            override fun onFailure(call: Call<AddItemResult>?, t: Throwable?) = Unit
        })
    }

    fun removeItemsApi() {
        (dataAdapter.getSelectedItems().size - 1 downTo 0)
                .map { dataAdapter.remove(dataAdapter.getSelectedItems()[it]) }
                .map { api!!.removeItem(it.id, app?.getAuthToken()!!) }
                .forEach {
                    it.enqueue(object : Callback<AddItemResult> {
                        override fun onResponse(call: Call<AddItemResult>?, response: Response<AddItemResult>?) {
                            var result = response?.body()!!
                            if (result.status == "success") {
                                dataAdapter.notifyDataSetChanged()
                            }
                        }
                        override fun onFailure(call: Call<AddItemResult>?, t: Throwable?) = Unit
                    })
                }
        actionMode?.finish()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
        }
        dataAdapter.setListener(AdapterListener())
        loadItems()
        refreshPage()
    }

    private fun refreshPage() = with(swipeRefreshLayout) {
        setColorSchemeColors(resources.getColor(R.color.colorPrimary), resources.getColor(R.color.colorAccent), resources.getColor(R.color.colorPrimaryDark))
        setOnRefreshListener({
            loadItems()
        })
    }

    private fun loadItems() {
        val call: Call<MutableList<DataList>> = api?.getItems(type, app!!.getAuthToken()!!)!!
        call.enqueue(object : Callback<MutableList<DataList>> {
            override fun onResponse(call: Call<MutableList<DataList>>?, response: Response<MutableList<DataList>>?) {
                dataAdapter.setData(response?.body()!!)
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<MutableList<DataList>>?, t: Throwable?) {
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val item = data!!.getParcelableExtra<DataList>("item")
            if (item.type == type) {
                addItem(item)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    inner class AdapterListener : ItemsAdapterListener {

        override fun onItemClick(item: DataList, position: Int) {
            when {
                isInActionMode() -> {
                    toggleSelection(position)
                    Log.i(TAG, item.id.toString())

                }
            }
        }

        override fun onItemLongClick(item: DataList, position: Int) {
            if (isInActionMode()) {
                return
            }
            actionMode = (activity as AppCompatActivity).startSupportActionMode(actionModeCallBack)
            toggleSelection(position)
        }

        private fun isInActionMode(): Boolean {
            return actionMode != null
        }

        private fun toggleSelection(position: Int) {
            dataAdapter.toggleSelection(position)
        }
    }

    var actionModeCallBack = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val inflater = MenuInflater(context)
            inflater.inflate(R.menu.items_menu, menu)
            return true
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.remove -> ConfirmationDialog(this@ItemsFragment).show(fragmentManager, "")
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            dataAdapter.clearSelections()
            actionMode = null
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
    }
}

