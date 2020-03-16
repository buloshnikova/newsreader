package me.happyclick.news.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import me.happyclick.news.R
import me.happyclick.news.databinding.LayoutNewsItemBinding
import me.happyclick.news.model.Article
import me.happyclick.news.view.listener.ItemClickListener
import me.happyclick.news.view.ListFragmentDirections

class NewsListAdapter(val newsList: ArrayList<Article>): RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>(),
    ItemClickListener {

    fun updateMovieList(newMoviesList: List<Article>) {
        newsList.clear()
        newsList.addAll(newMoviesList)
        notifyDataSetChanged()
    }

    class NewsViewHolder(var view: LayoutNewsItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<LayoutNewsItemBinding>(inflater, R.layout.layout_news_item, parent, false)

        return NewsViewHolder(
            view
        )
    }

    override fun getItemCount() = newsList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.view.item = newsList[position]
        holder.view.listener = this
    }

    override fun onItemClicked(v: View) {
        val action =
            ListFragmentDirections.actionGotoDetailFragment()
            action.url = v.tag.toString()
            Navigation.findNavController(v).navigate(action)
    }
}