package lee.jaebaom.location.util

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

/**
 * Created by leejaebeom on 2018. 1. 8..
 */
class GeoCode{
    companion object {
        fun searchCoordinate(context: Context, address: String): LatLng{
            val geoCoder = Geocoder(context)
            try {
                val coordinate = geoCoder.getFromLocationName(address, 10)
                if(coordinate.isNotEmpty()){
                    coordinate[0].apply {
                        return LatLng(latitude, longitude)
                    }
                }else{
                    return LatLng(0.0,0.0)
                }
            }catch (e: IOException){
                Log.e("GeoCoder Error: ", e.message)

            }
            return LatLng(0.0,0.0)
        }
    }
}