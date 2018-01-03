package lee.jaebaom.location.main
import android.view.KeyEvent
import android.view.View
import lee.jaebaom.location.data.AddressData

/**
 * Created by leejaebeom on 2018. 1. 4..
 */
interface MainContract {
    interface Presenter{
        fun getJusoData(keyword: String, page: Int)
        fun search(keyword: String, pageNumber: Int)
        fun updateList(newList:List<AddressData>)
        fun initializeObservable()
    }

    interface View{
        fun setPresenter(presenter: MainPresenter)
        fun updateCallback(newList:List<AddressData>)
    }
}