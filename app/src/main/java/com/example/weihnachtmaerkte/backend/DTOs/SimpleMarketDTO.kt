package com.example.weihnachtmaerkte.backend.DTOs

data class SimpleMarketDTO(val id: Long,
                           var name: String,
                           var coordinates: DoubleArray,
                           var averageRating: SimpleRatingDTO,
                           var image: String
) {
}