package com.example.weihnachtmaerkte

import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating

object FakeMarketsData {

    val markets: ArrayList<Market> = arrayListOf(
            Market(
                    "0",
                    "Wiener Weihnachtstraum",
                    "Rathausplatz 1, 1010 Wien",
                    doubleArrayOf(16.3578594, 48.2104119),
                    "27.11.2020 - 06.01.2020",
                    "10 - 22Uhr",
                    "http://www.wiener-rathausplatz.at/christkindlmarkt.html",
                    arrayListOf("1"),
                    4f,
                    4.1f,
                    2.2f,
                    4.8f,
                    4f,
                    3.8f,
                    2,
                    "https://www.vienna.at/2019/10/OBS_20191024_OBS0027-4-3-98015761182-1576x1182.jpg")
    )

    val ratings: ArrayList<Rating> = arrayListOf(
            Rating("1",
                    1.5f,
                    4f,
                    2.5f,
                    2f,
                    1f,
                    "Ekelhaft",
                    "Überall liegt Müll",
                    "Karen"
            )
    )
}