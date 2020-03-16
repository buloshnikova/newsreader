package me.happyclick.news.model

import io.reactivex.Single
import me.happyclick.news.util.API_KEY_NEWS_API
import me.happyclick.news.util.SOURCE_ID
import me.happyclick.news.util.KEY_API
import me.happyclick.news.util.KEY_SOURCES
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    fun getNewsListingQuery(@Query("sources") sources: String,
                       @Query("apiKey") apiKey: String): Single<TopHeadlines>

    @GET("top-headlines?$KEY_SOURCES=$SOURCE_ID&$KEY_API=$API_KEY_NEWS_API")
    fun getNewsListingDefault(): Single<TopHeadlines>

    @GET("sources?$KEY_API=$API_KEY_NEWS_API")
    fun getSources(): Single<SourcesResult>
}