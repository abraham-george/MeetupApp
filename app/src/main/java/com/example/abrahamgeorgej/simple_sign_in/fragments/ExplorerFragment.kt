package com.example.abrahamgeorgej.simple_sign_in.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.abrahamgeorgej.simple_sign_in.R
import com.example.abrahamgeorgej.simple_sign_in.activities.EventCardRecyclerAdapter
import com.example.abrahamgeorgej.simple_sign_in.activities.ThemeCardRecyclerAdapter
import com.example.abrahamgeorgej.simple_sign_in.activities.email
import com.example.abrahamgeorgej.simple_sign_in.models.Urls.getEvents
import com.example.abrahamgeorgej.simple_sign_in.models.Urls.getThemes
import com.example.abrahamgeorgej.simple_sign_in.providers.EventsDataProvider
import com.example.abrahamgeorgej.simple_sign_in.providers.ThemeDataProvider

/**
 * A simple [Fragment] subclass.
 *
 */
class ExplorerFragment : Fragment() {
    private val themeProvider: ThemeDataProvider = ThemeDataProvider()
    private val eventsProvider: EventsDataProvider = EventsDataProvider()
    var searchCardView: CardView? = null
    var explorerRecycler: RecyclerView? = null
    var spinnerOption: Spinner? = null
    var themeAdapter: ThemeCardRecyclerAdapter = ThemeCardRecyclerAdapter()
    var eventAdapter: EventCardRecyclerAdapter = EventCardRecyclerAdapter()
    var options = arrayOf("Events", "Themes")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_explorer, container, false)

        //searchCardView = view.findViewById<CardView>(R.id.search_card_view)
        explorerRecycler = view.findViewById<RecyclerView>(R.id.explore_article_recycler)
        spinnerOption = view.findViewById<Spinner>(R.id.spinner)
//        searchCardView!!.setOnClickListener{
//            val searchIntent = Intent(context, SearchActivity::class.java)
//            context!! .startActivity(searchIntent)
//        }

        explorerRecycler!!.layoutManager = LinearLayoutManager(context)
        explorerRecycler!!.adapter = themeAdapter


        spinnerOption?.adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, options)

        spinnerOption?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show()
                //Toast.makeText(activity, "Hello", Toast.LENGTH_SHORT).show()
                if(options.get(p2) == "Themes"){
                    explorerRecycler!!.adapter = themeAdapter
                    //explorerRecycler!!.addOnItemTouchListener(RecyclerView.OnItemTouchListener)
                    getThemes()
                }
                else if(options.get(p2) == "Events"){
                    explorerRecycler!!.adapter = eventAdapter
                    getEvents()
                }

            }

        }



        //getEvents()
        return view
    }

    private fun getEvents(){
        eventsProvider.getEvents(email) { result ->
            eventAdapter.currentResults.clear()
            eventAdapter.currentResults.addAll(result)
            activity?.runOnUiThread{ eventAdapter.notifyDataSetChanged()}

        }
    }

    private fun getThemes(){
        themeProvider.getThemes(email) { result ->
            themeAdapter.currentResults.clear()
            themeAdapter.currentResults.addAll(result)
            activity?.runOnUiThread{ themeAdapter.notifyDataSetChanged()}

        }
    }


}
