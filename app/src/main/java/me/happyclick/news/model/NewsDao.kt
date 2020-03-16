package me.happyclick.news.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDao {
    @Insert
    suspend fun insertAll(vararg articles: Article): List<Long>

    @Query("SELECT * FROM article")
    suspend fun getAllNews(): List<Article>

    @Query("SELECT * FROM article WHERE uuid = :url")
    suspend fun getNews(url: String): Article

    @Query("DELETE FROM article")
    suspend fun deleteAllNews()

    @Insert
    suspend fun insertAllSources(vararg sources: Source): List<Long>

    @Query("SELECT * FROM source")
    suspend fun getAllSources(): List<Source>

    @Query("DELETE FROM source")
    suspend fun deleteAllSources()

}