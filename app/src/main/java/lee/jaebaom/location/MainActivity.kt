package lee.jaebaom.location

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.nhn.android.maps.NMapView
import kotlinx.android.synthetic.main.activity_main.*
import lee.jaebaom.location.data.AddressData
import lee.jaebaom.location.recycler.MainAdapter
import io.reactivex.*
import io.reactivex.disposables.Disposable
import lee.jaebaom.location.data.JusoDatas
import lee.jaebaom.location.data.JusoRemoteDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private var loading = false
    private var lastVisibleItem: Int = 0
    private var totalItemCount:Int = 0
    private var pageNumber = 1

    var mainObservable: Observable<List<AddressData>>? = null
    var mainObserver: Observer<List<AddressData>>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val adapter = MainAdapter()
        keyword.setOnKeyListener { view, i, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)){
                getJusoData(keyword.text.toString(), pageNumber).apply {
                    adapter.clearItems()
                    progressBar.visibility = View.VISIBLE
                }
                return@setOnKeyListener true
            }else{
                return@setOnKeyListener false
            }
        }

        recycler.adapter = adapter
        val naverMap = NMapView(this).apply {
            setClientId(getString(R.string.naver_map_api_client_id))
            isClickable = false
            isEnabled = false
        }
        mainObservable = Observable.create(ObservableOnSubscribe {

        })
        mainObserver = object : Observer<List<AddressData>> {

            override fun onNext(t: List<AddressData>) {
                adapter.addItems(t)
                adapter.notifyDataSetChanged()
                loading = false
                progressBar.visibility = View.GONE
            }

            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {
                Toast.makeText(this@MainActivity, "구독됨", Toast.LENGTH_SHORT).show()
            }
            override fun onError(e: Throwable) {}
        }

        mainObservable?.subscribe(mainObserver!!)

        setUpPagination()


    }

    fun setUpPagination(){
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val VISIBLE_THRESHOLD = 1
                val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
                totalItemCount = layoutManager.itemCount
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!loading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD) && !keyword.text.toString().equals("")){
                    //input
                    pageNumber++
                    getJusoData(keyword.text.toString(), pageNumber).apply {
                        loading = true
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    fun getJusoData(keyword: String, page: Int) {
        val data :Call<JusoDatas> = JusoRemoteDataSource.getDataAsync(keyword, page)
        data.enqueue(object :Callback<JusoDatas>{
            override fun onFailure(call: Call<JusoDatas>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<JusoDatas>?, response: Response<JusoDatas>?) {
                mainObserver?.onNext(response?.body()?.results?.juso!!)
            }

        })
    }
}

