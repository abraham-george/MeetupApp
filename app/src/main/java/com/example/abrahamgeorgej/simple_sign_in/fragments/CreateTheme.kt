package com.example.abrahamgeorgej.simple_sign_in.fragments


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

import com.example.abrahamgeorgej.simple_sign_in.R
import com.example.abrahamgeorgej.simple_sign_in.activities.email
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_create_meetup.*
import kotlinx.android.synthetic.main.fragment_create_theme.*
import java.io.IOException
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CreateTheme : Fragment(), View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageReference: StorageReference? = null
    private var photoUrl: String? = null
//    private var themeCreate: Button? = null
//    private var themeImageButton: ImageButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_create_theme, container, false)
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference

        val googleApiClient: GoogleApiClient by lazy {
            GoogleApiClient.Builder(activity!!.applicationContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(this.requireActivity(), this)
                    .build();
        }

//        themeCreate = view.findViewById<Button>(R.id.themeCreate)
//        themeImageButton = view.findViewById<ImageButton>(R.id.themeImageButton)

        // Inflate the layout for this fragment
        return view
    }

    override fun onStart() {
        super.onStart()
        themeCreate.setOnClickListener(this)
        themeImageButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view === themeCreate)
            themeCreate(view)
        else if (view === themeImageButton)
            chooseAndUploadThemeImage(view)
    }

    private fun chooseAndUploadThemeImage(v: View?) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
    }

    private fun themeCreate(view: View?) {
        var name = themeName.text
        var desc = themeDescription.text

        val body = listOf("name" to name, "url" to photoUrl, "desc" to desc, "email" to email)
        FuelManager.instance.basePath = "http://cogent-script-210800.appspot.com"
        Fuel.post("/createThemeFromMobile", parameters = body)
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Success -> println(result)
                    }
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            filePath = data.data
            try {
                showFileUpload()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun showFileUpload() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(activity)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val imageRef = storageReference?.child("images/" + UUID.randomUUID().toString())
            imageRef?.putFile(filePath!!)
                    ?.addOnSuccessListener { taskSnapshot ->

                        imageRef.getDownloadUrl().addOnSuccessListener(OnSuccessListener<Uri> {
                            // Got the download URL for 'users/me/profile.png'
                            photoUrl = it.toString()
                            print(photoUrl)
                        }).addOnFailureListener(OnFailureListener {
                            // Handle any errors
                        })
                        progressDialog.dismiss()

                    }
                    ?.addOnFailureListener {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                    ?.addOnProgressListener { taskSnapshot ->

                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")

                    }
        }
    }

    override fun onConnected(p0: Bundle?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        print("API Client Connection Successful!")
    }

    override fun onConnectionSuspended(p0: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        print("API Client Connection Suspended!")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        print("API Client Connection Failed!")
    }


}
