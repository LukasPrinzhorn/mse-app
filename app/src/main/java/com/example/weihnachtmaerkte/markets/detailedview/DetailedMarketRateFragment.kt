package com.example.weihnachtmaerkte.markets.detailedview

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weihnachtmaerkte.LoginActivity.SHARED_PREFERENCES
import com.example.weihnachtmaerkte.LoginActivity.USER_ID
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating
import com.google.firebase.database.FirebaseDatabase


class DetailedMarketRateFragment : Fragment() {

    private lateinit var market: Market
    private lateinit var markets: ArrayList<Market>
    private lateinit var userId: String

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detailed_market_rate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = getUserId()

        view.findViewById<Button>(R.id.button_rate_go_back).setOnClickListener {
            findNavController().navigate(R.id.action_Second2Fragment_to_First2Fragment)
            view.hideKeyboard()
        }
        view.findViewById<Button>(R.id.button_rate_save).setOnClickListener {
            saveRating()
            findNavController().navigate(R.id.action_Second2Fragment_to_First2Fragment)
            view.hideKeyboard()
        }
        val bundle: Bundle? = activity?.intent?.getBundleExtra("bundle")
        val id: String = bundle?.get("id") as String
        markets = bundle.getParcelableArrayList<Market>("markets") as ArrayList<Market>
        markets.forEach {
            if (it.id == id) {
                market = it
            }
        }
        setData()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun setData() {
        var textView: TextView = view?.findViewById(R.id.detailed_market_name) as TextView
        textView.text = market.name

        textView = view?.findViewById(R.id.detailed_market_rate_title) as TextView
        val input: String = textView.text as String
        val output = "$input '${market.name}'"
        textView.text = output

        val imageView: ImageView = view?.findViewById(R.id.detailed_market_image) as ImageView

        if (market.image.startsWith("@")) {
            if (market.image == "@drawable/museumsquartier") {
                imageView.setImageResource(R.mipmap.museumsquartier)
            }
            if (market.image == "@drawable/spittelberg") {
                imageView.setImageResource(R.mipmap.spittelberg)
            }
            if (market.image == "@drawable/wiener_weihnachtstraum") {
                imageView.setImageResource(R.mipmap.wiener_weihnachtstraum)
            }
            if (market.image == "@drawable/zwidemu") {
                imageView.setImageResource(R.mipmap.zwidemu)
            }
            if (market.image == "@drawable/karlsplatz") {
                imageView.setImageResource(R.mipmap.karlsplatz)
            }
        }/* else {
            //load real picture
        }*/
    }

    private fun saveRating(){
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("ratings/")
        if (view != null) {
            val titleView: TextView = requireView().findViewById(R.id.detailed_title_section)
            val commentView: TextView = requireView().findViewById(R.id.detailed_comment_section)
            val ratingBarAmbience: RatingBar = requireView().findViewById(R.id.ratingBarAmbience)
            val ratingBarFood: RatingBar = requireView().findViewById(R.id.ratingBarFood)
            val ratingBarDrinks: RatingBar = requireView().findViewById(R.id.ratingBarDrinks)
            val ratingBarCrowding: RatingBar = requireView().findViewById(R.id.ratingBarCrowding)
            val ratingBarFamily: RatingBar = requireView().findViewById(R.id.ratingBarFamily)
            val starsAmbience: Float = ratingBarAmbience.rating
            val starsFood: Float = ratingBarFood.rating
            val starsDrinks: Float = ratingBarDrinks.rating
            val starsCrowding: Float = ratingBarCrowding.rating
            val starsFamily: Float = ratingBarFamily.rating

            val title = if(titleView.text != null) ""+titleView.text else null
            val comment = if(commentView.text != null) ""+commentView.text else null

            val ratingId: String? = databaseReference.push().key
            if (ratingId != null) {
                val rating = Rating(ratingId, starsAmbience, starsFood, starsDrinks, starsCrowding, starsFamily, title, comment, userId)
                databaseReference.child(ratingId).setValue(rating)
            }
        }
    }

    private fun getUserId() : String{
        val sharedPreferences: SharedPreferences? = activity?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(USER_ID, null)!!
    }
}
