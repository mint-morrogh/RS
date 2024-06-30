package net.botwithus

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

val grandexchange = Area.Rectangular(
    Coordinate(3163, 3484, 0),
    Coordinate(3166, 3484, 0)
)


fun toggleWithdrawAsNotes() {
    val noteButton = ComponentQuery.newQuery(517)
        .componentIndex(127)
        .results()
        .firstOrNull()

    if (noteButton != null) {
        Execution.delay(Navi.random.nextLong(400, 1000))
        noteButton.interact()
        Zezimax.Logger.log("**GRAND EXCHANGE** Toggled to withdraw items as notes.")
        Execution.delay(Navi.random.nextLong(400, 1000))
    }
}

fun interactWithGEClerk() {
    val clerk: Npc? = NpcQuery.newQuery()
        .name("Grand Exchange clerk")
        .results()
        .nearest()

    if (clerk != null) {
        Execution.delay(Navi.random.nextLong(500, 1000))
        clerk.interact("Exchange")
        Zezimax.Logger.log("**GRAND EXCHANGE** Interacting with Grand Exchange Clerk.")
        Execution.delay(Navi.random.nextLong(1000, 2000))
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** No Grand Exchange Clerk found.")
    }
}

fun interactWithSellButton() {
    if (Interfaces.isOpen(105)) {
        val sellButton = ComponentQuery.newQuery(105)
            .componentIndex(21)
            .results()
            .firstOrNull()

        if (sellButton != null) {
            Execution.delay(Navi.random.nextLong(400, 1000))
            sellButton.interact()
            Zezimax.Logger.log("**GRAND EXCHANGE** Clicked on the sell button in the first slot.")
            Execution.delay(Navi.random.nextLong(1000, 2000))
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** Sell button not found.")
        }
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Grand Exchange interface is not open.")
    }
}

fun interactWithInventoryItem() {
    val playerInventory = ComponentQuery.newQuery(107)
        .componentIndex(7)
        .subComponentIndex(0)
        .results()
        .firstOrNull()

    if (playerInventory != null) {
        Execution.delay(Navi.random.nextLong(400, 1000))
        playerInventory.interact("Offer")
        Zezimax.Logger.log("**GRAND EXCHANGE** Clicked on the item in the inventory.")
        Execution.delay(Navi.random.nextLong(1500, 2500))
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Item not found in the inventory.")
    }
}

fun clickAllButton() {
    if (Interfaces.isOpen(105)) {
        val allButton = ComponentQuery.newQuery(105)
            .componentIndex(268)
            .results()
            .firstOrNull()

        if (allButton != null) {
            Execution.delay(Navi.random.nextLong(400, 1000))
            allButton.interact()
            Zezimax.Logger.log("**GRAND EXCHANGE** Clicked on the 'All' button.")
            Execution.delay(Navi.random.nextLong(1000, 2000))
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** 'All' button not found.")
        }
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Grand Exchange interface is not open.")
    }
}

fun minus10Percent() {
    if (Interfaces.isOpen(105)) {
        val minus5Button = ComponentQuery.newQuery(105)
            .componentIndex(294)
            .results()
            .firstOrNull()

        if (minus5Button != null) {
            Execution.delay(Navi.random.nextLong(500, 800))
            minus5Button.interact()
            Zezimax.Logger.log("**GRAND EXCHANGE** Clicked on the -5% button.")
            Execution.delay(Navi.random.nextLong(500, 800))
            minus5Button.interact()
            Zezimax.Logger.log("**GRAND EXCHANGE** Clicked on the -5% button again.")
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** -5% button not found.")
        }
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Grand Exchange interface is not open.")
    }
}

fun clickConfirmOfferButton() {
    if (Interfaces.isOpen(105)) {
        val confirmOfferButton = ComponentQuery.newQuery(105)
            .componentIndex(325)
            .results()
            .firstOrNull()

        if (confirmOfferButton != null) {
            Execution.delay(Navi.random.nextLong(400, 1000))
            confirmOfferButton.interact()
            Zezimax.Logger.log("**GRAND EXCHANGE** Clicked on the Confirm Offer button.")
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** Confirm Offer button not found.")
        }
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Grand Exchange interface is not open.")
    }
}







fun grandExchangeSell(ItemID: Int, Quantity: String) {
    val player = Client.getLocalPlayer()
    Zezimax.Logger.log("**GRAND EXCHANGE** Walking to the Grand Exchange.")
    if (player == null || Client.getGameState() != Client.GameState.LOGGED_IN) {
        Zezimax.Logger.log("Player not logged in. Delaying execution.")
        Execution.delay(Navi.random.nextLong(2500, 7500))
        return
    }
    if (!grandexchange.contains(player.coordinate)) {
        Navi.walkToGrandExchangeEntrance() // so it doesn't tele to edgeville if not member account, will get stuck trying agility
        if (Navi.walkToGrandExchange()) {
            Zezimax.Logger.log("**GRAND EXCHANGE** Arrived at the Grand Exchange.")
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** Failed to walk to the Grand Exchange.")
        }
    }
    Zezimax.Logger.log("**GRAND EXCHANGE** Player is at the Grand Exchange...")
    Execution.delay(Navi.random.nextLong(1000, 2000))
    if (!Bank.isOpen()) {
        // Open the bank
        Bank.open()
        Execution.delay(Navi.random.nextLong(1500, 3000))
    }
    // Deposit all items into the bank
    if (Bank.isOpen()) {
        Zezimax.Logger.log("**GRAND EXCHANGE** Depositing all items.")
        Execution.delay(Navi.random.nextLong(1000, 3000))
        Bank.depositAll()
        Execution.delay(Navi.random.nextLong(2000, 4000))
    }
    if (ItemID != null) {
        toggleWithdrawAsNotes()

        when (Quantity) {
            "all" -> {
                Zezimax.Logger.log("**GRAND EXCHANGE** Withdrawing all items as notes.")
                Bank.withdrawAll(ItemID)
                Execution.delay(Navi.random.nextLong(1200, 2300))
            }
            else -> {
                val quantityInt = Quantity.toIntOrNull()
                if (quantityInt != null) {
                    Zezimax.Logger.log("**GRAND EXCHANGE** Withdrawing $quantityInt items as notes.")

                    val item = ComponentQuery.newQuery(517)
                        .item(ItemID)
                        .results()
                        .firstOrNull()

                    if (item != null) {
                        item.interact("Withdraw-X")
                        Zezimax.Logger.log("**GRAND EXCHANGE** Attempting Bank Withdraw for $ItemID")
                        Execution.delay(Navi.random.nextLong(1300, 2000)) // Short delay before typing
                        GameInput.setTextInput(Quantity)
                        Execution.delay(Navi.random.nextLong(1600, 2600)) // Simulate delay after typing
                    }
                } else {
                    Zezimax.Logger.log("**GRAND EXCHANGE** Invalid quantity provided.")
                }
            }
        }

        Execution.delay(Navi.random.nextLong(1500, 3000))
        Bank.close()
        Execution.delay(Navi.random.nextLong(1000, 2000))
    }
    interactWithGEClerk()
    Execution.delay(Navi.random.nextLong(1500, 2500))
    interactWithSellButton()
    Execution.delay(Navi.random.nextLong(1000, 2000))
    interactWithInventoryItem()
    Execution.delay(Navi.random.nextLong(1000, 2500))
    clickAllButton()
    Execution.delay(Navi.random.nextLong(800, 1900))
    minus10Percent()
    Execution.delay(Navi.random.nextLong(800, 1900))
    clickConfirmOfferButton()
    Execution.delay(Navi.random.nextLong(3000, 5500))








    /*

if item sold in 4 seconds {
Click to inventory
Close GE interface
}
if item not sold in 4 seconds {
Wait 10-20 seconds
    if item sold {
    click to inventory
    Close GE interface
    }
    if item not not sold {
    click abort offer button
    Click to bank button
    Close GE interface
    }
}
     */


}
