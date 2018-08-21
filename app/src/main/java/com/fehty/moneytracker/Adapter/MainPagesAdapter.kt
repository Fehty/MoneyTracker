package com.fehty.moneytracker.Adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.fehty.moneytracker.ItemList.BalanceFragment
import com.fehty.moneytracker.ItemList.ItemsFragment
import com.fehty.moneytracker.R

class MainPagesAdapter(fm: FragmentManager?, context: Context) : FragmentPagerAdapter(fm) {

    private var titles: Array<String> = context.resources.getStringArray(R.array.tab_title)
    val PAGE_EXPENSES = 0
    val PAGE_INCOMES = 1
    val PAGE_BALANCE = 2

    override fun getItem(position: Int): Fragment? {
        when (position) {
            PAGE_EXPENSES -> {
                val itemsFragment = ItemsFragment()
                return itemsFragment.createItemsFragment(itemsFragment.TYPE_EXPENSES)
            }
            PAGE_INCOMES -> {
                val itemsFragment = ItemsFragment()
                return itemsFragment.createItemsFragment(itemsFragment.TYPE_INCOMES)
            }
            PAGE_BALANCE -> {
                val balanceFragment = BalanceFragment()
                return balanceFragment.createItemsFragment(balanceFragment.TYPE_BALANCES)
            }
        }
        return null
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}

