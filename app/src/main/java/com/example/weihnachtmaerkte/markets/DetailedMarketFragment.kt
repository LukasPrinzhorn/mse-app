package com.example.weihnachtmaerkte.markets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DetailedMarketFragment : Fragment() {
    private lateinit var market : Market

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_detailed_market, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_First2Fragment_to_Second2Fragment)
        }

        val bundle: Bundle? = activity?.intent?.getBundleExtra("bundle")
        val id: Long = bundle?.get("id") as Long


        val markets : List<Market> = com.example.weihnachtmaerkte.backend.DataSource.createMarketsDataSet()
        markets.forEach{
            if (it.id == id){
                market = it
            }
        }

        val textView: TextView = view.findViewById(R.id.textview_first)
        val text = "Fragment of market '${market.name}' with id ${market.id}"
        textView.text = text
    }
}
