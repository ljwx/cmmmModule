package com.sisensing.common.entity.Device

import androidx.room.Entity
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity

@Entity(tableName = "RemoteGlucoseEntity")
class DeviceGlucoseEntity: BloodGlucoseEntity()