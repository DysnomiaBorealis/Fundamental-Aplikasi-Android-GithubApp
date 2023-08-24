package com.example.githubapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubapp.Repository.GithubRepository

class ViewModelFactory (private val repository: GithubRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GithubViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}