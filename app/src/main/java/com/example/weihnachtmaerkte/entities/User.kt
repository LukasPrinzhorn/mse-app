package com.example.weihnachtmaerkte.entities

data class User(val id: Long,
                var username: String,
                var friends: List<User>
)