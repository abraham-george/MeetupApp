package com.example.abrahamgeorgej.simple_sign_in.activities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.abrahamgeorgej.simple_sign_in.R
import com.example.abrahamgeorgej.simple_sign_in.holders.CardHolder
import com.example.abrahamgeorgej.simple_sign_in.models.Events

class EventCardRecyclerAdapter : RecyclerView.Adapter<CardHolder>(){
    val currentResults: ArrayList<Events> = ArrayList<Events>()

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(p0: CardHolder, p1: Int) {
        var events = currentResults[p1]
        p0?.updateWithEvents(events)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        var cardItem = LayoutInflater.from(parent?.context).inflate(R.layout.article_card_item, parent, false)
        return CardHolder(cardItem)

    }
}