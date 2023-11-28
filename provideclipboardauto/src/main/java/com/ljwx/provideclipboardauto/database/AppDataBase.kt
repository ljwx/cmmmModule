package com.ljwx.provideclipboardauto.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blankj.utilcode.util.Utils

@Database(version = 1, entities = [ClipboardDataEntity::class])
abstract class AppDataBase : RoomDatabase() {

    companion object {

        private var instance: AppDataBase? = null

        fun getInstance(): AppDataBase {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            Utils.getApp().applicationContext,
                            AppDataBase::class.java,
                            "modules_db.db"
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }

    abstract fun clipboardDataDao(): ClipboardDataDao

}