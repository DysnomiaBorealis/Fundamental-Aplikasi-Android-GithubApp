package com.example.githubapp.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubapp.DataClass.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity): Long

    @Delete
    fun delete(user: UserEntity): Int

    @Query("SELECT * FROM favorite_users")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Query("SELECT COUNT(*) > 0 FROM favorite_users WHERE id = :userId")
    fun isFavorite(userId: Int): LiveData<Boolean>
}

