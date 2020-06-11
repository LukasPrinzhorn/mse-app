package com.example.weihnachtmaerkte.markets.detailedview

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.weihnachtmaerkte.FriendsActivity
import com.example.weihnachtmaerkte.MainActivity
import com.example.weihnachtmaerkte.R
import com.google.android.material.navigation.NavigationView

class DetailedMarketActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var drawer: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_market)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        drawer = findViewById(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_explore)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer?.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_friends -> {
                val intent = Intent(this, FriendsActivity::class.java)
                intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_ANIMATION
                drawer!!.closeDrawer(GravityCompat.START)
                drawer!!.postDelayed({ startActivity(intent) }, 200)
            }
            R.id.nav_explore -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_ANIMATION
                drawer!!.closeDrawer(GravityCompat.START)
                drawer!!.postDelayed({ startActivity(intent) }, 200)
            }
        }
        return true
    }
}
