package lee.jaebaom.location.detail

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nhn.android.maps.NMapActivity
import com.nhn.android.maps.NMapController
import com.nhn.android.maps.NMapView
import com.nhn.android.maps.maplib.NGeoPoint
import com.nhn.android.maps.nmapmodel.NMapError
import kotlinx.android.synthetic.main.activity_detail_address.*
import kotlinx.android.synthetic.main.item_main.*
import lee.jaebaom.location.R
import lee.jaebaom.location.data.AddressData
import lee.jaebaom.location.util.GeoCode
import java.math.BigDecimal

class DetailAddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val address = intent.extras.getParcelable<AddressData>("data")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_address)

        val mapFragment :MapFragment = fragmentManager.findFragmentById(R.id.mapView) as MapFragment

        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE)
        collapsingToolbar.setExpandedTitleColor(Color.WHITE)
        toolbar.title = "${address.rn} ${address.buldMnnm}"
        if (!address.buldSlno.equals("0")){
            toolbar.title = toolbar.title.toString() + "- ${address.buldSlno}"
        }
        setSupportActionBar(toolbar)

        road_text.text = address.roadAddr
        building_text.text = address.jibunAddr
        english_text.text = address.engAddr
        code_text.text = address.zipNo

        mapFragment.getMapAsync { googleMap ->
            val seoul = GeoCode.searchCoordinate(this, address.jibunAddr)
            val lat = seoul.latitude.toBigDecimal().setScale(6, BigDecimal.ROUND_HALF_UP).toDouble()
            val long = seoul.longitude.toBigDecimal().setScale(6, BigDecimal.ROUND_HALF_UP).toDouble()
            geo_text.text = "${lat}, ${long}"
            val markerOptions = MarkerOptions()
            markerOptions.position(seoul)
            markerOptions.title(address.zipNo)
//            markerOptions.snippet("한국의 수도")

            googleMap.addMarker(markerOptions)
            googleMap.uiSettings.isScrollGesturesEnabled = false
            googleMap.uiSettings.isZoomGesturesEnabled = false
            googleMap.uiSettings.isMapToolbarEnabled= false
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(seoul))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16f))
        }
    }
}
