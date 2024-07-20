package net.botwithus

import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.rs3.game.hud.interfaces.Component
import net.botwithus.rs3.game.hud.interfaces.Interfaces
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery
import net.botwithus.rs3.game.scene.entities.`object`.SceneObject
import net.botwithus.rs3.game.skills.Skills
import net.botwithus.rs3.input.GameInput
import net.botwithus.rs3.script.Execution
import java.util.*



fun withdrawSmithingOreSupplies(vararg items: Pair<Int, Int?>) {
    if (Bank.isOpen()) {
        val noteButton = ComponentQuery.newQuery(517)
            .componentIndex(127)
            .results()
            .firstOrNull()

        if (noteButton != null) {
            Execution.delay(Navi.random.nextLong(400, 1000))
            noteButton.interact()  // This interacts with the note button
            Zezimax.Logger.log("Toggled to withdraw items as notes.")
            Execution.delay(Navi.random.nextLong(400, 1000))
        }

        for ((itemId, quantity) in items) {
            val itemComponent = ComponentQuery.newQuery(517)
                .item(itemId)
                .results()
                .firstOrNull()

            val smithingGetName = Utilities.getNameById(itemId)

            if (itemComponent != null) {
                if (quantity == null) {
                    // Withdraw all instances of the item
                    val success = itemComponent.interact("Withdraw-All")
                    Zezimax.Logger.log("Attempting to withdraw all instances of item ID: $smithingGetName")

                    if (success) {
                        Zezimax.Logger.log("Successfully withdrew all instances of item ID: $smithingGetName.")
                        Execution.delay(Navi.random.nextLong(1300, 2000))
                    } else {
                        Zezimax.Logger.log("Failed to withdraw item $smithingGetName")
                        Execution.delay(Navi.random.nextLong(1300, 2000))
                    }
                } else {
                    // Withdraw a specific quantity
                    val success = itemComponent.interact("Withdraw-X")
                    Zezimax.Logger.log("Attempting to withdraw $quantity of item ID: $smithingGetName")

                    if (success) {
                        // Type the quantity
                        Execution.delay(Navi.random.nextLong(1300, 2000)) // Short delay before typing
                        GameInput.setTextInput(quantity.toString())
                        Execution.delay(Navi.random.nextLong(1600, 2600)) // Simulate delay after typing

                        Zezimax.Logger.log("Withdrew $quantity of item ID: $smithingGetName.")
                    } else {
                        Zezimax.Logger.log("Failed to withdraw item ID: $smithingGetName")
                    }
                }
            } else {
                Zezimax.Logger.log("Could not find item ID: $smithingGetName in the bank.")
            }
        }
    }
}


fun depositOreInFurnace() {
    // Navigate to the smithing furnace location
    val reachedFurnace = Navi.walkToFaladorSmithingFurnace()
    if (reachedFurnace) {
        Zezimax.Logger.log("Reached Falador Smithing Furnace.")
    } else {
        Zezimax.Logger.log("Failed to reach Falador Smithing Furnace.")
    }
    val furnace: SceneObject? = SceneObjectQuery.newQuery().name("Furnace").results().nearest()
    if (furnace != null) {
        furnace.interact("Smelt")
        Zezimax.Logger.log("Smelting ore at furnace...")
        Execution.delay(Navi.random.nextLong(1300, 2000))
    }
    val smithDepositAllButton = ComponentQuery.newQuery(37)
        .componentIndex(167)
        .results()
        .firstOrNull()

    if (smithDepositAllButton != null) {
        Execution.delay(Navi.random.nextLong(400, 1000))
        smithDepositAllButton.interact()
        Zezimax.Logger.log("Deposited all ores into Furnace Inventory")
        Execution.delay(Navi.random.nextLong(1000, 1700))
    } else {
        Zezimax.Logger.log("Couldn't Deposit all ores...")
        Execution.delay(Navi.random.nextLong(400, 1000))
    }
    Zezimax.botState = Zezimax.ZezimaxBotState.START_SMITHING_ORE
}

fun smithOre() {

    // Helper function to extract count from Furnace Interface Component
    fun getCountFromComponent(component: Component?): Int {
        return component?.itemAmount ?: 0
    }

    // WHILE INTERFACE OPEN DECIDE WHICH BAR TO SMELT
    val copperore_Count = getCountFromComponent(
        ComponentQuery.newQuery(37)
            .componentIndex(52)
            .subComponentIndex(1)
            .results()
            .firstOrNull()
    )
    val tinore_Count = getCountFromComponent(
        ComponentQuery.newQuery(37)
            .componentIndex(52)
            .subComponentIndex(3)
            .results()
            .firstOrNull()
    )
    val ironore_Count = getCountFromComponent(
        ComponentQuery.newQuery(37)
            .componentIndex(52)
            .subComponentIndex(5)
            .results()
            .firstOrNull()
    )
    val coal_Count = getCountFromComponent(
        ComponentQuery.newQuery(37)
            .componentIndex(52)
            .subComponentIndex(7)
            .results()
            .firstOrNull()
    )
    val mithrilore_Count = getCountFromComponent(
        ComponentQuery.newQuery(37)
            .componentIndex(52)
            .subComponentIndex(9)
            .results()
            .firstOrNull()
    )
    val adamantiteore_Count = getCountFromComponent(
        ComponentQuery.newQuery(37)
            .componentIndex(52)
            .subComponentIndex(11)
            .results()
            .firstOrNull()
    )
    val runiteore_Count = getCountFromComponent(
        ComponentQuery.newQuery(37)
            .componentIndex(52)
            .subComponentIndex(15)
            .results()
            .firstOrNull()
    )
    val luminite_Count = getCountFromComponent(
        ComponentQuery.newQuery(37)
            .componentIndex(52)
            .subComponentIndex(13)
            .results()
            .firstOrNull()
    )
    val goldore_Count = getCountFromComponent(
        ComponentQuery.newQuery(37)
            .componentIndex(52)
            .subComponentIndex(33)
            .results()
            .firstOrNull()
    )
    val silverore_Count = getCountFromComponent(
        ComponentQuery.newQuery(37)
            .componentIndex(52)
            .subComponentIndex(31)
            .results()
            .firstOrNull()
    )
    var barToSmelt = ""
    var barSmeltButton = 0
    val smithingLevel = Skills.SMITHING.level
    if (smithingLevel >= 50 && runiteore_Count >= 1 && luminite_Count >= 1) {
        barToSmelt = "Rune bar"
        barSmeltButton = 11
    }
    else if (smithingLevel >= 40 && adamantiteore_Count >= 1 && luminite_Count >= 1) {
        barToSmelt = "Adamant bar"
        barSmeltButton = 9
    }
    else if (smithingLevel >= 30 && mithrilore_Count >= 1 && coal_Count >= 1) {
        barToSmelt = "Mithril bar"
        barSmeltButton = 7
    }
    else if (smithingLevel >= 20 && ironore_Count >= 1 && coal_Count >= 1) {
        barToSmelt = "Steel bar"
        barSmeltButton = 5
    }
    else if (smithingLevel >= 10 && ironore_Count >= 2) {
        barToSmelt = "Iron bar"
        barSmeltButton = 3
    }
    else if (copperore_Count >= 1 && tinore_Count>= 1) {
        barToSmelt = "Bronze bar"
        barSmeltButton = 1
    }
    else {
        Execution.delay(Navi.random.nextLong(1500, 2500))
        Zezimax.Logger.log("Not enough materials to continue, Returning Ore and Reinitializing...")
        val smithWithdrawAllButton = ComponentQuery.newQuery(37)
            .componentIndex(170) // Assuming component index for "Withdraw all"
            .results()
            .firstOrNull()
        if (smithWithdrawAllButton != null) {
            smithWithdrawAllButton.interact()
            Execution.delay(Navi.random.nextLong(2500, 3500))
        }
        Navi.walkToFaladorSmithBank()
        Execution.delay(Navi.random.nextLong(1500, 2500))
        Bank.open()
        Execution.delay(Navi.random.nextLong(1500, 2500))
        Bank.depositAll()
        Execution.delay(Navi.random.nextLong(1500, 2500))
        Zezimax.botState = Zezimax.ZezimaxBotState.INITIALIZING // back to decision tree
        return
    }


    while (true) {
        // Select bar in interface - component 37, 103, subcomponent 11 for "Rune bar"
        val barComponent = ComponentQuery.newQuery(37)
            .componentIndex(103)
            .subComponentIndex(barSmeltButton)
            .results()
            .firstOrNull()

        if (barComponent != null) {
            Execution.delay(Navi.random.nextLong(400, 1000))
            barComponent.interact("Select $barToSmelt")
            Zezimax.Logger.log("Selected $barToSmelt.")
            Execution.delay(Navi.random.nextLong(400, 1000))
        } else {
            Zezimax.Logger.log("$barToSmelt component not found.")
        }

        // Press begin project button - component 37, 163
        val beginProjectButton = ComponentQuery.newQuery(37)
            .componentIndex(163)
            .results()
            .firstOrNull()

        if (beginProjectButton != null) {
            Execution.delay(Navi.random.nextLong(400, 1000))
            beginProjectButton.interact()
            Zezimax.Logger.log("Pressed Begin Project button.")
            Execution.delay(Navi.random.nextLong(3000, 6000))
        } else {
            Zezimax.Logger.log("Begin Project button not found.")
        }
            // IF INTERFACE IS STILL OPEN, INVALID MATERIALS, LEAVING LOOP
        if (Interfaces.isOpen(37)) {
            // Click deposit materials
            val smithDepositAllButton = ComponentQuery.newQuery(37)
                .componentIndex(167) // Assuming component index for "Deposit All"
                .results()
                .firstOrNull()

            val smithWithdrawAllButton = ComponentQuery.newQuery(37)
                .componentIndex(170) // Assuming component index for "Withdraw all"
                .results()
                .firstOrNull()

            if (smithDepositAllButton != null) {
                smithDepositAllButton.interact()
                Zezimax.Logger.log("Deposited all materials.")
                Execution.delay(Navi.random.nextLong(1500, 2500))
                Zezimax.Logger.log("Not enough materials to continue, Returning Ore to Bank and Reinitializing...")
                if (smithWithdrawAllButton != null) {
                    smithWithdrawAllButton.interact()
                    Execution.delay(Navi.random.nextLong(2500, 3500))
                }
                Navi.walkToFaladorSmithBank()
                Execution.delay(Navi.random.nextLong(1500, 2500))
                Bank.open()
                Execution.delay(Navi.random.nextLong(1500, 2500))
                Bank.depositAll()
                Execution.delay(Navi.random.nextLong(1500, 2500))
                Zezimax.botState = Zezimax.ZezimaxBotState.INITIALIZING // back to decision tree
                return
            } else {
                Zezimax.Logger.log("Deposit All button not found.")
            }

        } else {
            // WAIT FOR EXP INTERFACE TO GO AWAY
            // Check exp interface
            while (Interfaces.isOpen(1251)) {
                Execution.delay(Navi.random.nextLong(2000, 6000))
            }
        }

        // Interact with furnace again to return bars
        val furnace = SceneObjectQuery.newQuery()
            .name("Furnace")
            .results()
            .nearest()

        if (furnace != null) {
            furnace.interact("Smelt")
            Zezimax.Logger.log("Interacted with Furnace to Smelt.")
            Execution.delay(Navi.random.nextLong(1200, 1900))
        } else {
            Zezimax.Logger.log("Furnace not found.")
            Execution.delay(Navi.random.nextLong(1200, 1900))
        }

        // Click deposit materials again
        val smithDepositAllButton2 = ComponentQuery.newQuery(37)
            .componentIndex(167) // Assuming component index for "Deposit All"
            .results()
            .firstOrNull()

        if (smithDepositAllButton2 != null) {
            Execution.delay(Navi.random.nextLong(1500, 2500))
            smithDepositAllButton2.interact()
            Zezimax.Logger.log("Deposited all materials.")
        } else {
            Zezimax.Logger.log("Deposit All button not found.")
        }
    }
}
// End of loop
