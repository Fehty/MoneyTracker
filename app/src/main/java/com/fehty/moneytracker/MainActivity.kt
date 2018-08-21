package com.fehty.moneytracker

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import com.fehty.moneytracker.Adapter.MainPagesAdapter
import com.fehty.moneytracker.ItemAdd.ItemAddActivity
import com.fehty.moneytracker.ItemList.App
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private val itemAddActivity = ItemAddActivity()
    private var type: String = "type"
    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabLayout.setupWithViewPager(pager)
        setSupportActionBar(toolBar)
        pager.addOnPageChangeListener(this)
        addItem()
    }

    override fun onResume() {
        super.onResume()
        when {
            (application as App).isAuthorized() -> initTabs()
            else -> {
                var intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun initTabs() {
        pager.adapter = MainPagesAdapter(supportFragmentManager, this)
    }

    override fun onSupportActionModeStarted(mode: ActionMode) {
        super.onSupportActionModeStarted(mode)
        floatingActionButton.hide()
        actionMode = mode
    }

    override fun onSupportActionModeFinished(mode: ActionMode) {
        super.onSupportActionModeFinished(mode)
        floatingActionButton.show()
    }

    private fun addItem() {
        floatingActionButton.setOnClickListener({
            val currentPage = pager.currentItem
            when (currentPage) {
                0 -> type = "expense"
                1 -> type = "income"
            }
            var intent = Intent(this, ItemAddActivity::class.java)
            intent.putExtra(itemAddActivity.TYPE_KEY, type)
            startActivityForResult(intent, 1)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) fragment.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPageScrollStateChanged(state: Int) {
        when (state) {
            ViewPager.SCROLL_STATE_IDLE -> floatingActionButton?.isEnabled = true
            ViewPager.SCROLL_STATE_DRAGGING -> {
                when {
                    actionMode != null -> actionMode?.finish()
                }
                floatingActionButton?.isEnabled = false
            }
            ViewPager.SCROLL_STATE_SETTLING -> {
                when {
                    actionMode != null -> actionMode?.finish()
                }
                floatingActionButton?.isEnabled = false
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        when (position) {
            0 -> floatingActionButton?.show()
            1 -> floatingActionButton?.show()
            2 -> floatingActionButton?.hide()
        }
    }

    override fun onPageSelected(position: Int) = Unit
}


