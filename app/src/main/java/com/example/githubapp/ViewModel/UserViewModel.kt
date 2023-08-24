package com.example.githubapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.githubapp.Database.AppDatabase
import com.example.githubapp.Repository.UserRepository
import com.example.githubapp.DataClass.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun addToFavorites(user: UserEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }

    fun removeFromFavorites(user: UserEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(user)
    }

    fun isFavorite(userId: Int): LiveData<Boolean> {
        return repository.isFavorite(userId)
    }

    val allUsers: LiveData<List<UserEntity>> = repository.allUsers
}
