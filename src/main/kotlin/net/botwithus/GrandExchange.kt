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
import net.botwithus.rs3.input.KeyboardInput
import net.botwithus.rs3.script.Execution
import net.botwithus.rs3.script.LoopingScript
import net.botwithus.rs3.script.ScriptConsole
import net.botwithus.rs3.script.config.ScriptConfig
import net.botwithus.rs3.util.Regex
import java.util.*
import java.util.concurrent.Callable
import java.util.function.BiFunction
import java.util.regex.Pattern

val grandexchange = Area.Rectangular(
    Coordinate(3163, 3484, 0),
    Coordinate(3166, 3484, 0)
)

// Function to map characters to their key codes
fun getKeyCode(char: Char): Int? {
    return when (char) {
        'a', 'A' -> 65
        'b', 'B' -> 66
        'c', 'C' -> 67
        'd', 'D' -> 68
        'e', 'E' -> 69
        'f', 'F' -> 70
        'g', 'G' -> 71
        'h', 'H' -> 72
        'i', 'I' -> 73
        'j', 'J' -> 74
        'k', 'K' -> 75
        'l', 'L' -> 76
        'm', 'M' -> 77
        'n', 'N' -> 78
        'o', 'O' -> 79
        'p', 'P' -> 80
        'q', 'Q' -> 81
        'r', 'R' -> 82
        's', 'S' -> 83
        't', 'T' -> 84
        'u', 'U' -> 85
        'v', 'V' -> 86
        'w', 'W' -> 87
        'x', 'X' -> 88
        'y', 'Y' -> 89
        'z', 'Z' -> 90
        '0' -> 48
        '1' -> 49
        '2' -> 50
        '3' -> 51
        '4' -> 52
        '5' -> 53
        '6' -> 54
        '7' -> 55
        '8' -> 56
        '9' -> 57
        ' ' -> 32
        '+' -> 107
        else -> null
    }
}

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
        Zezimax.Logger.log("**GRAND EXCHANGE** Interacting with Clerk.")
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
            Execution.delay(Navi.random.nextLong(1000, 1700))
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** Sell button not found.")
        }
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Grand Exchange interface is not open.")
    }
}

fun interactWithBuyButton() {
    if (Interfaces.isOpen(105)) {
        val buyButton = ComponentQuery.newQuery(105)
            .componentIndex(15)
            .results()
            .firstOrNull()

        if (buyButton != null) {
            Execution.delay(Navi.random.nextLong(400, 1000))
            buyButton.interact()
            Execution.delay(Navi.random.nextLong(1000, 1700))
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** Buy button not found.")
        }
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Grand Exchange interface is not open.")
    }
}

fun typeItemName(Name: String) {
    Execution.delay(Navi.random.nextLong(400, 1000))
    // keycoded characters, since normal string keyboard input doesn't appear to work for GE interface
    for (char in Name) {
        val keyCode = getKeyCode(char)
        if (keyCode != null) {
            KeyboardInput.pressKey(keyCode)
            Execution.delay(Navi.random.nextLong(70, 200))
        }
    }

    Zezimax.Logger.log("**GRAND EXCHANGE** Typed out the item name: $Name")
}

fun interactWithGEItem(Name: String) {
    // Define the BiFunction for case-insensitive string comparison
    val stringEquals: BiFunction<String, CharSequence, Boolean> = BiFunction { s1, s2 -> s1.equals(s2.toString(), ignoreCase = true) }
    // Find the subcomponent with the text matching Name, this is always 1 subcomponent higher than the clickable object
    val geInventoryText = ComponentQuery.newQuery(105)
        .componentIndex(342)
        .text(Name, stringEquals)
        .results()
        .firstOrNull()

    // If the component is found, proceed to read the subcomponent int
    if (geInventoryText != null) {
        // Subtract 1 from the subcomponent index to get the interactable subcomponent
        val interactableSubComponentIndex = geInventoryText.subComponentIndex - 1

        // Find the interactable component using the calculated index
        val geInventory = ComponentQuery.newQuery(105)
            .componentIndex(342)
            .subComponentIndex(interactableSubComponentIndex)
            .results()
            .firstOrNull()

        Execution.delay(Navi.random.nextLong(400, 1000))

        if (geInventory != null) {
            geInventory.interact("Select")
            Zezimax.Logger.log("**GRAND EXCHANGE** Selected item...")
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** Couldn't select item...")
        }
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Couldn't find the item text...")
    }

    Execution.delay(Navi.random.nextLong(900, 1800))
}


fun typeQuantity(Quantity: String) {
    val geQt = ComponentQuery.newQuery(105)
        .componentIndex(237)
        .results()
        .firstOrNull()
    Execution.delay(Navi.random.nextLong(400, 1000))
    if (geQt != null) {
        geQt.interact()
    }
    Execution.delay(Navi.random.nextLong(900, 1800))
    KeyboardInput.enter(Quantity)
    Execution.delay(Navi.random.nextLong(300, 600))
    // press enter
    KeyboardInput.pressKey(13)
    Execution.delay(Navi.random.nextLong(400, 1000))
}

fun interactWithInventoryItem() {
    val playerInventory = ComponentQuery.newQuery(107)
        .componentIndex(7)
        .subComponentIndex(0) // first item in inventory
        .results()
        .firstOrNull()

    if (playerInventory != null) {
        Execution.delay(Navi.random.nextLong(400, 1000))
        playerInventory.interact("Offer")
        Execution.delay(Navi.random.nextLong(900, 1800))
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
            Execution.delay(Navi.random.nextLong(400, 1000))
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** 'All' button not found.")
        }
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Grand Exchange interface is not open.")
    }
}

fun minusPercent(percentage: Int) {
    if (Interfaces.isOpen(105)) {
        val minus5Button = ComponentQuery.newQuery(105)
            .componentIndex(294)
            .results()
            .firstOrNull()

        if (minus5Button != null) {
            val presses = percentage / 5
            for (i in 1..presses) {
                Execution.delay(Navi.random.nextLong(500, 800))
                minus5Button.interact()
            }
            Execution.delay(Navi.random.nextLong(500, 800))
            Zezimax.Logger.log("**GRAND EXCHANGE** applied -${presses * 5}%...")
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** -5% button not found.")
        }
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Grand Exchange interface is not open.")
    }
}

fun plusPercent(percentage: Int) {
    if (Interfaces.isOpen(105)) {
        val plus5Button = ComponentQuery.newQuery(105)
            .componentIndex(307)
            .results()
            .firstOrNull()

        if (plus5Button != null) {
            val presses = percentage / 5
            for (i in 1..presses) {
                Execution.delay(Navi.random.nextLong(500, 800))
                plus5Button.interact()
            }
            Execution.delay(Navi.random.nextLong(500, 800))
            Zezimax.Logger.log("**GRAND EXCHANGE** applied +${presses * 5}%...")
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** +5% button not found.")
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
            Execution.delay(Navi.random.nextLong(400, 1000))
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** Confirm Offer button not found.")
        }
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Grand Exchange interface is not open.")
    }
}


fun clickToInventoryButton() {
    val toInventoryButton = ComponentQuery.newQuery(651)
        .componentIndex(6)
        .results()
        .firstOrNull()

    if (toInventoryButton != null) {
        Execution.delay(Navi.random.nextLong(800, 1500))
        toInventoryButton.interact()
        Zezimax.Logger.log("**GRAND EXCHANGE** Sent to inventory...")
        Execution.delay(Navi.random.nextLong(800, 1500))
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** 'To Inventory' button not found.")
    }
}

fun clickToBankButton() {
    val toBankButton = ComponentQuery.newQuery(651)
        .componentIndex(14)
        .results()
        .firstOrNull()

    if (toBankButton != null) {
        Execution.delay(Navi.random.nextLong(800, 1500))
        toBankButton.interact()
        Zezimax.Logger.log("**GRAND EXCHANGE** Sent items to bank...")
        Execution.delay(Navi.random.nextLong(800, 1500))
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** 'To Bank' button not found.")
    }
}

fun closeGEInterface() {
    val closeButton = ComponentQuery.newQuery(1477)
        .componentIndex(716)
        .results()
        .firstOrNull()

    if (closeButton != null) {
        Execution.delay(Navi.random.nextLong(1000, 2500))
        KeyboardInput.pressKey(27)
        Zezimax.Logger.log("**GRAND EXCHANGE** Pressed Escape to close Interface...")
        Execution.delay(Navi.random.nextLong(1000, 2500))
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** Close button not found.")
    }
}

fun clickAbortOfferButton() {
    val abortButton = ComponentQuery.newQuery(105)
        .componentIndex(9)
        .results()
        .firstOrNull()

    if (abortButton != null) {
        Execution.delay(Navi.random.nextLong(400, 1000))
        abortButton.interact()
        Zezimax.Logger.log("**GRAND EXCHANGE** Aborting offer...")
    } else {
        Zezimax.Logger.log("**GRAND EXCHANGE** 'Abort Offer' button not found.")
    }
}





fun grandExchangeSell(ItemID: Int, Quantity: String, PercentageDecrease: String) {
    val player = Client.getLocalPlayer()
    if (player == null || Client.getGameState() != Client.GameState.LOGGED_IN) {
        Zezimax.Logger.log("Player not logged in. Delaying execution.")
        Execution.delay(Navi.random.nextLong(2500, 7500))
        return
    }

    if (!grandexchange.contains(player.coordinate)) {
        Navi.walkToGrandExchangeEntrance()
        if (Navi.walkToGrandExchange()) {
            Zezimax.Logger.log("**GRAND EXCHANGE** Arrived at the Grand Exchange.")
        } else {
            Zezimax.Logger.log("**GRAND EXCHANGE** Failed to walk to the Grand Exchange.")
            return
        }
    }

    Execution.delay(Navi.random.nextLong(1000, 2000))

    if (!Bank.isOpen()) {
        Bank.open()
        Execution.delay(Navi.random.nextLong(1500, 3000))
    }

    if (Bank.isOpen()) {
        Zezimax.Logger.log("**GRAND EXCHANGE** Depositing all items.")
        Execution.delay(Navi.random.nextLong(1000, 3000))
        Bank.depositAll()
        Execution.delay(Navi.random.nextLong(2000, 4000))
    }

    val item = ComponentQuery.newQuery(517)
        .item(ItemID)
        .results()
        .firstOrNull()

    if (item != null) {
        toggleWithdrawAsNotes()

        when (Quantity) {
            "all" -> {
                Zezimax.Logger.log("**GRAND EXCHANGE** Withdrawing all items for sale...")
                Bank.withdrawAll(ItemID)
                Execution.delay(Navi.random.nextLong(1200, 2300))
            }
            else -> {
                val quantityInt = Quantity.toIntOrNull()
                if (quantityInt != null) {
                    item.interact("Withdraw-X")
                    Zezimax.Logger.log("**GRAND EXCHANGE** Attempting Bank Withdraw for $ItemID...")
                    Execution.delay(Navi.random.nextLong(1300, 2000)) // Short delay before typing
                    GameInput.setTextInput(Quantity)
                    Execution.delay(Navi.random.nextLong(1600, 2600)) // Simulate delay after typing
                } else {
                    Zezimax.Logger.log("**GRAND EXCHANGE** Invalid quantity provided.")
                    return
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
    minusPercent(PercentageDecrease.toInt())
    Execution.delay(Navi.random.nextLong(800, 1900))
    clickConfirmOfferButton()
    Execution.delay(Navi.random.nextLong(3000, 5500))

    val editOfferBox = ComponentQuery.newQuery(105)
        .componentIndex(7)
        .subComponentIndex(5)
        .results()
        .firstOrNull()
        ?.text

    if (editOfferBox != null) {
        if (editOfferBox.contains("/")) {
            Execution.delay(Navi.random.nextLong(10000, 20000))
            if (editOfferBox.contains("/")) {
                clickAbortOfferButton()
                clickToBankButton()
                closeGEInterface()
                Zezimax.Logger.log("Couldn't sell item to Grand Exchange, Reinitializing...")
                Zezimax.botState = Zezimax.ZezimaxBotState.INITIALIZING
                return
            } else {
                clickToInventoryButton()
                closeGEInterface()
            }
        } else {
            clickToInventoryButton()
            closeGEInterface()
        }
    }
}




fun grandExchangeBuy(ItemName: String, Quantity: String, PercentageIncrease: String) {
    val player = Client.getLocalPlayer()
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
    interactWithGEClerk()
    Execution.delay(Navi.random.nextLong(1500, 2500))
    interactWithBuyButton()
    Execution.delay(Navi.random.nextLong(1000, 2500))
    typeItemName(ItemName)
    Execution.delay(Navi.random.nextLong(2000, 3000))
    interactWithGEItem(ItemName)
    Execution.delay(Navi.random.nextLong(1000, 2000))
    typeQuantity(Quantity)
    Execution.delay(Navi.random.nextLong(1000, 2000))
    plusPercent(PercentageIncrease.toInt())
    Execution.delay(Navi.random.nextLong(1000, 2000))
    clickConfirmOfferButton()
    Execution.delay(Navi.random.nextLong(3000, 5500))
    // Conditional logic checks if box is a green bar with no text, or has text containing a "/"
    val editOfferBox = ComponentQuery.newQuery(105)
        .componentIndex(7)
        .subComponentIndex(5)
        .results()
        .firstOrNull()
        ?.text

    if (editOfferBox != null) {
        if (editOfferBox.contains("/")) {
            Execution.delay(Navi.random.nextLong(10000, 20000)) // Wait 10-20 seconds
            if (editOfferBox.contains("/")) {
                clickAbortOfferButton()
                clickToInventoryButton()
                closeGEInterface()
                Zezimax.Logger.log("Couldn't purchase item from Grand Exchange, Reinitializing...")
                Zezimax.botState = Zezimax.ZezimaxBotState.INITIALIZING
                return
            } else {
                clickToBankButton()
                closeGEInterface()
            }
        } else {
            clickToBankButton()
            closeGEInterface()
        }
    }
}


/*
only tested instant sells
 */