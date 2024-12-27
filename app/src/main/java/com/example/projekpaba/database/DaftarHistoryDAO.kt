package com.example.projekpaba.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface DaftarHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(daftar: DaftarHistory)

    @Query("UPDATE DaftarHistory SET tanggal=:isi_tanggal,item=:isi_item, jumlah=:isi_jumlah WHERE id=:pilihid")
    fun update(isi_tanggal: String, isi_item: String, isi_jumlah: String, pilihid: Int)

    @Delete
    fun delete(daftar: DaftarHistory)

    @Query("SELECT * FROM DaftarHistory ORDER BY id asc")
    fun selectAll(): MutableList<DaftarHistory>

    @Query("SELECT * FROM DaftarHistory WHERE id=:isi_id")
    suspend fun getItem(isi_id: Int):DaftarHistory
}