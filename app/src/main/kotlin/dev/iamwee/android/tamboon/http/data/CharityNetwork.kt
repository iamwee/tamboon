package dev.iamwee.android.tamboon.http.data
import com.google.gson.annotations.SerializedName

data class CharityNetwork(
    @SerializedName("id")
    val id: Long,
    @SerializedName("logo_url")
    val logoUrl: String? = null,
    @SerializedName("name")
    val name: String
)