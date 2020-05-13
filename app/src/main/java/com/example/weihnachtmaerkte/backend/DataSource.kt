package com.example.weihnachtmaerkte.backend

import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating

class DataSource {

    companion object {
        fun createMarketsDataSet(): List<Market> {
            val list = ArrayList<Market>()
            val rating1 =
                    Rating(1,
                            4.8f,
                            3.7f,
                            3.5f,
                            2.3f,
                            1.8f,
                            "Ekelhaft",
                            "Überall liegt Müll.",
                            null)

            val rating2 =
                    Rating(1,
                            3.8f,
                            2.7f,
                            4.5f,
                            1.3f,
                            1.3f,
                            "Köstliches Essen",
                            "Besonders Heinz Bratwurststand ist sehr zu empfehlen.",
                            null)
            val rating3 =
                    Rating(3,
                            1.8f,
                            1.7f,
                            1.5f,
                            0.3f,
                            1.3f,
                            "Geil",
                            "Punsch haut richtig rein",
                            null)
            val rating4 =
                    Rating(4,
                            4.8f,
                            4.7f,
                            4.5f,
                            2.3f,
                            4.3f,
                            "Wunderbar",
                            "Meine Familie liebt Wien's Weihnachtsmärkte, aber diesen ganz besonders",
                            null)
            val rating5 =
                    Rating(4,
                            4.8f,
                            4.2f,
                            2.5f,
                            1.3f,
                            4.3f,
                            "Omas Apfelzauber",
                            "Die schönste Zeit im Jahr noch süßer mit Omas Apfelzauber Punsch beim Kesslerstand. Und das zu nur 3€",
                            null)
            val coordinates: DoubleArray = doubleArrayOf(2.0, 3.5)

            list.add(
                    Market(101,
                            "Wiener Weihnachtstraum",
                            "Rathausplatz 1, 1010 Wien",
                            coordinates,
                            "27.11.2020 - 06.01.2021",
                            "10 - 22Uhr",
                            "http://www.wiener-rathausplatz.at/christkindlmarkt.html",
                            listOf(rating1, rating4),
                            //"https://raw.githubusercontent.com/mitchtabian/Blog-Images/master/digital_ocean.png"
                            "@drawable/wiener_weihnachtstraum"
                    )
            )

            list.add(
                    Market(102,
                            "Museumsquartier",
                            "Museumsplatz 1, 1070 Wien",
                            coordinates,
                            "06.12.2020 - 28.12.2020",
                            "13 - 20Uhr",
                            "https://www.stadt-wien.at/wien/maerkte/winter-im-mq-advent-im-museumsquartier-wien.html",
                            listOf(rating2, rating3, rating4),
                            //"https://raw.githubusercontent.com/mitchtabian/Kotlin-RecyclerView-Example/json-data-source/app/src/main/res/drawable/time_to_build_a_kotlin_app.png"
                            "@drawable/museumsquartier"
                    )
            )

            list.add(
                    Market(103,
                            "Weihnachtsdorf Marien-Theresien-Platz",
                            "Maria-Theresien-Platz, Burgring 5, 1010 Wienn",
                            coordinates,
                            "24.11.2020 - 31.12.2020",
                            "11 - 21Uhr",
                            "https://www.weihnachtsmarkt.at/maria-theresien-platz/der-markt/uebersicht/",
                            listOf(rating1, rating4),
                            //"https://raw.githubusercontent.com/mitchtabian/Kotlin-RecyclerView-Example/json-data-source/app/src/main/res/drawable/coding_for_entrepreneurs.png"
                            "@drawable/zwidemu"
                    )
            )
            list.add(
                    Market(104,
                            "Weihnachtsmarkt Karlsplatz",
                            "Karlsplatz 1, 1040 Wien",
                            coordinates,
                            "23.11.2020 - 26.12.2020",
                            "11 - 01Uhr",
                            "http://www.wiener-rathausplatz.at/christkindlmarkt.html",
                            listOf(rating2, rating3, rating4, rating5),
                            //"https://raw.githubusercontent.com/mitchtabian/Kotlin-RecyclerView-Example/json-data-source/app/src/main/res/drawable/coding_for_entrepreneurs.png"
                            "@drawable/karlsplatz"
                    )
            )
            return list
        }
    }
}