package net.botwithus

import java.io.File
import java.net.URL
import com.google.gson.Gson



// Should be able to pass GrandExchange("Cannonball") for a Println return of GE API data


data class ItemDetail(
    val item: Item
)

data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val members: String,
    val current: PriceInfo
)

data class PriceInfo(
    val trend: String,
    val price: String
)

fun GrandExchange(itemName: String) {
    // Load item database from JSON file
    val itemDBFile = File("ItemDB.json")
    val itemDBJson = itemDBFile.readText()
    val itemDB = Gson().fromJson(itemDBJson, Array<Item>::class.java)

    // Find the item in the database
    val item = itemDB.find { it.name.equals(itemName, ignoreCase = true) }

    if (item == null) {
        println("Item not found in the database.")
        return
    }

    // Make API call to Grand Exchange using item ID
    val apiUrl = "https://secure.runescape.com/m=itemdb_rs/api/catalogue/detail.json?item=${item.id}"

    try {
        val json = URL(apiUrl).readText()
        val itemDetail = Gson().fromJson(json, ItemDetail::class.java)
        val grandExchangeItem = itemDetail.item

        println("Item Name: ${grandExchangeItem.name}")
        println("Description: ${grandExchangeItem.description}")
        println("Is Members Only: ${if (grandExchangeItem.members.toBoolean()) "Yes" else "No"}")
        println("Current Price: ${grandExchangeItem.current.price}")
        println("Price Trend: ${grandExchangeItem.current.trend}")
    } catch (e: Exception) {
        println("Error fetching item information: ${e.message}")
    }
}

