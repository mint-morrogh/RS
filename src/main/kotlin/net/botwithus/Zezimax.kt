package net.botwithus

import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.internal.scripts.ScriptDefinition
import net.botwithus.rs3.events.impl.InventoryUpdateEvent
import net.botwithus.rs3.events.impl.SkillUpdateEvent
import net.botwithus.rs3.game.Area
import net.botwithus.rs3.game.Client
import net.botwithus.rs3.game.Coordinate
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
import net.botwithus.rs3.script.config.ScriptConfig
import net.botwithus.rs3.util.Regex
import java.util.*
import java.util.concurrent.Callable
import java.util.regex.Pattern


class Zezimax(
    name: String,
    scriptConfig: ScriptConfig,
    scriptDefinition: ScriptDefinition
) : LoopingScript(name, scriptConfig, scriptDefinition) {

    enum class ZezimaxBotState {
        IDLE,
        MINING,
        NAVIGATING_TO_MINE,
        NAVIGATING_TO_BANK,
        BANKING
    }

    var botState: ZezimaxBotState = ZezimaxBotState.IDLE
    var someBoolean: Boolean = true

    override fun initialize(): Boolean {
        super.initialize()
        this.sgc = ZezimaxGraphicsContext(this, console)
        botState = ZezimaxBotState.IDLE
        println("Script initialized. State set to IDLE.")
        return true
    }

    override fun onLoop() {
        val player = Client.getLocalPlayer()
        if (Client.getGameState() != Client.GameState.LOGGED_IN || player == null) {
            println("Player not logged in. Delaying execution.")
            Execution.delay(Navi.random.nextLong(2500, 7500))
            return
        }

        when (botState) {
            ZezimaxBotState.IDLE -> {
                val miningLevel = Skills.MINING.level
                if (miningLevel >= 60) {
                    botState = ZezimaxBotState.NAVIGATING_TO_MINE
                } else {
                    println("Mining level too low: $miningLevel")
                    Execution.delay(Navi.random.nextLong(1500, 3000))
                }
            }
            ZezimaxBotState.NAVIGATING_TO_MINE -> {
                if (Navi.walkToMiningGuild()) {
                    println("Reached mining guild.")
                    botState = ZezimaxBotState.MINING
                } else {
                    println("Walking to mining guild.")
                }
            }
            ZezimaxBotState.NAVIGATING_TO_BANK -> {
                if (Navi.walkToFaladorSmithBank()) {
                    println("Reached Falador smith bank.")
                    botState = ZezimaxBotState.BANKING
                } else {
                    println("Walking to Falador smith bank.")
                }
            }
            ZezimaxBotState.MINING -> {
                Mining().mine(player)
            }
            ZezimaxBotState.BANKING -> {
                Banking().bank()
            }
        }
    }

    inner class Mining {
        fun mine(player: LocalPlayer) {
            if (!Backpack.isFull()) {
                val runeRock: SceneObject? = SceneObjectQuery.newQuery().name("Runite rock").results().nearest()
                if (runeRock != null && runeRock.interact("Mine")) {
                    println("Mining rune rock...")
                    Execution.delay(Navi.random.nextLong(6000, 20000)) // Simulate mining delay
                } else {
                    println("No rune rock found or failed to interact.")
                    Execution.delay(Navi.random.nextLong(1500, 3000))
                }

                // Check if inventory is full during mining process
                if (Backpack.isFull()) {
                    botState = ZezimaxBotState.NAVIGATING_TO_BANK
                    println("Inventory is full. Navigating to bank.")
                }
            }
        }
    }


    inner class Banking {
        fun bank() {
            if (!Backpack.isFull()) {
                println("Backpack is not full, should be mining.")
                botState = ZezimaxBotState.NAVIGATING_TO_MINE
                return
            }

            if (!Bank.isOpen()) {
                // Open the bank
                val bankBooth: SceneObject? = SceneObjectQuery.newQuery().name("Bank booth", "Bank chest").results().nearest()
                if (bankBooth != null && (bankBooth.interact("Bank") || bankBooth.interact("Use"))) {
                    println("Interacting with bank booth or chest.")
                    Execution.delayUntil(5000, Callable { Bank.isOpen() })
                } else {
                    println("No bank booth or chest found or failed to interact.")
                    Execution.delay(Navi.random.nextLong(1500, 3000))
                    return
                }
            }

            // Deposit all runite ore into the bank
            if (Bank.isOpen()) {
                println("Depositing all runite ore.")
                Bank.depositAll("Runite ore")
                Execution.delay(Navi.random.nextLong(1000, 4000)) // Simulate deposit delay

                // Close the bank
                Bank.close()
                Execution.delay(Navi.random.nextLong(1000, 4000)) // Simulate bank closing delay
                botState = ZezimaxBotState.NAVIGATING_TO_MINE
            } else {
                println("Bank is not open, retrying.")
                Execution.delay(Navi.random.nextLong(1500, 4000))
            }
        }
    }
}
