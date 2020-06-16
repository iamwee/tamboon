package dev.iamwee.android.tamboon.data

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharityInfo(
    val id: Long,
    val name: String,
    val imageUrl: String
): Parcelable {

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<CharityInfo>() {
            override fun areItemsTheSame(oldItem: CharityInfo, newItem: CharityInfo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CharityInfo, newItem: CharityInfo): Boolean {
                return oldItem == newItem
            }

        }
    }
}