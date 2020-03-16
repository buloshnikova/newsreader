package me.happyclick.news.view.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.item_bottom_sheet_menu.view.*
import me.happyclick.news.R
import me.happyclick.news.model.Source
import me.happyclick.news.viewmodel.ListViewModel


class BottomSheetMenuAdapter(private val items: List<Source>, val viewModel: ListViewModel, val dialog: BottomSheetDialog) : RecyclerView.Adapter<BottomSheetMenuAdapter.BottomSheetMenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetMenuViewHolder {
        return BottomSheetMenuViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bottom_sheet_menu, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BottomSheetMenuViewHolder, position: Int) {
        holder.bind(items[position])
        holder.view.setOnClickListener {
            viewModel.updateSource(items[position])
            dialog.dismiss()
        }
    }

    class BottomSheetMenuViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Source) {
            with(view) {
                bottom_menu_title.text = item.name
            }
        }
    }

}