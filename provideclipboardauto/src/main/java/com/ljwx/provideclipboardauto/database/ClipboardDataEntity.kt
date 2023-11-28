package com.ljwx.provideclipboardauto.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.TimeUtils

@Entity(tableName = "clipboard_data", indices = [Index("url")])
class ClipboardDataEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var content: String = ""

    var category: String = ""

    var url: String = ""

    var title: String = ""

    var time: Long = 0

    var placeholder: String = ""

    var tag = ""

    var other = false

    val timeShow:String
        get() {
            return TimeUtils.millis2String(time, "MM-dd HH:mm")
        }

}