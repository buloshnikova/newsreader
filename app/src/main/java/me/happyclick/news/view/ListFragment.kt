package me.happyclick.news.view


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*

import me.happyclick.news.R
import me.happyclick.news.model.Source
import me.happyclick.news.util.SharedPreferencesHelper
import me.happyclick.news.view.adapter.NewsListAdapter
import me.happyclick.news.view.bottomsheet.BottomSheetMenu
import me.happyclick.news.viewmodel.ListViewModel


class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val newsListAdapter =
        NewsListAdapter(arrayListOf())

    private lateinit var sources: List<Source>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        rv_news_listing.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsListAdapter

        }

        (activity!! as MainActivity).supportActionBar!!.title = SharedPreferencesHelper(context!!).getSourceName()

        refresh_layout.setOnRefreshListener {
            rv_news_listing.visibility = View.GONE
            list_error.visibility = View.GONE
            loading_view.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refresh_layout.isRefreshing = false
        }
        viewModel.fetchSources()

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.news.observe(viewLifecycleOwner, Observer { articles ->
            articles?.let {
                rv_news_listing.visibility = View.VISIBLE
                list_error.visibility = View.GONE
                newsListAdapter.updateMovieList(articles)
            }
        })

        viewModel.newsLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                list_error.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    list_error.visibility = View.GONE
                    rv_news_listing.visibility = View.GONE
                }
            }
        })

        viewModel.sources.observe(viewLifecycleOwner, Observer { allSources ->
            allSources?.let {
                sources = allSources
            }
        })

        viewModel.sourceUpdated.observe(viewLifecycleOwner, Observer { updatedSource ->
            updatedSource?.let {
                (activity!! as MainActivity).supportActionBar!!.title = it.name
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_chose_source -> {
                createSourceDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createSourceDialog() {

        BottomSheetMenu(context!!, sources, viewModel).show()
    }
}
