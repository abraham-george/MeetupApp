package com.example.abrahamgeorgej.simple_sign_in.fragments


//import com.example.abrahamgeorgej.simple_sign_in.R.id.button
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.abrahamgeorgej.simple_sign_in.R
import com.example.abrahamgeorgej.simple_sign_in.activities.email
import com.example.abrahamgeorgej.simple_sign_in.providers.ThemeDataProvider
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
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
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CreateMeetup : Fragment(), View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private val PICK_IMAGE_REQUEST = 1234
    private var photoUrl: String? = null
    private var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageReference: StorageReference? = null
    private val PLACE_PICKER_REQUEST = 1
    var placeAddress: String = ""
    private val themeProvider: ThemeDataProvider = ThemeDataProvider()
    var themes = ArrayList<String>()
    var spinnerOption: Spinner? = null
    var freeTag: CheckBox? = null
    var paidTag: CheckBox? = null
    var kidFriendlyTag: CheckBox? = null
    var weekendTag: CheckBox? = null
    var onCampus: CheckBox? = null
    var offCampus: CheckBox? = null
    var chosenTheme: String? = null
    var tags = ArrayList<CheckBox>()
    var tagValues = ArrayList<String>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_create_meetup, container, false)
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

        spinnerOption = view?.findViewById<Spinner>(R.id.themeChooser)
        freeTag = view.findViewById<CheckBox>(R.id.free)
        paidTag = view.findViewById<CheckBox>(R.id.paid)
        kidFriendlyTag = view.findViewById<CheckBox>(R.id.kidFriendly)
        weekendTag = view.findViewById<CheckBox>(R.id.weekend)
        onCampus = view.findViewById<CheckBox>(R.id.onCampus)
        offCampus = view.findViewById<CheckBox>(R.id.offCampus)

        tags.add(freeTag!!)
        tags.add(paidTag!!)
        tags.add(kidFriendlyTag!!)
        tags.add(weekendTag!!)
        tags.add(onCampus!!)
        tags.add(offCampus!!)

        themeProvider.getThemes(email) { result ->
            themes.clear()
            for (r in result) {
                themes.add(r.name.toString())
            }
            spinnerOption?.adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, themes)
            Unit

        }


        spinnerOption?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                chosenTheme = themes.get(p2)

            }
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onStart() {
        super.onStart()
        create.setOnClickListener(this)
//        cancel.setOnClickListener(this)
        imageButton.setOnClickListener(this)
        maps.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
//        if (view === cancel)
//            cancel(view)
        if (view === create)
            createMeetup(view)
        else if (view === imageButton)
            chooseAndUpload(view)
        else if (view === maps)
            onAddPlaceButtonClicked(view)
    }

    private fun createMeetup(v: View) {
        var name = meetupName.text
        var desc = meetupDescription.text

        for (box in tags) {

            if (box.isChecked) {
                tagValues.add(box.text.toString())
            }
        }

        val body = listOf("name" to name, "url" to photoUrl, "desc" to desc, "place" to placeAddress, "tagValues" to tagValues, "theme" to chosenTheme, "email" to email)
        FuelManager.instance.basePath = "http://cogent-script-210800.appspot.com"
        Fuel.post("/createMeetupFromMobile", parameters = body)
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Success -> println(result)
                    }
                }
    }

    private fun chooseAndUpload(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)

    }

    private fun onAddPlaceButtonClicked(view: View) {
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this.requireActivity(), permissions, 0)
        if (ActivityCompat.checkSelfPermission(activity!!.applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity!!.applicationContext, "You need to enable location permissions first", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            // Start a new Activity for the Place Picker API, this will trigger {@code #onActivityResult}
            // when a place is selected or with the user cancels.
            var builder: PlacePicker.IntentBuilder = PlacePicker.IntentBuilder();
            var intent: Intent = builder.build(activity);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (e: GooglePlayServicesRepairableException) {
            Log.e("", String.format("GooglePlayServices Not Available [%s]", e.message));
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.e("", String.format("GooglePlayServices Not Available [%s]", e.message));
        } catch (e: Exception) {
            Log.e("", String.format("PlacePicker Exception: %s", e.message));
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            var place: Place = PlacePicker.getPlace(activity?.applicationContext, data)
            if (place == null) {
                Log.i("", "No place selected");
                return;
            }

            // Extract the place information from the API
            placeAddress = place.getAddress().toString()


        } else if (requestCode == PICK_IMAGE_REQUEST &&
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
