/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sisensing.common.share;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.user.UserInfoUtils;

/**
 * TODO tip 1：event-ViewModel 的职责仅限于在 "跨页面通信" 的场景下，承担 "唯一可信源"，
 * 所有跨页面的 "状态同步请求" 都交由该可信源在内部决策和处理，并统一分发给所有订阅者页面。
 */
public class SharedViewModel extends ViewModel {


    private LiveData<DeviceEntity> mDeviceEntityLiveData;


    public LiveData<DeviceEntity> getDeviceEntityLiveData() {

        if (mDeviceEntityLiveData == null) {
            mDeviceEntityLiveData = AppDatabase.getInstance().getDeviceDao().findLatestUseDevice(UserInfoUtils.getUserId());
        }

        return mDeviceEntityLiveData;
    }


    public void resetDeviceLiveData() {
        mDeviceEntityLiveData = null;
    }
}
