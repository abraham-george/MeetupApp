package com.example.abrahamgeorgej.simple_sign_in.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.abrahamgeorgej.simple_sign_in.R
import com.example.abrahamgeorgej.simple_sign_in.holders.ListItemHolder

class ArticleListItemRecyclerAdapter : RecyclerView.Adapter<ListItemHolder>(){

    override fun getItemCount(): Int {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return 15
    }

    override fun onBindViewHolder(p0: ListItemHolder, p1: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        //TODO("not implemented") //To change body of created ListItemHolder use File | Settings | File Templates.
        var cardItem = LayoutInflater.from(parent?.context).inflate(R.layout.article_list_item, parent, false)
        return ListItemHolder(cardItem)

    }
}