package com.example.androidremoval.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidremoval.model.Item
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ListViewModel : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    private val api = Item.ApiClient.apiService

    fun fetchItems() {
        viewModelScope.launch {
            api.getItems().enqueue(object : Callback<List<Item>> {
                override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        Log.d("ListViewModel", "API response: $body")
                        _items.value = body ?: emptyList()
                    } else {
                        Log.e("ListViewModel", "API Error: ${response.errorBody()?.string()}")
                    }
                }


                override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                    Log.e("ListViewModel", "Error fetching items: ${t.message}")
                }
            })
        }
    }




    fun addItem(item: Item) {
        viewModelScope.launch {
            api.addItem(item).enqueue(object : Callback<Item> {
                override fun onResponse(call: Call<Item>, response: Response<Item>) {
                    if (response.isSuccessful) {
                        Log.d("ListViewModel", "Item added successfully")
                    } else {
                        handleErrorResponse(response.errorBody())
                    }
                }

                override fun onFailure(call: Call<Item>, t: Throwable) {
                    Log.e("ListViewModel", "Error adding item: ${t.message}")
                }
            })
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            api.updateItem(item.id, item).enqueue(object : Callback<Item> {
                override fun onResponse(call: Call<Item>, response: Response<Item>) {
                    if (response.isSuccessful) {
                        Log.d("ListViewModel", "Item updated successfully")
                    } else {
                        handleErrorResponse(response.errorBody())
                    }
                }

                override fun onFailure(call: Call<Item>, t: Throwable) {
                    Log.e("ListViewModel", "Error updating item: ${t.message}")
                }
            })
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            api.deleteItem(itemId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("ListViewModel", "Item deleted successfully")
                    } else {
                        handleErrorResponse(response.errorBody())
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("ListViewModel", "Error deleting item: ${t.message}")
                }
            })
        }
    }

    private fun handleErrorResponse(errorBody: ResponseBody?) {
        try {
            val jsonResponse = errorBody?.string()
            if (jsonResponse != null) {
                when {
                    jsonResponse.startsWith("[") -> parseJsonArray(jsonResponse)
                    jsonResponse.startsWith("{") -> parseJsonObject(jsonResponse)
                    else -> Log.e("ListViewModel", "Unexpected response format")
                }
            } else {
                Log.e("ListViewModel", "Error: Empty or null response body")
            }
        } catch (e: JSONException) {
            Log.e("ListViewModel", "Error parsing JSON: ${e.message}")
        } catch (e: Exception) {
            Log.e("ListViewModel", "Error handling response: ${e.message}")
        }
    }

    private fun parseJsonArray(jsonArrayString: String) {
        try {
            val jsonArray = JSONArray(jsonArrayString)
            val itemsList = mutableListOf<Item>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val item = Item.fromJsonString(jsonObject.toString())
                itemsList.add(item)
            }
            _items.value = itemsList
        } catch (e: JSONException) {
            Log.e("ListViewModel", "Error parsing JSON array: ${e.message}")
        }
    }

    private fun parseJsonObject(jsonObjectString: String) {
        try {
            val jsonObject = JSONObject(jsonObjectString)
            val item = Item.fromJsonString(jsonObject.toString())
            _items.value = listOf(item)
        } catch (e: JSONException) {
            Log.e("ListViewModel", "Error parsing JSON object: ${e.message}")
        }
    }
}
