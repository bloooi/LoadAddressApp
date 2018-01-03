package lee.jaebaom.location.recycler

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
class MainAdapter(): RecyclerView.Adapter<MainAdapter.ViewHolder>(){
    var isSearching :Boolean = false
    val EMPTY_DATA = 0  //검색 전 빈데이터
    val FILL_DATA = 1   //검색 후 데이터
    val NONE_DATA= 2    //검색 후 데이터가 없을 때
    var addresses: ArrayList<AddressData> = ArrayList()
    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
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
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainAdapter.ViewHolder{
        return when(viewType){
            FILL_DATA ->
                MainViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_main, parent, false))
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
    fun addItems(data : List<AddressData>){
        addresses.addAll(data)
    }

    fun clearItems(){
        addresses.clear()
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
