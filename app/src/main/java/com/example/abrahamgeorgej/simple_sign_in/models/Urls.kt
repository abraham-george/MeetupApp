package com.example.abrahamgeorgej.simple_sign_in.models

import com.example.abrahamgeorgej.simple_sign_in.activities.email
import com.example.abrahamgeorgej.simple_sign_in.activities.token

object Urls {

    val BaseUrl = "http://cogent-script-210800.appspot.com"

    fun getThemes(email: String?): String{
        return BaseUrl + "/viewThemes?email=" + email
    }

    fun getEvents(email: String?): String{
        return BaseUrl + "/viewEvents?email=" + email
    }

    fun createMeetup(email: String?): String{
        return BaseUrl + "/upload?email=" + email
    }

    fun authenticateToken(token: String?): String{
        return BaseUrl + "/mauthentic?token=" + token
    }
}