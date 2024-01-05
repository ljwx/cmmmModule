package com.sisensing.common.fragment

import android.os.Bundle
import android.view.View
import com.ljwx.baseapp.constant.BaseConstBundleKey
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.basefragment.BaseBindingFragment
import com.sisensing.common.R
import com.sisensing.common.constants.ConstTypeValue
import com.sisensing.common.databinding.FragmentMonitoringHelpBinding
import com.sisensing.common.router.RouterActivityPath

class FragmentMonitoringHelp :
    BaseBindingFragment<FragmentMonitoringHelpBinding>(R.layout.fragment_monitoring_help) {

    private val argumentFromType by lazy { arguments?.getInt(BaseConstBundleKey.FROM_TYPE, -10) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (argumentFromType == ConstTypeValue.HOME_MONITORING_INIT) {
            mBinding.help.root.visibleGone(true)
            mBinding.newSensor.root.visibleGone(false)
        } else if (argumentFromType == ConstTypeValue.HOME_MONITORING_EXPIRE) {
            mBinding.help.root.visibleGone(false)
            mBinding.newSensor.root.visibleGone(true)
        }

        mBinding.help.howToApply.singleClick {
            routerTo(RouterActivityPath.PersonalCenter.HELP_APPLY_SENSOR).withFromType(
                ConstTypeValue.HELP_APPLY
            ).start()
        }
        mBinding.help.howToConnect.singleClick {
            routerTo(RouterActivityPath.PersonalCenter.HELP_APPLY_SENSOR).withFromType(
                ConstTypeValue.HELP_CONNECT
            ).start()
        }
        mBinding.help.startConnecting.singleClick {
            //连接传感器-佩戴引导页
            routerTo(RouterActivityPath.Scan.PAGER_SCAN).start()
        }

        mBinding.newSensor.connectADevice.singleClick {
            routerTo(RouterActivityPath.Scan.PAGER_SCAN).start()
        }
        mBinding.newSensor.help.singleClick {
            routerTo(RouterActivityPath.PersonalCenter.HELP_MAIN).start()
        }
    }

}