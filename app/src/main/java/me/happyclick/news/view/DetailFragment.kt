package me.happyclick.news.view

import android.annotation.SuppressLint
import android.app.Activity
import android.net.http.SslError
import android.os.Bundle
import android.view.*
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_detail.*

import me.happyclick.news.R
import me.happyclick.news.databinding.FragmentDetailBinding
import me.happyclick.news.model.Article
import me.happyclick.news.viewmodel.DetailViewModel

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    private val MAX_PROGRESS = 100

    private var url = ""
    private var title = ""

    private lateinit var dataBinding: FragmentDetailBinding

    private var currentArticle: Article? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            url = DetailFragmentArgs.fromBundle(it).url
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetch()
        observeViewModel()

        initWebView()
        setWebClient()
        loadUrl(url)
        onKeyDown()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        web_view.settings.javaScriptEnabled = true
        web_view.settings.loadWithOverviewMode = true
        web_view.settings.useWideViewPort = true
        web_view.settings.domStorageEnabled = true
        web_view.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }
    }

    private fun setWebClient() {
        web_view.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                progress_bar_horizontal?.let { pbar ->
                    pbar.progress = newProgress
                    if (newProgress < MAX_PROGRESS && pbar.visibility == ProgressBar.GONE) {
                        pbar.visibility = ProgressBar.VISIBLE
                    }
                    if (newProgress == MAX_PROGRESS) {
                        pbar.visibility = ProgressBar.GONE
                    }
                }
            }
        }
    }

    private fun loadUrl(pageUrl: String) {
        web_view.loadUrl(pageUrl)
    }

    fun onKeyDown(){
        web_view.setOnKeyListener(object: View.OnKeyListener {

            override fun onKey(v: View?, p1: Int, event: KeyEvent?): Boolean {
                event?.let {
                    if (event.keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && web_view.canGoBack()
                    ) {
                        web_view.goBack()
                        return true;
                    }
                    else return false
                }
                return true;
            }
        });
    }

    fun observeViewModel() {
        viewModel.sourceTitle.observe(viewLifecycleOwner, Observer { title ->
            title?.let {
                (activity!! as MainActivity).supportActionBar!!.title = it
            }
        })
    }
}
