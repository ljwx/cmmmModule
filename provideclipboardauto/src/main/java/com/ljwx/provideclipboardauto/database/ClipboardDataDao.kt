package com.ljwx.provideclipboardauto.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ClipboardDataDao {

    @Query("select * from clipboard_data order by time desc")
    fun getAll(): List<ClipboardDataEntity>

    @Query("select * from clipboard_data where url = :url limit 1")
    fun findItem(url: String): ClipboardDataEntity?

    @Query("select * from clipboard_data order by time asc limit 1")
    fun getLast():ClipboardDataEntity?

    @Update
    fun updateItem(item: ClipboardDataEntity)

    @Insert
    fun addItem(item: ClipboardDataEntity)

    @Delete()
    fun deleteItem(item: ClipboardDataEntity)

    @Query("delete from clipboard_data")
    fun clear()
}