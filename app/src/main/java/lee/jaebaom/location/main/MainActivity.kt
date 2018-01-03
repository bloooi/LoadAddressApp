package lee.jaebaom.location.main

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.nhn.android.maps.NMapView
import kotlinx.android.synthetic.main.activity_main.*
import lee.jaebaom.location.data.AddressData
import io.reactivex.*
import io.reactivex.disposables.Disposable
import lee.jaebaom.location.R
import lee.jaebaom.location.data.ResultsData


class MainActivity : AppCompatActivity() {

    private var loading = false
    private var totalPageCount: Int = 0
    private var pageNumber = 1
    val adapter = MainAdapter()
    private val mainPresenter = MainPresenter(adapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        keyword.setOnKeyListener { view, i, keyEvent ->
            val imm : InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)){
                initialize()

                mainPresenter.search(keyword.text.toString(), pageNumber).apply {
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    progressBar.visibility = View.VISIBLE   //로딩
                }
                return@setOnKeyListener true
            }else{
                return@setOnKeyListener false
            }
        }

        recycler.adapter = adapter

        mainPresenter.initializeObservable()
        mainPresenter.mainObserver = object : Observer<ResultsData> {

            override fun onNext(t: ResultsData) {
                mainPresenter.updateList(t.juso)
                if (totalPageCount == 0){
                    totalPageCount = t.common.totalCount / 100
                    if ((t.common.totalCount / 100) > 0){
                        totalPageCount++
                    }
                }
                loading = false
                progressBar.visibility = View.GONE
            }

            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {
            }
            override fun onError(e: Throwable) {}
        }

        mainPresenter.mainObservable?.subscribe(mainPresenter.mainObserver!!)

        setUpPagination()


    }

    fun setUpPagination(){
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val VISIBLE_THRESHOLD = 1
                val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!loading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD) && !keyword.text.toString().equals("") && pageNumber < totalPageCount){
                    //input
                    pageNumber++
                    mainPresenter.search(keyword.text.toString(), pageNumber).apply {
                        loading = true
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    fun initialize(){
        pageNumber = 1  //새로운 검색시 첫 페이지부터 시작해야 한다.
        totalPageCount = 0  //새로운 검색시 전체 페이지 수를 받아오기 위해 0으로 초기화
        adapter.clearItems()    //새로운 검색이기 때문에 이전 검색결과는 지운다.
    }

}

//val naverMap = NMapView(this).apply {
//    setClientId(getString(R.string.naver_map_api_client_id))
//    isClickable = false
//    isEnabled = false
//}

