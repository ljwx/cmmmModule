package com.ljwx.baseactivity.fast

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ljwx.baseactivity.BaseMVVMActivity
import com.ljwx.baseapp.BaseViewModel

open abstract class FastTabLayoutActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel>(@LayoutRes layoutResID: Int) :
    BaseMVVMActivity<Binding, ViewModel>(layoutResID) {

    protected lateinit var mTabLayout: TabLayout

    protected lateinit var mViewPager: ViewPager2

    /**
     * fragment集合
     */
    private val mTabFragments = LinkedHashMap<TabLayout.Tab, Fragment>()


    /**
     * 添加tab和对应的fragment
     */
    fun addTabFragment(tab: TabLayout.Tab, fragment: Fragment) {
        mTabFragments[tab] = fragment
    }

    /**
     * 添加tab名称和对应的fragment
     */
    fun addTabFragment(tabName: String, fragment: Fragment) {
        val tab = mTabLayout.newTab()
        tab.text = tabName
        mTabFragments[tab] = fragment
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
        pageLimit: Int = 1,
    ) {
        mViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return mTabFragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return mTabFragments.values.toList()[position]
            }
        }
        mViewPager.isUserInputEnabled = canScroll
        mViewPager.offscreenPageLimit = pageLimit
        TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
            val tabItem = mTabFragments.keys.toList()[position]
            if (tabItem.customView != null) {
                tab.customView = tabItem.customView
            } else if (!tabItem.text.isNullOrBlank()) {
                tab.text = tabItem.text
            }
        }.attach()
    }

}