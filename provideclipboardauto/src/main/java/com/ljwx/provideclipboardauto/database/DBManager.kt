package com.ljwx.provideclipboardauto.database

object DBManager {

    fun clipboardDao(): ClipboardDataDao {
        return AppDataBase.getInstance().clipboardDataDao()
    }

}