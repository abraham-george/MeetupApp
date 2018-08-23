package com.example.abrahamgeorgej.simple_sign_in.providers

import com.example.abrahamgeorgej.simple_sign_in.models.Events
import com.example.abrahamgeorgej.simple_sign_in.models.Urls
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import java.io.Reader

class EventsDataProvider {

    fun getEvents(email: String?, responseHandler: (result: Array<Events>) -> Unit?) {
        Urls.getEvents(email).httpGet().responseObject(ThemeDataDeserializer()){_, response, result ->

            if(response.httpStatusCode != 200){
                throw Exception("Unable to fetch")
            }

            val (data, _) = result
            responseHandler.invoke(data as Array<Events>)

        }

        }
    }

    class ThemeDataDeserializer :ResponseDeserializable<Array<Events>>{
        override fun deserialize(reader: Reader) = Gson().fromJson(reader, Array<Events>::class.java)
    }
