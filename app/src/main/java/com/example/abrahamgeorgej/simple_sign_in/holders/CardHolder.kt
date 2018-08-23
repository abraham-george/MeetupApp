package com.example.abrahamgeorgej.simple_sign_in.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.abrahamgeorgej.simple_sign_in.R
import com.example.abrahamgeorgej.simple_sign_in.models.Events
import com.example.abrahamgeorgej.simple_sign_in.models.Theme
import com.squareup.picasso.Picasso

class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val articleImageView: ImageView = itemView.findViewById<ImageView>(R.id.article_image)
    private val titleTextView: TextView = itemView.findViewById<TextView>(R.id.article_title)
    private val descriptionTextView: TextView = itemView.findViewById<TextView>(R.id.article_desc)

    private var currentTheme: Theme? = null
    private var currentEvent: Events? = null

    fun updateWithTheme(theme: Theme){
        currentTheme = theme

        titleTextView.text = theme.name

        //image load
        if(theme.image_url!= null){
            Picasso.with(itemView.context)
                    .load(theme.image_url)
                    .resize(1100, 700)
                    .centerCrop()
                    .into(articleImageView)
        }

        descriptionTextView.text = theme.description

    }

    fun updateWithEvents(event: Events){
        currentEvent = event

        titleTextView.text = event.name

        //image load
        if(event.cover_image!= null){
            Picasso.with(itemView.context)
                    .load(event.cover_image)
                    .resize(1100, 700)
                    .centerCrop()
                    .into(articleImageView)
        }

        descriptionTextView.text = event.description

    }
}