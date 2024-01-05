package com.sisensing.common.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.ljwx.basefragment.BaseBindingFragment
import com.sisensing.common.R
import com.sisensing.common.databinding.FragmentHelpApplySensorBinding
import com.sisensing.common.databinding.HelpStepViewBinding
import com.sisensing.common.entity.HelpStepData
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator

class FragmentConnectSensor :
    BaseBindingFragment<FragmentHelpApplySensorBinding>(R.layout.fragment_help_apply_sensor) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arrayOf(
            HelpStepData(
                resources.getDrawable(R.mipmap.connect_sensor_step_1),
                getString(R.string.step_1),
                getString(R.string.turn_on_the_bluetooth_and_tap_connect_a_device_after_logging_in),
                ""
            ),
            HelpStepData(
                resources.getDrawable(R.mipmap.connect_sensor_step_2),
                getString(R.string.step_2),
                getString(R.string.enter_the_scanning_page_scan_the_sensor_code_on_the_package),
                ""
            ),
            HelpStepData(
                resources.getDrawable(R.mipmap.connect_sensor_step_3),
                getString(R.string.step_3),
                getString(R.string.enter_the_connection_succeeded_page_and_wait_60_minutes_for_initialization),
                ""
            ),
            HelpStepData(
                resources.getDrawable(R.mipmap.connect_sensor_step_4),
                getString(R.string.step_4),
                getString(R.string.after_the_countdown_you_will_see_real_time_glucose_data_uploaded_every_five),
                ""
            ),
        )

        val adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return data.size
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//                super.destroyItem(container, position, `object`)
                container.removeView(`object` as View)
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val binding = DataBindingUtil.inflate<HelpStepViewBinding>(
                    LayoutInflater.from(container.context),
                    R.layout.help_step_view,
                    null,
                    false
                )
                binding.data = data.get(position)
                container.addView(binding.root)
                return binding.root
            }

        }
        mBinding.viewpager.adapter = adapter

        val indicator = CircleNavigator(requireContext())
        indicator.circleColor = resources.getColor(R.color.theme_color)
        indicator.circleCount = data.size
        ViewPagerHelper.bind(mBinding.indicator, mBinding.viewpager)
        mBinding.indicator.navigator = indicator


    }

}