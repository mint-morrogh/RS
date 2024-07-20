package net.botwithus

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.io.IOException

import com.google.gson.*
import java.lang.reflect.Type


class ItemDeserializer : JsonDeserializer<Item> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Item {
        val jsonObject = json.asJsonObject

        return Item(
            id = jsonObject.get("id").asString,
            name = jsonObject.get("name").asString,
            tradeable = jsonObject.get("tradeable").asBoolean,
            members = jsonObject.get("members").asBoolean,
            value = jsonObject.get("value").asInt,
            examine = jsonObject.get("examine").asString,
            actions_inventory = jsonObject.get("actions_inventory").asString,
            actions_worn = jsonObject.get("actions_worn").asString,
            actions_ground = jsonObject.get("actions_ground").asString
        )
    }
}

// Define the Item data class
data class Item(
    val id: String,
    val name: String,
    val tradeable: Boolean,
    val members: Boolean,
    val value: Int,
    val examine: String,
    val actions_inventory: String?,
    val actions_worn: String?,
    val actions_ground: String?
)


object Utilities {
    private val gson = GsonBuilder()
        .registerTypeAdapter(Item::class.java, ItemDeserializer())
        .create()

    fun readItemDB(): List<Item> {
        try {
            val inputStream = this::class.java.classLoader.getResourceAsStream("ItemDB.json")
                ?: throw IllegalArgumentException("ItemDB.json not found")

            val reader = InputStreamReader(inputStream)
            val itemType = object : TypeToken<List<Item>>() {}.type

            // Zezimax.Logger.log("Parsing ItemDB.json")
            val items: List<Item> = gson.fromJson(reader, itemType)

            return items
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            return emptyList()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    fun getNameById(id: Int): String? {
        // Zezimax.Logger.log("Fetching item name with ID: $id")
        val items = readItemDB()
        val item = items.find { it.id.toInt() == id }
        return if (item != null) {
            // Zezimax.Logger.log(item.name)
            item.name
        } else {
           // Zezimax.Logger.log("Item with ID $id not found")
            null
        }
    }

    fun getIdByName(name: String): String? {
        // Zezimax.Logger.log("Fetching item ID with name: $name")
        val items = readItemDB()
        val item = items.find { it.name.equals(name, ignoreCase = true) }
        return if (item != null) {
            // Zezimax.Logger.log(item.id)
            item.id
        } else {
            // Zezimax.Logger.log("Item with name '$name' not found")
            null
        }
    }



}
