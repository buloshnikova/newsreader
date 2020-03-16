package me.happyclick.news.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "article")
data class Article (

    @SerializedName("author")
    val voteCount: String?,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("urlToImage")
    val urlToImage: String?,

    @SerializedName("publishedAt")
    val publishedAt: String?,

    @SerializedName("content")
    val content: String?

    //@SerializedName("source")
    //val source: Source?

) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}


@Entity(tableName = "source")
data class Source (
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?
){
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}

@Entity
data class TopHeadlines (

    @SerializedName("total_results")
    val totalResults: Int?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("articles")
    val articles: List<Article>
)

@Entity
data class SourcesResult (
    @SerializedName("status")
    val status: String?,

    @SerializedName("sources")
    val sourcesList: List<Source>
)
