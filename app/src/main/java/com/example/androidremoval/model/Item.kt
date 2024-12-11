package com.example.androidremoval.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class Item(
    val id: String,
    val name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        name = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item = Item(parcel)
        override fun newArray(size: Int): Array<Item?> = arrayOfNulls(size)

        fun fromJsonString(jsonString: String): Item {
            return GsonBuilder().setLenient().create().fromJson(jsonString, Item::class.java)
        }


    }


    interface ApiService {
        @GET("/items")
        fun getItems(): Call<List<Item>>

        @POST("/items")
        fun addItem(@Body item: Item): Call<Item>

        @PUT("/items/{id}")
        fun updateItem(@Path("id") id: String, @Body item: Item): Call<Item>

        @DELETE("/items/{id}")
        fun deleteItem(@Path("id") id: String): Call<Void>
    }

    object ApiClient {
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://cac772a6cb8c09493348.free.beeceptor.com/api/items/")
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLenient().create()
                    )
                )
                .build()
        }

        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}
