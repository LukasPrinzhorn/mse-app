package com.example.weihnachtmaerkte.markets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailedMarketRateFragment : Fragment() {

    private lateinit var market: Market
    private lateinit var markets: ArrayList<Market>



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed_market_rate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_rate_go_back).setOnClickListener {
            findNavController().navigate(R.id.action_Second2Fragment_to_First2Fragment)
            view.hideKeyboard()

        }
        view.findViewById<Button>(R.id.button_rate_save).setOnClickListener {
            findNavController().navigate(R.id.action_Second2Fragment_to_First2Fragment)
            view.hideKeyboard()
        }
        val bundle: Bundle? = activity?.intent?.getBundleExtra("bundle")
        val id: Long = bundle?.get("id") as Long
        markets = bundle.getParcelableArrayList<Market>("markets") as ArrayList<Market>

        //val markets: List<Market> = com.example.weihnachtmaerkte.backend.DataSource.createMarketsDataSet()
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
}
