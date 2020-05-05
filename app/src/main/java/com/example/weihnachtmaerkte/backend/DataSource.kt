package com.example.weihnachtmaerkte.backend

import com.example.weihnachtmaerkte.backend.DTOs.DetailedMarketDTO
import com.example.weihnachtmaerkte.backend.DTOs.SimpleRatingDTO

class DataSource {

    companion object {

        fun listOfMarkets(): List<DetailedMarketDTO> {
            val list = ArrayList<DetailedMarketDTO>()
            var simpleRatingDTO: SimpleRatingDTO =
                    SimpleRatingDTO(4.8f,
                            3.7f,
                            3.5f,
                            2.3f,
                            1.8f)

            list.add(
                    DetailedMarketDTO(100,
                            "Wiener Weihnachtstraum",
                            "Rathausplatz 1, 1010 Wien",
                            "27.11.2020 - 06.01.2021",
                            "10 - 22Uhr",
                            "http://www.wiener-rathausplatz.at/christkindlmarkt.html",
                            simpleRatingDTO,
                            listOf(1),
                            "@drawable/wiener_weihnachtstraum"
                    )
            )

            return list
        }
    }
}