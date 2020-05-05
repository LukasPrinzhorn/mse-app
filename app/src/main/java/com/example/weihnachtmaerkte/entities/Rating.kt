package com.example.weihnachtmaerkte.entities

data class Rating(val id: Long,
                  var ambience: Float,
                  var food: Float,
                  var drinks: Float,
                  var crowding: Float,
                  var family: Float,
                  var title: String,
                  var text: String,
                  var images: List<String>

) {
}