package hn.single.imageapp.common.networking

import hn.single.imageapp.features.show_image.models.ImageDetail
import hn.single.imageapp.features.show_image.models.Popular
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface IRetrofit {

    //@GET("https://api.pexels.com/v1/collections/featured?per_page=1")
    //apiKey = XLI8ZB4PsrKtzLCUlj1Ny9HLGhoMoh6JG0IFEKEUMOaaEMcNg0d4WghL
    @GET("/collections/featured?per_page=1")
    fun getItem(@Header("Authorization") apiKey: String): String

    //https://api.pexels.com/v1/collections?per_page=15
    //https://api.pexels.com/v1/collections
    @GET("collections/")
    fun getPopularCategories(@Header("Authorization") apiKey: String): Single<Popular>

    //https://api.pexels.com/v1/collections/$id?per_page=20
    @GET("collections/{id}?per_page=20")
    fun getImagesById(
        @Header("Authorization") apiKey: String,
        @Path("id") id: String
    ): Single<ImageDetail>

    @GET("collections/{id}")
    fun getPopularImages(
        @Header("Authorization") apiKey: String,
        @Path("id") id: String
    ): Single<ImageDetail>

    //https://api.pexels.com/v1/search?query=
    @GET("search?")
    fun searchImagesByText(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String
    ): Single<ImageDetail>

}