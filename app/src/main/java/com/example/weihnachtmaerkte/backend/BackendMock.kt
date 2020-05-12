package com.example.weihnachtmaerkte.backend

import com.example.weihnachtmaerkte.backend.DTOs.*

class BackendMock {

    /*fun getMarketById(id: Long): DetailedMarketDTO? {
        val markets: List<DetailedMarketDTO> = DataSource.listOfMarkets()
        var returnMarketDTO : DetailedMarketDTO? = null;
        markets.forEach {
            if (it.id == id){
                returnMarketDTO = it;
            }
        }
        return returnMarketDTO;
    }*/

    fun getMarketById(id: Long): DetailedMarketDTO? {
        return DetailedMarketDTO(100,
                "Wiener Weihnachtstraum",
                "Rathausplatz 1, 1010 Wien",
                "27.11.2020 - 06.01.2021",
                "10 - 22Uhr",
                "http://www.wiener-rathausplatz.at/christkindlmarkt.html",
                SimpleRatingDTO(4.8f,
                        3.7f,
                        3.5f,
                        2.3f,
                        1.8f),
                listOf(1),
                "@drawable/wiener_weihnachtstraum"
        )
    }

    fun getAllMarketsFiltered(xCoord: Double, yCoord: Double, radius: Double): List<SimpleMarketDTO> {
        val result = ArrayList<SimpleMarketDTO>()

        result.add(SimpleMarketDTO(1,
                "Wiener Weihnachtstraum",
                DoubleArray(2) { 2.0 },
                SimpleRatingDTO(4.5f,
                        3.9f,
                        4.9f,
                        2.5f,
                        4.6f),
                "SampleImage"
        ))
        result.add(SimpleMarketDTO(2,
                "Arabella Weihnachtsdorf",
                DoubleArray(2) { 2.0 },
                SimpleRatingDTO(4.0f,
                        3.5f,
                        4.9f,
                        4.5f,
                        4.2f),
                "SampleImage"
        ))
        result.add(SimpleMarketDTO(3,
                "MQ Weihnachtsmarkt",
                DoubleArray(2) { 2.0 },
                SimpleRatingDTO(3.8f,
                        2.5f,
                        3.9f,
                        3.5f,
                        3.8f),
                "SampleImage"
        ))
        result.add(SimpleMarketDTO(4,
                "Weihnachtsmarkt Spittelberg",
                DoubleArray(2) { 2.0 },
                SimpleRatingDTO(4.0f,
                        3.7f,
                        4.2f,
                        3.5f,
                        4.0f),
                "SampleImage"
        ))
        result.add(SimpleMarketDTO(5,
                "Blumengärten Hirschstetten",
                DoubleArray(2) { 2.0 },
                SimpleRatingDTO(4.9f,
                        3.5f,
                        4.0f,
                        4.5f,
                        4.9f),
                "SampleImage"
        ))
        result.add(SimpleMarketDTO(6,
                "Karlsplatz",
                DoubleArray(2) { 2.0 },
                SimpleRatingDTO(3.5f,
                        3.4f,
                        4.5f,
                        4.5f,
                        4.2f),
                "SampleImage"
        ))

        return result
    }

    fun getAllMarkets(): List<SimpleMarketDTO> {
        return getAllMarketsFiltered(0.0, 0.0, 0.0)
    }

    fun getCoordinates(input: String): DoubleArray? {
        return DoubleArray(2) { 2.0 }
    }

    fun getRatingById(id: Long): DetailedRatingDTO {
        val images = ArrayList<String>()
        images.add("Image 1")
        images.add("Image 2")

        return DetailedRatingDTO(1,
                3.5f,
                3.4f,
                4.5f,
                4.5f,
                4.2f,
                "Netter Besuch",
                "Wir hatten einen schönen Aufenthalt mit unserer gersamten Familie!",
                images
        )
    }

    fun getUserById(id: Long): DetailedUserDTO? {
        val friends = ArrayList<SimpleUserDTO>()
        friends.add(SimpleUserDTO(2,
                "vienna_traveler123"))
        friends.add(SimpleUserDTO(3,
                "0815_tourist"))
        friends.add(SimpleUserDTO(2,
                "der_punscher"))
        friends.add(SimpleUserDTO(3,
                "lebkuchenfreak"))
        friends.add(SimpleUserDTO(2,
                "glühweinfanatiker"))
        friends.add(SimpleUserDTO(3,
                "christkind"))
        friends.add(SimpleUserDTO(2,
                "zuckerstange_nr1"))
        friends.add(SimpleUserDTO(3,
                "weihnachtsmann"))
        friends.add(SimpleUserDTO(2,
                "zimtnelke"))
        friends.add(SimpleUserDTO(3,
                "schaumrolle"))

        return DetailedUserDTO(1,
                "robert_1",
                friends)
    }

}