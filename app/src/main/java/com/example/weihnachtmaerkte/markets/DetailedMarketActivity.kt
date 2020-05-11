package com.example.weihnachtmaerkte.markets

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weihnachtmaerkte.R
import kotlinx.android.synthetic.main.activity_detailed_market.*

class DetailedMarketActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_market)
        setSupportActionBar(toolbar)
    }


}
