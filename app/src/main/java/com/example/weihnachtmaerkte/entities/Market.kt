package com.example.weihnachtmaerkte.entities

data class Market(val id : Long,
                  var name: String,
                  var address: String,
                  var coordinates: DoubleArray,
                  var dates: String,
                  var openingHours: String,
                  var weblink: String,
                  var ratings: List<Rating>,
                  var image: String
) {
}