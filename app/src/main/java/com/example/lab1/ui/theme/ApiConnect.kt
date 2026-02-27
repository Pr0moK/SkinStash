package com.example.lab1.ui.theme

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLEncoder

@Serializable
data class ApiResponse(
    val data: List<Listing>
)

@Serializable
data class Listing(
    val price: Float,
    val item: Item
)


@Serializable
data class  Item(
    val quality: Int? = null,
    val market_hash_name: String,
    val icon_url: String

)
class ApiConnect {


    suspend fun ConnecToApi(ItemName: String, ItemQuality: String,ItemCategory: String) = withContext(Dispatchers.IO) {
        try {
            Log.i("API", "NAZWA ITEMU ${ItemName}")


            val ApiUrl = "https://csfloat.com/api/v1/listings"
            val encodedName = URLEncoder.encode(
                "${ItemName} (${ItemQuality})",
                "UTF-8"
            )


            var finalUrl =
                "$ApiUrl?market_hash_name=$encodedName&limit=1&category=${ItemCategory}&type=buy_now&sort_by=lowest_price"

            if(ItemName.contains("Case")){
                finalUrl = "$ApiUrl?market_hash_name=${ItemName.replace(" ", "+")}&type=buy_now&sort_by=lowest_price"
            }


            val url: URL = URI.create(finalUrl).toURL()

            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty(
                "Authorization",
                "${Config.getApi()}"
            )
            Log.i("API", "${finalUrl}")
            connection.requestMethod = "GET"

            if (connection.responseCode == 200) {
                val reader: BufferedReader =
                    BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                val response = StringBuilder()

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                val json = Json { ignoreUnknownKeys = true }
                val apiResponse = json.decodeFromString<ApiResponse>(response.toString())

                if(!apiResponse.data.isEmpty()){
                    val listing = apiResponse.data.first()
                    Log.i("API", "udalo sie ")
                    Log.i("API", "${listing.price / 100}, ${finalUrl}")
                    return@withContext listing.price / 100
                }else {
                    connection.disconnect()
                    Log.i("API", "NIE UDALO SIE ITD")
                    val testname = URLEncoder.encode(ItemName, "UTF-8")
                    finalUrl = "$ApiUrl?market_hash_name=${testname.replace(" ", "+")}&type=buy_now&sort_by=lowest_price"
                    val url: URL = URI.create(finalUrl).toURL()

                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

                    connection.setRequestProperty(
                        "Authorization",
                        "${Config.getApi()}"
                    )

                    connection.requestMethod = "GET"
                    Log.i("API", "${connection.responseCode}")
                    if (connection.responseCode == 200) {
                        Log.i("API", "${finalUrl}")

                        val reader: BufferedReader =
                            BufferedReader(InputStreamReader(connection.inputStream))
                        var line: String?
                        val response = StringBuilder()

                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        val json = Json { ignoreUnknownKeys = true }
                        val apiResponse = json.decodeFromString<ApiResponse>(response.toString())

                        if(!apiResponse.data.isEmpty()){
                            val listing = apiResponse.data.first()
                            Log.i("API", "udalo sie ")
                            Log.i("API", "${listing.price / 100}, ${finalUrl}")
                            return@withContext listing.price / 100
                        }else {
                            return@withContext 0
                        }
                    }


                }
            } else {
                Log.i("API", "Nie udalo sie ${connection.responseCode}")
            }
        } catch (e : IOException){
            Log.i("API", "Wystapil blad ${e}")
            return@withContext 0
        }
    }

    suspend fun RefreshData(itemName: String, quality: String, category: String): Any {
        return ConnecToApi(itemName, quality, category)
    }


    suspend fun GetImage(ItemName: String, ItemQuality: String) = withContext(Dispatchers.IO) {
        try {
            Log.i("API", "NAZWA ITEMU ${ItemName}")


            val ApiUrl = "https://csfloat.com/api/v1/listings"
            val encodedName = URLEncoder.encode(
                "${ItemName} (${ItemQuality})",
                "UTF-8"
            )


            var finalUrl =
                "$ApiUrl?market_hash_name=$encodedName&limit=1&type=buy_now&sort_by=lowest_price"

            if(ItemName.contains("Case")){
                finalUrl = "$ApiUrl?market_hash_name=${ItemName.replace(" ", "+")}&type=buy_now&sort_by=lowest_price"
            }


            val url: URL = URI.create(finalUrl).toURL()

            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty(
                "Authorization",
                "${Config.getApi()}"
            )
            Log.i("API", "${finalUrl}")
            connection.requestMethod = "GET"

            if (connection.responseCode == 200) {
                val reader: BufferedReader =
                    BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                val response = StringBuilder()

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                val json = Json { ignoreUnknownKeys = true }
                val apiResponse = json.decodeFromString<ApiResponse>(response.toString())

                if(!apiResponse.data.isEmpty()){
                    val listing = apiResponse.data.first()
                    Log.i("API", "udalo sie ")
                    Log.i("API", "${listing.price / 100}, ${finalUrl}")
                    return@withContext listing.item.icon_url
                }else {
                    connection.disconnect()
                    Log.i("API", "NIE UDALO SIE ITD")
                    val testname = URLEncoder.encode(ItemName, "UTF-8")
                    finalUrl = "$ApiUrl?market_hash_name=${testname.replace(" ", "+")}&type=buy_now&sort_by=lowest_price"
                    val url: URL = URI.create(finalUrl).toURL()

                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

                    connection.setRequestProperty(
                        "Authorization",
                        "${Config.getApi()}"
                    )

                    connection.requestMethod = "GET"
                    Log.i("API", "${connection.responseCode}")
                    if (connection.responseCode == 200) {
                        Log.i("API", "${finalUrl}")

                        val reader: BufferedReader =
                            BufferedReader(InputStreamReader(connection.inputStream))
                        var line: String?
                        val response = StringBuilder()

                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        val json = Json { ignoreUnknownKeys = true }
                        val apiResponse = json.decodeFromString<ApiResponse>(response.toString())

                        if(!apiResponse.data.isEmpty()){
                            val listing = apiResponse.data.first()
                            Log.i("API", "udalo sie ")
                            Log.i("API", "${listing.price / 100}, ${finalUrl}")
                            return@withContext listing.item.icon_url
                        }else {
                            return@withContext null
                        }
                    }


                }
            } else {
                Log.i("API", "Nie udalo sie ${connection.responseCode}")
            }
        } catch (e : IOException){
            Log.i("API", "Wystapil blad ${e}")
            return@withContext null
        }
    }
}