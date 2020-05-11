package com.example.weihnachtmaerkte.markets

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.weihnachtmaerkte.R

import kotlinx.android.synthetic.main.activity_detailed_market.*

class DetailedMarketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_market)
        setSupportActionBar(toolbar)


    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {

        return super.onCreateView(parent, name, context, attrs)
    }

}
