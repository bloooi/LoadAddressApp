package lee.jaebaom.location.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_main.view.*
import kotlinx.android.synthetic.main.item_none.view.*
import lee.jaebaom.location.data.AddressData
import lee.jaebaom.location.R
import lee.jaebaom.location.detail.DetailAddressActivity

/**
 * Created by leejaebeom on 2017. 12. 22..
 */
class MainAdapter(val context: Context): RecyclerView.Adapter<MainAdapter.ViewHolder>(), MainContract.View{
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
        return when(viewType){
            FILL_DATA ->
                MainViewHolder(context, LayoutInflater.from(parent?.context).inflate(R.layout.item_main, parent, false))
            EMPTY_DATA ->
                EmptyViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_none, parent, false), R.drawable.ic_location, "주소를 검색해주세요")
            NONE_DATA ->
                EmptyViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_none, parent, false), R.drawable.ic_none, "찾으시는 주소가 존재하지 않습니다.")
            else ->
                MainViewHolder(context, LayoutInflater.from(parent?.context).inflate(R.layout.item_main, parent, false))
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


    override fun updateCallback(newList:List<AddressData>) {
        addresses.addAll(newList)
        notifyDataSetChanged()
    }

    abstract class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(address: AddressData?)
    }

    class MainViewHolder(val context: Context, itemView: View): ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{
        var info: AddressData? = null
        override fun bind(address: AddressData?){
            itemView.zipcode.text = address?.zipNo
            itemView.address.text = address?.roadAddr
            itemView.jibun_address.text = address?.jibunAddr
            info = address

            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            val detailIntent = Intent(context, DetailAddressActivity::class.java)
            detailIntent.putExtra("data", info)
            context.startActivity(detailIntent)
        }

        override fun onLongClick(p0: View?): Boolean {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.primaryClip = ClipData.newPlainText("도로명 주소", p0?.address?.text)
            Snackbar.make(p0!!, "도로명 주소가 복사되었습니다.", Snackbar.LENGTH_LONG).show()
            return true
        }
    }

    class EmptyViewHolder(itemView: View, val id: Int, private val comment: String): ViewHolder(itemView){
        override fun bind(address: AddressData?){
            itemView.image.setImageResource(id)
            itemView.text.text = comment
        }
    }

}
