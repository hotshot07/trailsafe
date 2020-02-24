package com.example.trailsafe

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.trailsafe.Helper.DirectionsJSONParser
import com.example.trailsafe.Remote.IGoogleAPIService
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dmax.dialog.SpotsDialog
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val TAG = MapsActivity::class.java.simpleName
    private val REQUEST_LOCATION_PERMISSION = 1

    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()
    lateinit var mService: IGoogleAPIService

    private lateinit var destination: Location

    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null

    //location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    lateinit var polyLine: Polyline

    lateinit var info: TextView

    companion object {
        private const val MY_PERMISSION_CODE: Int = 1000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val mSearchText = findViewById<EditText>(R.id.input_search)

        //Request Runtime Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallback()

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )
            }
        }
        else{
            buildLocationRequest()
            buildLocationCallback()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.myLooper())
        }
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.locations.get(p0!!.locations.size - 1)//get last location

                if (mMarker != null) {
                    mMarker!!.remove()
                }

                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                val latLng = LatLng(latitude, longitude)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                mMarker = map!!.addMarker(markerOptions)

                //move camera
                map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.6f))
            }
        }
    }

    private fun drawPath(mLastLocation:Location?, location: Location?){
        if(polyLine != null)
            polyLine.remove() //remove old directions

        val origin = StringBuilder(mLastLocation!!.latitude.toString())
            .append(",")
            .append(mLastLocation!!.longitude.toString())
            .toString()
        val destination = StringBuilder(location!!.latitude.toString())
            .append(",")
            .append(location!!.longitude.toString())
            .toString()

        mService.getDirections(origin, destination )
            .enqueue(object:Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("Cian", t.message)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    ParserTask().execute(response.body()!!.toString())
                }

            })
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f

    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            return false
        } else
            return true
    }

    //Override OnrequestPermissionResult
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                        if (checkLocationPermission()) {
                            buildLocationRequest()
                            buildLocationCallback()

                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                            map!!.isMyLocationEnabled = true
                        }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                }
            }
        }
    }


    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                map!!.isMyLocationEnabled = true
            }
        }
        else
            map!!.isMyLocationEnabled = true

        //enable zoom control

        setMapLongClick(map)
        setPoiClick(map)
        //enableMyLocation()
        map.uiSettings.isZoomControlsEnabled = true

        if(destination != null){
            drawPath(mLastLocation, destination)
        }


    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { LatLng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                LatLng.latitude,
                LatLng.longitude
            )
            map.clear()

            destination = Location(LocationManager.GPS_PROVIDER)
            destination.setLatitude(LatLng.latitude)
            destination.setLongitude(LatLng.longitude)

            map.addMarker(
                MarkerOptions()
                    .position(LatLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)

            )

        }

    }


    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker.showInfoWindow()
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    inner class ParserTask:AsyncTask<String,Int,List<List<HashMap<String,String>>>>() {

        internal val waitingDialog: android.app.AlertDialog? = SpotsDialog.Builder().setContext(this@MapsActivity).build()

        override fun onPreExecute() {
            super.onPreExecute()
            waitingDialog!!.show()
            waitingDialog!!.setMessage("Calculating route")
        }

        override fun doInBackground(vararg params: String?): List<List<HashMap<String, String>>>? {
            val jsonObject:JSONObject
            var routes: List<List<HashMap<String, String>>>? = null
            try{
                jsonObject = JSONObject(params[0])
                val parser = DirectionsJSONParser()
                routes = parser.parse(jsonObject)
            }catch (e:JSONException)
            {
                e.printStackTrace()
            }
            return routes
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>) {
            super.onPostExecute(result)

            var points: ArrayList<LatLng>? = null
            var polylineOptions: PolylineOptions? = null

            for(i in result.indices)
            {
                points = ArrayList()
                polylineOptions = PolylineOptions()

                val path: List<HashMap<String, String>> = result[i]

                for (j in path.indices)
                {
                    val point :HashMap<String,String> = path[i]
                    val lat:Double = point["lat"]!!.toDouble()
                    val lng:Double = point["lng"]!!.toDouble()
                    val position = LatLng(lat, lng)

                    points.add(position)

                }
                polylineOptions.addAll(points)
                polylineOptions.width(12f)
                polylineOptions.color(Color.MAGENTA)
                polylineOptions.geodesic(true)
            }
            polyLine = map!!.addPolyline(polylineOptions)
            waitingDialog!!.dismiss()
        }

    }
}