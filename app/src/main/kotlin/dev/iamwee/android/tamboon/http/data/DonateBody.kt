package dev.iamwee.android.tamboon.http.data
import com.google.gson.annotations.SerializedName

data class DonateBody(
    @SerializedName("amount")
    val amount: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("token")
    val token: String
)