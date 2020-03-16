package me.happyclick.news.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import me.happyclick.news.model.Article
import me.happyclick.news.model.NewsDatabase
import me.happyclick.news.util.SharedPreferencesHelper

class DetailViewModel(application: Application): BaseViewModel(application) {

    private val disposable = CompositeDisposable()
    private var prefHelper =
        SharedPreferencesHelper(getApplication())

    val sourceTitle = MutableLiveData<String>()

    fun fetch() {
        val title = prefHelper.getSourceName()
        sourceTitle.value = title
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}