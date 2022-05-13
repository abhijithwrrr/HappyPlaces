package com.abhijith.happyplaces.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.util.*


/**
 * A AsyncTask class to get the address from latitude and longitude.
 */
class GetAddressFromLatLng(
    context: Context, private val latitude: Double,
    private val longitude: Double
) : AsyncTask<Void, String, String>() {

    /**
     * Constructs a Geocoder whose responses will be localized for the
     * given Locale.
     */
    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())

    /**
     * A variable of address listener interface.
     * Reference Youtube
     */
    private lateinit var mAddressListener: AddressListener

    /**
     * Background method of AsyncTask where the background operation will be performed.
     */
    override fun doInBackground(vararg params: Void?): String {
        try {
            /**
             * Returns an array of Addresses that are known to describe the
             * area immediately surrounding the given latitude and longitude.
             */
            val addressList: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (addressList != null && addressList.isNotEmpty()) {
                val address: Address = addressList[0]
                val sb = StringBuilder()
                for (i in 0..address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append(",")
                }
                sb.deleteCharAt(sb.length - 1) // Here we remove the last comma that we have added above from the address.
                return sb.toString()
            }
        } catch (e: IOException) {
            Log.e("HappyPlaces", "Unable connect to Geocoder")
        }

        return ""
    }

    /**
     * onPostExecute method of AsyncTask where the result will be received and assigned to the interface accordingly.
     */
    override fun onPostExecute(resultString: String?) {
        if (resultString == null) {
            mAddressListener.onError()
        } else {
            mAddressListener.onAddressFound(resultString)
        }
        super.onPostExecute(resultString)
    }

    fun setAddressListener(addressListener: AddressListener) {
        mAddressListener = addressListener
    }

    fun getAddress() {
        execute()
    }

    interface AddressListener {
        fun onAddressFound(address: String?)
        fun onError()
    }
}