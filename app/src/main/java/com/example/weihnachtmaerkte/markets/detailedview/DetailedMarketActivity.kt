package com.example.weihnachtmaerkte.markets.detailedview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.weihnachtmaerkte.FriendsActivity
import com.example.weihnachtmaerkte.LoginActivity
import com.example.weihnachtmaerkte.MainActivity
import com.example.weihnachtmaerkte.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class DetailedMarketActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var drawer: DrawerLayout? = null
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_market)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        drawer = findViewById(R.id.drawer_layout)

        navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView!!.setNavigationItemSelectedListener(this)
        navigationView!!.setCheckedItem(R.id.nav_explore)

        setUsername()

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

    override fun onResume() {
        super.onResume()
        navigationView!!.setCheckedItem(R.id.nav_explore)
    }

    private fun setUsername() {
        val sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString(LoginActivity.USER_ID, null)!!
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(FriendsActivity.QR_CODE_PREFIX + userId, BarcodeFormat.QR_CODE, 400, 400)
            val imageViewQrCode: ImageView = navigationView!!.findViewById(R.id.qr_code)
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("Exception", if (e.message == null) "No further information" else e.message)
        }
        val headerMessage: TextView = navigationView!!.getHeaderView(0).findViewById(R.id.header_username)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("users/$userId")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val message = "Hallo " + dataSnapshot.child("username").getValue(String::class.java) + "!"
                headerMessage.text = message
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val message = "Hallo"
                headerMessage.text = message
            }
        })
    }
}
