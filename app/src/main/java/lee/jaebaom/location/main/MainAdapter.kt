package lee.jaebaom.location.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_main.view.*
import kotlinx.android.synthetic.main.item_none.view.*
import lee.jaebaom.location.data.AddressData
import lee.jaebaom.location.R

/**
 * Created by leejaebeom on 2017. 12. 22..
 */
class MainAdapter: RecyclerView.Adapter<MainAdapter.ViewHolder>(), View.OnClickListener, MainContract.View{

    lateinit var mainPresenter: MainPresenter
    private var isSearching :Boolean = false
    private val EMPTY_DATA = 0  //검색 전 빈데이터
    private val FILL_DATA = 1   //검색 후 데이터
    private val NONE_DATA= 2    //검색 후 데이터가 없을 때
    private var addresses: ArrayList<AddressData> = ArrayList()

    override fun setPresenter(presenter: MainPresenter) {
        this.mainPresenter = presenter
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (addresses.isNotEmpty()){
            holder.bind(addresses[position])
        }else{
            holder.bind(null)
        }
    }

    override fun getItemCount(): Int {
        if (addresses.isNotEmpty()) {
            return addresses.size
        }else{
            return 1
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val mainItemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_main, parent, false)
        mainItemView.setOnClickListener(this)
        return when(viewType){
            FILL_DATA ->
                MainViewHolder(mainItemView)
            EMPTY_DATA ->
                EmptyViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_none, parent, false), R.drawable.ic_location, "주소를 검색해주세요")
            NONE_DATA ->
                EmptyViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_none, parent, false), R.drawable.ic_none, "찾으시는 주소가 존재하지 않습니다.")
            else ->
                MainViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_main, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (!isSearching && addresses.isEmpty()){
            return EMPTY_DATA
        }else if (isSearching && addresses.isEmpty()){
            return NONE_DATA
        }else{
            return FILL_DATA
        }
    }

    fun clearItems(){
        addresses.clear()
    }

    override fun onClick(p0: View?) {

    }

    override fun updateCallback(newList:List<AddressData>) {
        addresses.addAll(newList)
        notifyDataSetChanged()
    }

    abstract class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(address: AddressData?)
    }

    class MainViewHolder(itemView: View): ViewHolder(itemView){
        override fun bind(address: AddressData?){
            itemView.zipcode.text = address?.zipNo
            itemView.address.text = address?.roadAddr
        }
    }

    class EmptyViewHolder(itemView: View, val id: Int, private val comment: String): ViewHolder(itemView){
        override fun bind(address: AddressData?){
            itemView.image.setImageResource(id)
            itemView.text.text = comment
        }
    }

}
