package com.example.githubapp.DataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_users")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val username: String,
    val avatarUrl: String,
)

