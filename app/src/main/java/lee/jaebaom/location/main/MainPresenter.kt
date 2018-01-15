package lee.jaebaom.location.main

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import lee.jaebaom.location.data.AddressData
import lee.jaebaom.location.data.JusoDatas
import lee.jaebaom.location.network.JusoRemoteDataSource
import lee.jaebaom.location.data.ResultsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by leejaebeom on 2018. 1. 4..
 */
class MainPresenter(val view: MainContract.View) : MainContract.Presenter{
    init {
        view.setPresenter(this)
    }

    var mainObservable: Observable<ResultsData>? = null

    var mainObserver: Observer<ResultsData>? = null
    override fun getJusoData(keyword: String, page: Int) {
        val data : Call<JusoDatas> = JusoRemoteDataSource.getDataAsync(keyword, page)
        data.enqueue(object : Callback<JusoDatas> {
            override fun onFailure(call: Call<JusoDatas>?, t: Throwable?) {}
            override fun onResponse(call: Call<JusoDatas>?, response: Response<JusoDatas>?) {
                mainObserver?.onNext(response?.body()?.results!!)
            }

        })
    }

    override fun search(keyword: String, pageNumber: Int) {
        getJusoData(keyword, pageNumber)
    }

    override fun updateList(newList:List<AddressData>) {
        view.updateCallback(newList)
    }

    override fun initializeObservable() {
        mainObservable = Observable.create(ObservableOnSubscribe {

        })
    }
}
