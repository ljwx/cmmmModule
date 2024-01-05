package com.sisensing.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.basefragment.BaseBindingFragment
import com.sisensing.common.R
import com.sisensing.common.constants.ConstTypeValue
import com.sisensing.common.databinding.FragmentHelpApplySensorBinding
import com.sisensing.common.databinding.HelpStepViewBinding
import com.sisensing.common.entity.HelpStepData
import com.sisensing.common.router.RouterActivityPath
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator

class FragmentApplySensor :
    BaseBindingFragment<FragmentHelpApplySensorBinding>(R.layout.fragment_help_apply_sensor) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arrayOf(
            HelpStepData(
                resources.getDrawable(R.mipmap.apply_sensor_step_1),
                getString(R.string.step_1),
                getString(R.string.apply_site_select_the_back_of_your_upper_arm),
                getString(R.string.note_avoid_areas_with_wounds_or_insulin_injection_site)
            ),
            HelpStepData(
                resources.getDrawable(R.mipmap.apply_sensor_step_2),
                getString(R.string.step_2),
                getString(R.string.clean_the_back_of_your_upper_arm_with_alcohol_wipe_and_wait_for_the_skin_to_dry),
                ""
            ),
            HelpStepData(
                resources.getDrawable(R.mipmap.apply_sensor_step_3),
                getString(R.string.step_3),
                getString(R.string.open_the_sensor_pack_and_sensor_applicator_and_line_up_the_two_components_as_shown),
                ""
            ),
            HelpStepData(
                resources.getDrawable(R.mipmap.apply_sensor_step_4),
                getString(R.string.step_4),
                getString(R.string.press_firmly_down_on_the_sensor_applicator_until_it_comes_to_a_stop_lift_the_sensor_applicator_out_of_the_sensor_pack_and_make_sure),
                getString(R.string.note_do_not_press_the_button_on_the_top_and_do_not_touch_the_inside_of_the_sensor)
            ),
            HelpStepData(
                resources.getDrawable(R.mipmap.apply_sensor_step_5),
                getString(R.string.step_5),
                getString(R.string.place_the_sensor_applicator_over_the_prepared_site_and_push_down_firmly),
                getString(R.string.precaution_do_not_push_down_on_the_sensor_applicator_until)
            ),
            HelpStepData(
                resources.getDrawable(R.mipmap.apply_sensor_step_6),
                getString(R.string.step_6),
                getString(R.string.gently_pull_the_sensor_applicator_away_from_your_body),
                ""
            ),
            HelpStepData(
                resources.getDrawable(R.mipmap.apply_sensor_step_7),
                getString(R.string.step_7),
                getString(R.string.make_sure_the_sensor_is_placed_as_shown_and_gently_press_the_edge_of_the),
                ""
            )
        )

        mBinding.viewpager.adapter = object : PagerAdapter() {
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
                if (position == data.size-1) {
                    binding.howTo.visibleGone(true)
                }
                binding.howTo.singleClick {
                    routerTo(RouterActivityPath.PersonalCenter.HELP_APPLY_SENSOR)
                        .withFromType(ConstTypeValue.HELP_CONNECT)
                        .start()
                    activity?.finish()
                }
                return binding.root
            }

        }

        val indicator = CircleNavigator(requireContext())
        indicator.circleColor = resources.getColor(R.color.theme_color)
        indicator.circleCount = data.size
        ViewPagerHelper.bind(mBinding.indicator, mBinding.viewpager)
        mBinding.indicator.navigator = indicator

    }

}