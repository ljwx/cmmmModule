package com.ljwx.baseactivity.fast

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ljwx.baseactivity.BaseMVVMActivity
import com.ljwx.baseapp.vm.BaseViewModel

open abstract class QuickTabLayoutActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel<*>>(
    @LayoutRes layoutResID: Int
) :
    BaseMVVMActivity<Binding, ViewModel>(layoutResID) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachTabLayoutViewPager()
    }

    /**
     * fragment集合
     */
    private val mTabFragments = LinkedHashMap<TabLayout.Tab, Fragment>()


    /**
     * 添加tab和对应的fragment
     */
    fun addTabFragment(tab: TabLayout.Tab, fragment: Fragment?, notify: Boolean = false) {
        if (fragment == null) {
            return
        }
        mTabFragments[tab] = fragment
        if (notify) {
            getViewPager2().adapter?.notifyDataSetChanged()
        }
    }

    /**
     * 添加tab名称和对应的fragment
     */
    fun addTabFragment(tabName: String, fragment: Fragment?, notify: Boolean = true) {
        if (fragment == null) {
            return
        }
        val tab = getTabLayout().newTab()
        tab.text = tabName
        mTabFragments[tab] = fragment
        if (notify) {
            getViewPager2().adapter?.notifyDataSetChanged()
        }
    }

    /**
     * 清空fragment
     */
    fun cleanFragment() {
        mTabFragments.clear()
    }

    /**
     * 初始化viewpager和tabLayout
     *
     * @param canScroll 是否滑动切换
     * @param pageLimit 缓存数量
     */
    fun attachTabLayoutViewPager(
        canScroll: Boolean = false,
        pageLimit: Int = 0,
    ) {
        getViewPager2().adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return mTabFragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return mTabFragments.values.toList()[position]
            }
        }
        getViewPager2().isUserInputEnabled = canScroll
        if (pageLimit > 0) {
            getViewPager2().offscreenPageLimit = pageLimit
        }
        TabLayoutMediator(getTabLayout(), getViewPager2()) { tab, position ->
            val tabItem = mTabFragments.keys.toList()[position]
            if (tabItem.customView != null) {
                tab.customView = tabItem.customView
            } else if (!tabItem.text.isNullOrBlank()) {
                tab.text = tabItem.text
            }
        }.attach()
    }

    abstract fun getTabLayout(): TabLayout

    abstract fun getViewPager2(): ViewPager2

    override fun onDestroy() {
        mTabFragments.clear()
        super.onDestroy()
    }

}