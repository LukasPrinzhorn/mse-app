package com.example.weihnachtmaerkte.backend.DTOs

data class DetailedUserDTO(val id: Long,
                           var username: String,
                           var friends: List<SimpleUserDTO>
)