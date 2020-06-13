package dev.iamwee.android.tamboon.http.data
import com.google.gson.annotations.SerializedName
import dev.iamwee.android.tamboon.data.CharityInfo

data class CharityNetwork(
    @SerializedName("id")
    val id: Long,
    @SerializedName("logo_url")
    val logoUrl: String? = null,
    @SerializedName("name")
    val name: String
)

val CharityNetwork.asInfo: CharityInfo
    get() = CharityInfo(
        id = id,
        imageUrl = logoUrl ?: "",
        name = name
    )