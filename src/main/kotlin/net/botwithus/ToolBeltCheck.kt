package net.botwithus

import net.botwithus.rs3.game.js5.types.configs.ConfigManager
import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.internal.scripts.ScriptDefinition
import net.botwithus.rs3.events.impl.InventoryUpdateEvent
import net.botwithus.rs3.events.impl.SkillUpdateEvent
import net.botwithus.rs3.game.Area
import net.botwithus.rs3.game.Client
import net.botwithus.rs3.game.Coordinate
import net.botwithus.rs3.game.hud.interfaces.Interfaces
import net.botwithus.rs3.game.minimenu.MiniMenu
import net.botwithus.rs3.game.minimenu.actions.SelectableAction
import net.botwithus.rs3.game.movement.Movement
import net.botwithus.rs3.game.movement.NavPath
import net.botwithus.rs3.game.movement.TraverseEvent
import net.botwithus.rs3.game.queries.builders.animations.SpotAnimationQuery
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery
import net.botwithus.rs3.game.scene.entities.animation.SpotAnimation
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer
import net.botwithus.rs3.game.scene.entities.`object`.SceneObject
import net.botwithus.rs3.game.skills.Skills
import net.botwithus.rs3.game.vars.VarManager
import net.botwithus.rs3.imgui.NativeInteger
import net.botwithus.rs3.input.GameInput
import net.botwithus.rs3.script.Execution
import net.botwithus.rs3.script.LoopingScript
import net.botwithus.rs3.script.ScriptConsole
import net.botwithus.rs3.script.config.ScriptConfig
import net.botwithus.rs3.util.Regex
import java.util.*
import java.util.concurrent.Callable
import java.util.regex.Pattern


var hatchetId = ConfigManager.getEnumType(6397)!!.getOutput(VarManager.getVarbitValue(18522)) as Int
var hatchetName:kotlin.String? = ConfigManager.getItemType(hatchetId)?.getName()
var pickaxeId:kotlin.Int = ConfigManager.getEnumType(2433)?.getOutput(VarManager.getVarbitValue(43044)) as kotlin.Int
var pickaxeName:kotlin.String? = ConfigManager.getItemType(pickaxeId)?.getName()

fun ToolBelt() {
    val coinPouch = getCoinPouchAmount()

    if (coinPouch >= 100000) {

        val hatchetList = listOf(
            "Rune hatchet", "Sacred clay hatchet", "Volatile clay hatchet", "Dragon hatchet",
            "Inferno adze", "Crystal hatchet", "Imcando hatchet"
        )

        val pickaxeList = listOf(
            "Rune pickaxe", "Gilded rune pickaxe", "Rune pickaxe + 1", "Rune pickaxe + 2",
            "Rune pickaxe + 3", "Inferno adze", "Orikalkum pickaxe", "Orikalkum pickaxe + 1",
            "Orikalkum pickaxe + 2", "Orikalkum pickaxe + 3", "Dragon pickaxe",
            "Gilded dragon pickaxe", "Necronium pickaxe", "Necronium pickaxe + 1",
            "Necronium pickaxe + 2", "Necronium pickaxe + 3", "Necronium pickaxe + 4",
            "Crystal pickaxe", "Bane pickaxe", "Bane pickaxe + 1", "Bane pickaxe + 2",
            "Bane pickaxe + 3", "Bane pickaxe + 4", "Imcando pickaxe", "Elder rune pickaxe",
            "Elder rune pickaxe + 1", "Elder rune pickaxe + 2", "Elder rune pickaxe + 3",
            "Elder rune pickaxe + 4", "Elder rune pickaxe + 5", "Pickaxe of Earth and Song"
        )

        if (!hatchetList.contains(hatchetName) && coinPouch >= 100000) {
            grandExchangeBuy("Rune hatchet", "1")
            Bank.open()
            Bank.withdrawAll(1359)
            equipItemToToolbelt("Rune hatchet")
        }

        if (!pickaxeList.contains(pickaxeName) && coinPouch >= 100000) {
            grandExchangeBuy("Rune pickaxe", "1")
            Bank.open()
            Bank.withdrawAll(45548)
            equipItemToToolbelt("Rune pickaxe")
        }
    }
}

fun getCoinPouchAmount(): Int {
    // Replace with the actual implementation to get the coin pouch amount
    return 150000 // Example value
}

fun equipItemToToolbelt(itemName: String) {
    // Implement equipping item to toolbelt logic
    // interact("Add to tool belt")
    Zezimax.Logger.log("Equipping $itemName to toolbelt.")
}