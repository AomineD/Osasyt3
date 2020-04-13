package com.dagf.uweyt3.iptv

import com.dagf.uweyt3.iptv.ApiClient
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface ApiInterface {

    /*@GET("movie/now_playing?api_key=" + ApiClient.API_KEY)
    fun getLatestMovies(): Call<ServerResponse<Movie>>

    @GET("genre/movie/list?api_key=" + ApiClient.API_KEY)
    fun getMoviesCategory(): Call<ServerResponse<MovieCategory>>

    @GET("movie/upcoming?api_key=" + ApiClient.API_KEY)
    fun getUpcomingMovies(): Call<ServerResponse<Movie>>

    @GET("movie/top_rated?api_key=" + ApiClient.API_KEY)
    fun getTopRatedMovies(): Call<ServerResponse<Movie>>

    @GET("movie/popular?api_key=" + ApiClient.API_KEY)
    fun getPopularMovies(): Call<ServerResponse<Movie>>

    @GET("movie/{movie_id}/similar?api_key=" + ApiClient.API_KEY)
    fun getSimilarMovies(@Path("movie_id") movieId: String): Call<ServerResponse<Movie>>

    @GET("movie/{movie_id}/external_ids?api_key=" + ApiClient.API_KEY)
    fun getExternalIds(@Path("movie_id") movieId: String): Call<JsonObject>

    @GET("movie/{movie_id}/videos?api_key=" + ApiClient.API_KEY)
    fun getMovieTrailer(@Path("movie_id") movieId: String): Call<ServerResponse<MovieTrailer>>

    @GET("movie/{movie_id}/credits?api_key=" + ApiClient.API_KEY)
    fun getMovieCasts(@Path("movie_id") movieId: String): Call<ServerResponse<Cast>>

    @GET("discover/movie?api_key=" + ApiClient.API_KEY)
    fun getCategoryMovies(@Query("with_genres") categoryId: Int, @Query("page") pageNo: Int): Call<ServerResponse<Movie>>

    @GET("search/movie?api_key=" + ApiClient.API_KEY)
    fun findMovie(@Query("query") movieName: String, @Query("page") pageNo: Int): Call<ServerResponse<Movie>>

    @GET("Ads.json")
    fun getAdsIds(): Call<Ads>

    @GET("AppUpdates.json")
    fun getAppUpdates(): Call<AppUpdates>
*/
    @GET
    fun fetchFile(@Url fileUrl: String): Call<ResponseBody>

    @GET("{country_name}?name;capital;alpha2Code")
    fun getCountryDetails(@Path("country_name", encoded = true) countryName: String): Call<Array<Country>>
}