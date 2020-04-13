package com.dagf.uweyt3.iptv

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "b1c5b3ae3b8dc94d895cd5e73a8a90e3"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w200"
        const val BANNER_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val COUNTRY_URL = "https://restcountries.eu/rest/v2/name/"
        const val COUNTRY_FLAG_URL = "https://www.countryflags.io/%s/flat/64.png"
        const val IPTV_URL = "https://iptv-org.github.io/iptv/"
        const val IPTV_FILE_URL = "https://raw.githubusercontent.com/sharyrajpoot/exiontv/master/"
        const val ADS_URL = "https://exion-tv.firebaseio.com/"
        const val APP_UPDATES = "https://exion-tv.firebaseio.com/"

        var retrofit: Retrofit? = null
        var countryApi: Retrofit? = null

        fun getClient(): Retrofit? {
            if(retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit
        }

        fun getCountryAPI(): Retrofit? {
            if(countryApi == null) {
                countryApi = Retrofit.Builder()
                    .baseUrl(COUNTRY_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return countryApi
        }
    }

    fun getFile(URL: String): Retrofit? {
        return Retrofit.Builder()
                .baseUrl(URL)
                .client(OkHttpClient.Builder().build())
                .build()
    }

    fun getAdIds(): Retrofit? {
        return Retrofit.Builder()
            .baseUrl(ADS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAppUpdates(): Retrofit? {
        return Retrofit.Builder()
            .baseUrl(APP_UPDATES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}