package lee.jaebaom.location.network

import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import lee.jaebaom.location.data.JusoDatas
import retrofit2.Call

/**
 * Created by leejaebeom on 2018. 1. 1..
 */
interface JusoRemote {
    @GET("addrlink/addrLinkApi.do?&countPerPage=100&resultType=json&confmKey=U01TX0FVVEgyMDE3MTIyMjIxNTg1NzEwNzU2Njk=")
    fun listRepos(
            @Query("keyword") key: String,
            @Query("currentPage") pageNumber: Int
    ): Call<JusoDatas>
}