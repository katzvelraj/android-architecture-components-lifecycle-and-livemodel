package com.tinmegali.myweather

import android.Manifest
import android.arch.lifecycle.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.tinmegali.myweather.models.WeatherMain
import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import javax.inject.Inject

class MainActivity : LifecycleActivity(), AnkoLogger {


    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        info("onCreate")

        // Dagger
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isLoading(false)
        uiNoData()

        initModel()

        btnGetCity.setOnClickListener {
            info("get city: ${editCity.text}")
            if (!editCity.text.toString().isEmpty()) {
                isLoading(true)
                getWeatherByCity(editCity.text.toString())
            } else {
                toast("Please, write down a city.")
            }
        }

        btnGetLocation.setOnClickListener {
            info("get location")
            if (checkLocationPermissions()) {
                isLoading(true)
                getWeatherByLocation()
            }
        }

    }

    private fun initModel() {
        // Get ViewModel
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MainViewModel::class.java)

        if (viewModel != null) {

            // observe weather
            viewModel!!.getWeather().observe(
                    this@MainActivity,
                    Observer {
                        r ->
                        if ( r != null ) {
                            info("Weather received on MainActivity:\n $r")
                            if (!r.hasError()) {
                                // Doesn't have any errors
                                info("weather: ${r.data}")
                                if (r.data != null)
                                    setUI(r.data)
                            } else {
                                // error
                                error("error: ${r.error}")
                                isLoading(false)
                                if (r.error!!.statusCode != 0) {
                                        toast(r.error.message!!)
                                }
                            }
                        }
                    }
            )
            viewModel!!.getErrors().observe(
                    this@MainActivity,
                    Observer {
                        errorMsg ->
                        // stop pre loader
                        isLoading(false)
                        if (errorMsg != null)
                            showWarning(errorMsg)
                    }
            )

        }
    }

    private fun getWeatherByLocation() {
        info("updateWeatherByLocation")
        isLoading(true)
        viewModel!!.weatherByLocation()
    }

    private fun getWeatherByCity(city: String) {
        info("getWeatherByCity: $city")
        isLoading(true)
        viewModel!!.weatherByCityName(city)
    }

    private fun isLoading( isLoading: Boolean ) {
        info("isLoading: $isLoading")
        if ( isLoading )
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }

    private fun setUI( data: WeatherMain ) {
        info("setUI")
        isLoading(false)
        uiData()
        containerWeather.visibility = View.VISIBLE
        txtCity.text = data.name
        txtMain.text = data.main
        txtDescription.text = data.description
        txtMin.text = data.tempMin.toString()
        txtMax.text = data.tempMax.toString()
        Glide.with(this).load(data.iconUrl()).into(imgIcon)
    }

    fun uiNoData() {
        info("uiNoData")
        containerWeather.visibility = View.GONE
        txtNoWeather.visibility = View.VISIBLE
    }

    fun uiData() {
        info("uiData")
        containerWeather.visibility = View.VISIBLE
        txtNoWeather.visibility = View.GONE
    }

    val permissionReq = 987
    fun checkLocationPermissions(): Boolean {
        info("checkLocationPermissions")
        val permission1 =
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
        val permission2 =
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
        if ( permission1 == PackageManager.PERMISSION_GRANTED &&
                permission2 == PackageManager.PERMISSION_GRANTED ) {
            info("checkLocationPermissions: permission granted.")
            return true
        } else {
            info("checkLocationPermissions: permission denied.")
            ActivityCompat.requestPermissions(
                    this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),permissionReq)
            return false
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when( requestCode ) {
            permissionReq -> { getWeatherByLocation() }
        }
    }

    fun showWarning(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT ).show()
    }
}
