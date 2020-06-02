package com.example.weihnachtmaerkte.entities

data class User(val id: String,
                var username: String,
                var friends: List<User>
)