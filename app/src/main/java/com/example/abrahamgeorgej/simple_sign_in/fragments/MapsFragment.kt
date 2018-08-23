package com.example.abrahamgeorgej.simple_sign_in.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.abrahamgeorgej.simple_sign_in.R
import com.example.abrahamgeorgej.simple_sign_in.activities.email
import com.example.abrahamgeorgej.simple_sign_in.providers.EventsDataProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: MapView

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMap?.onSaveInstanceState(outState)
    }

    private val eventProvider: EventsDataProvider = EventsDataProvider()
    var lat = ArrayList<Double>()
    var long = ArrayList<Double>()
    var name = ArrayList<String>()

    //var mapsRecycler: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_maps, container, false)

        eventProvider.getEvents(email) { result ->
            lat.clear()
            long.clear()
            for (r in result) {
                lat.add(r.lat!!.toDouble())
                long.add(r.long!!.toDouble())
                name.add(r.name.toString())
            }
            Unit
        }

        mMap = view?.findViewById(R.id.mapView) as MapView
        mMap?.onCreate(savedInstanceState)
        mMap?.getMapAsync(this)

        return view
    }

    override fun onResume() {
        super.onResume()
        mMap?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMap?.onPause()
    }

    override fun onStart() {
        super.onStart()
        mMap?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMap?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMap?.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (ContextCompat.checkSelfPermission(this@MapsFragment.activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }


        var i = lat.size
        for(j in 0..i-1){
            googleMap.addMarker(MarkerOptions().position(LatLng(lat[j], long[j])).title(name[j]))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat[j], long[j])))
        }

    }



}
