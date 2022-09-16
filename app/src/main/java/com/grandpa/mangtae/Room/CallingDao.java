package com.grandpa.mangtae.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Dao
public interface CallingDao {
    @Query("SELECT * FROM CallingData ORDER BY writer DESC")
    List<CallingData> getAll();

    @Query("SELECT * FROM CallingData WHERE id IN (:callingDataIds)")
    List<CallingData> loadAllByIds(int[] callingDataIds);

    @Query("SELECT * FROM CallingData WHERE id = :id")
    CallingData loadById(int id);

    @TypeConverters({Converters.class})
    @Query("UPDATE CallingData SET name = :name, content = :content, updateDate = :updateDate, category = :category WHERE id = :id")
    void update(int id, String name, String content, Date updateDate, String category);

    @Query("Delete From CallingData WHERE id = :id")
    void delete(int id);


    @Insert
    void insertAll(CallingData callingData);

    @Delete
    void delete(CallingData callingData);
}

