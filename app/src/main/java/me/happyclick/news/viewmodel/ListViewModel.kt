package me.happyclick.news.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import me.happyclick.news.model.*
import me.happyclick.news.util.SOURCE_ID
import me.happyclick.news.util.SharedPreferencesHelper

class ListViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper =
        SharedPreferencesHelper(getApplication())
    private val newsService = NewsApiService()
    private val disposable = CompositeDisposable()
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L // 5 minutes
    private var refreshTimeSources = 30 * 60 * 1000 * 1000 * 1000L // 30 minutes


    val news = MutableLiveData<List<Article>>()
    val newsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val sources = MutableLiveData<List<Source>>()
    val sourceUpdated = MutableLiveData<Source>()

    fun refresh() {
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromDatabase() {
        loading.value = true

        launch {
            val news = NewsDatabase(getApplication())
                .newsDao().getAllNews()
            newsRetrieved(news)
            Toast.makeText(getApplication(), "Retrieved from database", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            newsService.getNews(prefHelper.getSource() ?: SOURCE_ID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TopHeadlines>() {
                    override fun onSuccess(newsList: TopHeadlines) {
                        storeNewsLocally(newsList.articles)
                        Toast.makeText(getApplication(), "Retrieved from remote", Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onError(e: Throwable) {
                        newsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun newsRetrieved(articleList: List<Article>) {
        news.value = articleList
        newsLoadError.value = false
        loading.value = false
    }

    private fun storeNewsLocally(list: List<Article>) {

        launch {
            val dao = NewsDatabase(getApplication()).newsDao()
            dao.deleteAllNews()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            newsRetrieved(list)
        }

        prefHelper.saveUpdateTime(System.nanoTime())
    }

    fun fetchSources() {

        val updateTimeS = prefHelper.getUpdateTimeSources()
        if (updateTimeS != null && updateTimeS != 0L && System.nanoTime() - updateTimeS < refreshTimeSources) {
            fetchSourcesFromDatabase()
        } else {
            fetchSourcesFromRemote()
        }
    }

    private fun fetchSourcesFromDatabase() {
        launch {
            val src = NewsDatabase(getApplication())
                .newsDao().getAllSources()
            sourcesRetrieved(src)
        }
    }

    private fun fetchSourcesFromRemote() {
        disposable.add(
            newsService.getSources()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SourcesResult>() {
                    override fun onSuccess(sourceList: SourcesResult) {
                        storeSourcesLocally(sourceList.sourcesList)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun storeSourcesLocally(list: List<Source>) {

        launch {
            val dao = NewsDatabase(getApplication()).newsDao()
            dao.deleteAllSources()
            val result = dao.insertAllSources(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            sourcesRetrieved(list)
        }

        prefHelper.saveUpdateTimeSources(System.nanoTime())
    }

    private fun sourcesRetrieved(sourceList: List<Source>) {
        sources.value = sourceList
    }

    fun updateSource(source: Source) {
        prefHelper.saveSource(source.id!!, source.name!!)
        refreshBypassCache()
        sourceUpdated.value = source
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}