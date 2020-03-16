package me.happyclick.news.model

import io.reactivex.Single
import me.happyclick.news.util.API_KEY_NEWS_API
import me.happyclick.news.util.BASE_URL_NEWS_API
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiService {

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL_NEWS_API)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(NewsApi::class.java)

    fun getNews(source: String) : Single<TopHeadlines> {
        return api.getNewsListingQuery(source, API_KEY_NEWS_API)
    }

    fun getNewsDefault() : Single<TopHeadlines> {
        return api.getNewsListingDefault()
    }

    fun getSources() : Single<SourcesResult> {
        return api.getSources()
    }
}