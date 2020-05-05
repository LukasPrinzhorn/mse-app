package com.example.weihnachtmaerkte.backend

import com.example.weihnachtmaerkte.backend.DTOs.DetailedMarketDTO

class BackendMock {

    fun getMarketById(id: Long): DetailedMarketDTO? {
        val markets: List<DetailedMarketDTO> = DataSource.listOfMarkets()
        var returnMarketDTO : DetailedMarketDTO? = null;
        markets.forEach {
            if (it.id == id){
                returnMarketDTO = it;
            }
        }
        return returnMarketDTO;
    }
}