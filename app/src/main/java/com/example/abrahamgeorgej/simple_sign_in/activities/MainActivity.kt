package com.example.abrahamgeorgej.simple_sign_in.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.abrahamgeorgej.simple_sign_in.R
import com.example.abrahamgeorgej.simple_sign_in.models.Urls
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.android.gms.common.config.GservicesValue.init
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_explorer.*
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.layout_fab_submenu.*
//import com.example.abrahamgeorgej.simple_sign_in.R.id.layoutFabPhoto
import com.example.abrahamgeorgej.simple_sign_in.R.id.layoutFabEdit
import com.example.abrahamgeorgej.simple_sign_in.R.id.layoutFabSave
import com.example.abrahamgeorgej.simple_sign_in.fragments.*


class MainActivity : AppCompatActivity() {

    private val explorerFragment: ExplorerFragment
    private val favoritesFragment: FavoritesFragment
    private val mapsFragment: MapsFragment
    private val createMeetup: CreateMeetup
    private val createTheme: CreateTheme
    private var fabExpanded: Boolean = false
    private var layoutFabSave: LinearLayout? = null
    private var layoutFabEdit: LinearLayout? = null
    private var layoutFabSettings: LinearLayout? = null

    init{
        explorerFragment = ExplorerFragment()
        favoritesFragment = FavoritesFragment()
        mapsFragment = MapsFragment()
        createMeetup = CreateMeetup()
        createTheme = CreateTheme()
    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

        when(item.itemId){
            R.id.navigation_explore -> transaction.replace(R.id.fragment_container, explorerFragment)
            R.id.navigation_favorites -> transaction.replace(R.id.fragment_container, favoritesFragment)
            R.id.navigation_location -> transaction.replace(R.id.fragment_container, mapsFragment)

        }
        transaction.commit()
        showFab()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar1)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val transaction =supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, explorerFragment)
        transaction.commit()

        var fab = this.findViewById<View>(R.id.fabSetting) as FloatingActionButton;


        layoutFabSettings = findViewById<LinearLayout>(R.id.layoutFabSettings)
        layoutFabSave = findViewById<LinearLayout>(R.id.layoutFabSave)
        layoutFabEdit = findViewById<LinearLayout>(R.id.layoutFabEdit)


        fab.setOnClickListener(object : View.OnClickListener  {
            override
            fun onClick(v: View) {
                if (fabExpanded == true){
                    closeSubMenusFab(fab);
                } else {
                    openSubMenusFab(fab);
                }
            }
        })

        closeSubMenusFab(fab)
    }

    private fun closeSubMenusFab(fab: FloatingActionButton) {
        layoutFabSave?.visibility = View.INVISIBLE
        layoutFabEdit?.visibility = View.INVISIBLE
        fab?.setImageResource(R.drawable.baseline_add_black_24)
        fabExpanded = false
    }

    private fun hideFab(){
        layoutFabSettings?.visibility = View.INVISIBLE
    }

    private fun showFab(){
        layoutFabSettings?.visibility = View.VISIBLE
    }

    //Opens FAB submenus
    private fun openSubMenusFab(fab: FloatingActionButton) {
        layoutFabSave?.visibility = View.VISIBLE
        layoutFabEdit?.visibility = View.VISIBLE
        //Change settings icon to 'X' icon
        fab?.setImageResource(R.drawable.baseline_close_24)
        fabExpanded = true

        layoutFabEdit?.setOnClickListener(object: View.OnClickListener{
            override
            fun onClick(v: View) {
                closeSubMenusFab(fab)
                hideFab()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, createMeetup) // give your fragment container id in first parameter
                transaction.addToBackStack(null)  // if written, this transaction will be added to backstack
                transaction.commit()
            }
        })

        layoutFabSave?.setOnClickListener(object: View.OnClickListener{
            override
            fun onClick(v: View) {
                closeSubMenusFab(fab)
                hideFab()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, createTheme) // give your fragment container id in first parameter
                transaction.addToBackStack(null)  // if written, this transaction will be added to backstack
                transaction.commit()
            }
        })

    }

    override fun onStart() {
        super.onStart()

        //val account = intent.getStringExtra("account")
        username = com.example.abrahamgeorgej.simple_sign_in.activities.account?.displayName
        token = com.example.abrahamgeorgej.simple_sign_in.activities.account?.idToken
        var data = ""
//        if (account == null){
//            Toast.makeText(this, "Sign in first!", Toast.LENGTH_SHORT).show()
//        }
//        else{
                Urls.authenticateToken(token).httpGet()
                        .responseString { request, response, result ->
                            //do something with response
                            when (result) {
                                is Result.Failure -> {
                                    val ex = result.getException()
                                }
                                is Result.Success -> {
                                    data = result.get()
                                }

                            }
                        }

                if(data != null){
//                    Urls.getThemes().httpGet()
//                            .responseString { request, response, result ->
//                                //do something with response
//                                when (result) {
//                                    is Result.Failure -> {
//                                        val ex = result.getException()
//                                    }
//                                    is Result.Success -> {
//                                        data1 = result.get()
//                                    }
//
//                                }
//                            }

//                    Urls.getThemes().httpGet().responseObject(ThemeDataProvider.ThemeDataDeserializer()){ request, response, result ->
//                        if(response.httpStatusCode != 200){
//                            throw Exception("Unable to fetch")
//                        }
//
//                        val (themes, error) = result
//                        themes?.forEach { theme ->
//                            println(theme)
//                        }
//                    }
//                    val URL: String = "https://www.google.com"
//                    URL.httpGet().responseString{ request, response, result ->
//                        when(result){
//                            is Result.Success -> println(result)
//                        }
//                    }

//                    var dp: ThemeDataProvider = ThemeDataProvider()
//                    dp.getThemes(email) { result ->
//
//                        print(result)
//                    }
                }

        //}
    }
}
