package com.example.githubapp.Repository

import androidx.lifecycle.LiveData
import com.example.githubapp.Database.UserDao
import com.example.githubapp.DataClass.UserEntity

class UserRepository(private val userDao: UserDao) {

    val allUsers: LiveData<List<UserEntity>> = userDao.getAllUsers()

    suspend fun insert(user: UserEntity) {
        userDao.insert(user)
    }

    suspend fun delete(user: UserEntity) {
        userDao.delete(user)
    }

    fun isFavorite(userId: Int): LiveData<Boolean> {
        return userDao.isFavorite(userId)
    }
}



