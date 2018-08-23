package com.example.abrahamgeorgej.simple_sign_in.activities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.abrahamgeorgej.simple_sign_in.R
import com.example.abrahamgeorgej.simple_sign_in.holders.CardHolder
import com.example.abrahamgeorgej.simple_sign_in.models.Events
import com.example.abrahamgeorgej.simple_sign_in.models.Theme

class ThemeCardRecyclerAdapter : RecyclerView.Adapter<CardHolder>(){
    val currentResults: ArrayList<Theme> = ArrayList<Theme>()

    override fun getItemCount(): Int {
        //return 15
        return currentResults.size
    }

    override fun onBindViewHolder(p0: CardHolder, p1: Int) {
        var themes = currentResults[p1]
        p0?.updateWithTheme(themes)
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var cardItem = LayoutInflater.from(parent?.context).inflate(R.layout.article_card_item, parent, false)
        return CardHolder(cardItem)

    }
}