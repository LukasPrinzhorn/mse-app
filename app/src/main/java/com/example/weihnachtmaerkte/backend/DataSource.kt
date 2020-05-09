package com.example.weihnachtmaerkte.backend

import com.example.weihnachtmaerkte.backend.DTOs.DetailedMarketDTO
import com.example.weihnachtmaerkte.backend.DTOs.SimpleRatingDTO
import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating
import kotlin.collections.ArrayList

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


        fun createMarketsDataSet(): List<Market> {
            val list = ArrayList<Market>()
            var rating1: Rating =
                    Rating(1,
                            4.8f,
                            3.7f,
                            3.5f,
                            2.3f,
                            1.8f,
                            null,
                            null,
                            null)

            var rating2: Rating =
                    Rating(1,
                            3.8f,
                            2.7f,
                            4.5f,
                            1.3f,
                            1.3f,
                            null,
                            null,
                            null)
            var rating3: Rating =
                    Rating(3,
                            1.8f,
                            1.7f,
                            1.5f,
                            0.3f,
                            1.3f,
                            null,
                            null,
                            null)
            var rating4: Rating =
                    Rating(4,
                            4.8f,
                            4.7f,
                            4.5f,
                            2.3f,
                            4.3f,
                            null,
                            null,
                            null)
            var coordinates: DoubleArray = doubleArrayOf(2.0, 3.5)

            list.add(
                    Market(101,
                            "Wiener Weihnachtstraum",
                            "Rathausplatz 1, 1010 Wien",
                            coordinates,
                            "27.11.2020 - 06.01.2021",
                            "10 - 22Uhr",
                            "http://www.wiener-rathausplatz.at/christkindlmarkt.html",
                            listOf(rating1, rating2),
                            //"https://raw.githubusercontent.com/mitchtabian/Blog-Images/master/digital_ocean.png"
                            "@drawable/wiener_weihnachtstraum"
                    )
            )
            list.add(
                    Market(102,
                            "Museumsquartier",
                            "Rathausplatz 1, 1010 Wien",
                            coordinates,
                            "27.11.2020 - 06.01.2021",
                            "10 - 22Uhr",
                            "http://www.wiener-rathausplatz.at/christkindlmarkt.html",
                            listOf(rating2, rating3, rating4),
                            //"https://raw.githubusercontent.com/mitchtabian/Kotlin-RecyclerView-Example/json-data-source/app/src/main/res/drawable/time_to_build_a_kotlin_app.png"
                            "@drawable/museumsquartier"
                    )
            )
            list.add(
                    Market(103,
                            "Adventmarkt Spittelberg",
                            "Rathausplatz 1, 1010 Wien",
                            coordinates,
                            "27.11.2020 - 06.01.2021",
                            "10 - 22Uhr",
                            "http://www.wiener-rathausplatz.at/christkindlmarkt.html",
                            listOf(rating1, rating2, rating3),
                            //"https://raw.githubusercontent.com/mitchtabian/Kotlin-RecyclerView-Example/json-data-source/app/src/main/res/drawable/coding_for_entrepreneurs.png"
                            "@drawable/spittelberg"
                    )
            )
            list.add(
                    Market(104,
                            "Weihnachtsdorf Marien-Theresien-Platz",
                            "Rathausplatz 1, 1010 Wien",
                            coordinates,
                            "27.11.2020 - 06.01.2021",
                            "10 - 22Uhr",
                            "http://www.wiener-rathausplatz.at/christkindlmarkt.html",
                            listOf(rating1, rating4),
                            //"https://raw.githubusercontent.com/mitchtabian/Kotlin-RecyclerView-Example/json-data-source/app/src/main/res/drawable/coding_for_entrepreneurs.png"
                            "@drawable/zwidemu"
                    )
            )
            return list;
        }
    }


}