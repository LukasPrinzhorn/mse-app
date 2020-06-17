package com.example.weihnachtmaerkte.markets.detailedview

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weihnachtmaerkte.LoginActivity
import com.example.weihnachtmaerkte.LoginActivity.SHARED_PREFERENCES
import com.example.weihnachtmaerkte.LoginActivity.USER_ID
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating
import com.google.firebase.database.FirebaseDatabase


class DetailedMarketRateFragment : Fragment() {

    private lateinit var market: Market
    private lateinit var markets: ArrayList<Market>
    private lateinit var ratings: ArrayList<Rating>
    private lateinit var userId: String

    private lateinit var saveButton: Button
    private lateinit var rbAmbience: RatingBar
    private lateinit var rbFood: RatingBar
    private lateinit var rbDrinks: RatingBar
    private lateinit var rbCrowding: RatingBar
    private lateinit var rbFamily: RatingBar
    private var desText: Boolean = true

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
            findNavController().navigate(R.id.action_RateFragment_to_OverviewFragment)
            view.hideKeyboard()
        }
        initSaveButton()

        val bundle: Bundle? = activity?.intent?.getBundleExtra("bundle")
        val id: String = bundle?.get("id") as String
        markets = bundle.getParcelableArrayList<Market>("markets") as ArrayList<Market>
        ratings = bundle.getParcelableArrayList<Rating>("ratings") as ArrayList<Rating>
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

    private fun initSaveButton() {
        saveButton = requireView().findViewById(R.id.button_rate_save)
        saveButton.isEnabled = false

        saveButton.setOnClickListener {
            saveRating()
            findNavController().navigate(R.id.action_RateFragment_to_OverviewFragment)
            requireView().hideKeyboard()
        }

        rbAmbience = requireView().findViewById(R.id.ratingBarAmbience)
        rbFood = requireView().findViewById(R.id.ratingBarFood)
        rbDrinks = requireView().findViewById(R.id.ratingBarDrinks)
        rbCrowding = requireView().findViewById(R.id.ratingBarCrowding)
        rbFamily = requireView().findViewById(R.id.ratingBarFamily)
        val tvTitle: TextView = requireView().findViewById(R.id.detailed_title_section)
        val tvComment: TextView = requireView().findViewById(R.id.detailed_comment_section)
        initOnRatingListener()
        initTextWatcher(tvTitle, tvComment)
    }

    private fun setData() {
        var textView: TextView = view?.findViewById(R.id.detailed_market_name) as TextView
        textView.text = market.name

        textView = view?.findViewById(R.id.detailed_market_rate_title) as TextView
        val input: String = textView.text as String
        val output = "$input '${market.name}'"
        textView.text = output

        val imageView: ImageView = view?.findViewById(R.id.detailed_market_image) as ImageView

        val requestOptions = RequestOptions()
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)

        Glide.with(imageView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(market.image)
                .into(imageView)
    }

    private fun initOnRatingListener() {
        val ratingBarListener: RatingBar.OnRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, _, _ ->
            saveButton.isEnabled = checkForm()
        }
        rbAmbience.onRatingBarChangeListener = ratingBarListener
        rbFood.onRatingBarChangeListener = ratingBarListener
        rbDrinks.onRatingBarChangeListener = ratingBarListener
        rbCrowding.onRatingBarChangeListener = ratingBarListener
        rbFamily.onRatingBarChangeListener = ratingBarListener
    }

    private fun initTextWatcher(tv1: TextView, tv2: TextView) {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val decisionNoComment = tv1.text.isEmpty() && tv2.text.isEmpty()
                val decisionComment = tv1.text.isNotEmpty() && tv2.text.isNotEmpty()
                desText = (decisionComment || decisionNoComment)
                saveButton.isEnabled = checkForm()
                if (!(decisionComment || decisionNoComment)) {
                    if (tv1.text.isEmpty())
                        tv1.error = "Der Titel Ihres Erfahrungsberichtes ist leer"
                    if (tv2.text.isEmpty())
                        tv2.error = "Ihr Erfahrungsbericht ist leer"
                }

            }

            override fun afterTextChanged(editable: Editable) {
            }
        }
        tv1.addTextChangedListener(textWatcher)
        tv2.addTextChangedListener(textWatcher)
    }

    private fun checkForm(): Boolean {
        return (rbAmbience.rating > 0) &&
                (rbFood.rating > 0) &&
                (rbDrinks.rating > 0) &&
                (rbCrowding.rating > 0) &&
                (rbFamily.rating > 0) && desText
    }

    private fun saveRating() {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("ratings/")

        val SHARED_PREFERENCES = "shared_preferences"
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        var username = ""
        username = if (sharedPreferences.contains(LoginActivity.USERNAME)) {
            sharedPreferences.getString(LoginActivity.USERNAME,null).toString()
        } else {
            userId
        }

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

            val title = if (titleView.text != null) "" + titleView.text else null
            val comment = if (commentView.text != null) "" + commentView.text else null

            val ratingId: String? = databaseReference.push().key
            if (ratingId != null) {
                val rating = Rating(ratingId, starsAmbience, starsFood, starsDrinks, starsCrowding, starsFamily, title, comment, username)
                databaseReference.child(ratingId).setValue(rating)
                ratings.add(rating)
                saveMarket(rating)
            }
        }
    }

    private fun saveMarket(latestRating: Rating) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("markets/" + market.id)

        val ratingIds: ArrayList<String>? = market.ratings
        val results = ArrayList<Rating>()
        if (ratingIds != null) {
            ratings.forEach {
                if (ratingIds.contains(it.id)) {
                    results.add(it)
                }
            }
        }
        results.add(latestRating)
        val avg: ArrayList<Float> = calculateAverageRating(results)
        market.ratings?.add(latestRating.id)
        Log.d("testingwerte", "${market.id}||" + avg[0] + "|" + avg[1] + "|" + avg[2] + "|" + avg[3] + "|" + avg[4] + "|" + avg[5] + "|")

        databaseReference.child("avgAmbience").setValue(avg[0])
        databaseReference.child("avgFood").setValue(avg[1])
        databaseReference.child("avgDrinks").setValue(avg[2])
        databaseReference.child("avgCrowding").setValue(avg[3])
        databaseReference.child("avgFamily").setValue(avg[4])
        databaseReference.child("avgOverall").setValue(avg[5])
        databaseReference.child("numberOfRates").setValue(++market.numberOfRates)
        databaseReference.child("ratings").setValue(market.ratings)

    }

    private fun getUserId(): String {
        val sharedPreferences: SharedPreferences? = activity?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(USER_ID, null)!!
    }

    private fun calculateAverageRating(list: List<Rating>): ArrayList<Float> {
        val results: ArrayList<Float> = ArrayList()
        var counter = 0
        var overall = 0f
        var ambience = 0f
        var food = 0f
        var drinks = 0f
        var crowding = 0f
        var family = 0f
        list.forEach {
            overall += (it.ambience + it.crowding + it.drinks + it.family + it.food) / 5.0f
            ambience += it.ambience
            food += it.food
            drinks += it.drinks
            crowding += it.crowding
            family += it.family
            counter++
        }

        if (counter != 0) {
            results.add(round((ambience / counter), 1))
            results.add(round((food / counter), 1))
            results.add(round((drinks / counter), 1))
            results.add(round((crowding / counter), 1))
            results.add(round((family / counter), 1))
            results.add(round((overall / counter), 1))
        } else {
            results.add(3f)
            results.add(3f)
            results.add(3f)
            results.add(3f)
            results.add(3f)
            results.add(3f)
        }
        return results
    }

    private fun round(number: Float, decimal: Int): Float {
        val numberString: String = "" + number
        val splits: List<String> = numberString.split(".")
        if (splits.size == 1) {
            return number
        }
        return if (splits.size == 2) {
            val s: String = splits[0] + "." + splits[1].substring(0, decimal)
            s.toFloat()
        } else {
            0f
        }
    }
}
