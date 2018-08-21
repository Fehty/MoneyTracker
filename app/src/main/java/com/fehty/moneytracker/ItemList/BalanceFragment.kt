package com.fehty.moneytracker.ItemList

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fehty.moneytracker.R

class BalanceFragment : Fragment() {

    private val TYPE_KEY = "type"
    val TYPE_BALANCES = 3

    fun createItemsFragment(type: Int): BalanceFragment {
        val balanceFragment = BalanceFragment()
        val bundle = Bundle()
        bundle.putInt(balanceFragment.TYPE_KEY, type)
        balanceFragment.arguments = bundle
        return balanceFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        bundle.getInt(TYPE_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_balance, container, false)
    }

}
