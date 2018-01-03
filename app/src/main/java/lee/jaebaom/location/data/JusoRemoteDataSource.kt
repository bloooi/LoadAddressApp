package lee.jaebaom.location.data

import lee.jaebaom.location.createRetrofit
import lee.jaebaom.location.network.JusoRemote
import retrofit2.Call

/**
 * Created by leejaebeom on 2018. 1. 3..
 */
object JusoRemoteDataSource{
    private val remote : JusoRemote = createRetrofit(JusoRemote::class.java, "http://www.juso.go.kr/")

    fun getDataSync(keyword: String, page: Int): JusoDatas?{
        val call : Call<JusoDatas> = remote.listRepos(keyword, page)
        return  call.execute().body()
    }

    fun getDataAsync(keyword: String, page: Int): Call<JusoDatas> = remote.listRepos(keyword, page)
}