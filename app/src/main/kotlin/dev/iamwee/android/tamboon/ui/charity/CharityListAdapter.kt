package dev.iamwee.android.tamboon.ui.charity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.iamwee.android.tamboon.R
import dev.iamwee.android.tamboon.data.CharityInfo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_charity_list.*

class CharityListAdapter(
    private val onItemClicked: (CharityInfo) -> Unit
) : ListAdapter<CharityInfo, CharityViewHolder>(CharityInfo.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CharityViewHolder(parent)

    override fun onBindViewHolder(holder: CharityViewHolder, position: Int) = holder.bind(getItem(position), onItemClicked)

}

class CharityViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    constructor(parent: ViewGroup): this (
        LayoutInflater.from(parent.context).inflate(R.layout.item_charity_list, parent, false)
    )

    fun bind(item: CharityInfo, onItemClicked: (CharityInfo) -> Unit) {
        Glide.with(itemView)
            .load(item.imageUrl)
            .into(imageViewCharityCover)
        textViewCharityName.text = item.name
        itemView.setOnClickListener { onItemClicked(item) }
    }

}