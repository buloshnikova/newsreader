package me.happyclick.news.view.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_menu.view.*
import me.happyclick.news.R
import me.happyclick.news.model.Source
import me.happyclick.news.viewmodel.ListViewModel

class BottomSheetMenu(private val context: Context,
                      private val items: List<Source>,
                      private val viewModel: ListViewModel) {

    private val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_menu, null)
        bottomSheetDialog.setContentView(view)

        with(view) {
            bottom_sheet_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            bottom_sheet_recycler.adapter = BottomSheetMenuAdapter(items, viewModel, bottomSheetDialog)
        }
        bottomSheetDialog.setTitle("Choose a news source provider")
    }

    fun show() {
        bottomSheetDialog.show()
    }

    fun dismiss() {
        bottomSheetDialog.dismiss()
    }
}