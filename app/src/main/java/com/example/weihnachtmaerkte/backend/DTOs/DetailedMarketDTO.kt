package com.example.weihnachtmaerkte.backend.DTOs

data class DetailedMarketDTO(val id: Long,
                             var name: String,
                             var address: String,
                             var dates: String,
                             var openingHours: String,
                             var weblink: String,
                             var averageRating: SimpleRatingDTO,
                             var ratingIds: List<Long>,
                             var image: String
)