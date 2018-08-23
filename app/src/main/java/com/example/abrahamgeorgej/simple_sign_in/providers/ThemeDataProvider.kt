package com.example.abrahamgeorgej.simple_sign_in.providers

import com.example.abrahamgeorgej.simple_sign_in.models.Theme
import com.example.abrahamgeorgej.simple_sign_in.models.Urls
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import java.io.Reader

class ThemeDataProvider {

    fun getThemes(email: String?, responseHandler: (result: Array<Theme>) -> Unit?) {
        Urls.getThemes(email).httpGet()
                .responseObject(ThemeDataDeserializer()){_, response, result ->

                    if(response.httpStatusCode != 200){
                        throw Exception("Unable to fetch")
                    }

                    val (data, _) = result
                    responseHandler.invoke(data as Array<Theme>)

                }
    }

    class ThemeDataDeserializer :ResponseDeserializable<Array<Theme>>{
        override fun deserialize(reader: Reader) = Gson().fromJson(reader, Array<Theme>::class.java)
    }
}