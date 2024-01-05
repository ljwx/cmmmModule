package com.sisensing.common.constants;

import com.sisensing.base.BaseViewModel;
import com.sisensing.common.base.Model;
import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.database.RoomResponseListener;
import com.sisensing.common.database.RoomTask;
import com.sisensing.common.user.UserInfoUtils;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.constants
 * Author: f.deng
 * CreateDate: 2021/7/27 17:24
 * Description:
 */
public abstract class GeneralModel extends Model {

    public GeneralModel() {
    }

    public GeneralModel(BaseViewModel viewModel) {
        super(viewModel);
    }

    /**
     * 删除当前deviceName数据
     *
     * @param deviceName
     * @param listener
     */
    public void deleteBloodGlucoseByBle(String deviceName, RoomResponseListener<Integer> listener) {
        RoomTask.singleTask(AppDatabase.getInstance().getBloodEntityDao().deleteBloodGlucoseByBle(UserInfoUtils.getUserId(), deviceName), listener);
    }




}
