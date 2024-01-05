package com.sisensing.common.database;

import androidx.annotation.Nullable;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.database
 * @Author: f.deng
 * @CreateDate: 2021/3/9 17:03
 * @Description:
 */
public interface RoomResponseListener<T> {

    void response(@Nullable T t);

}
