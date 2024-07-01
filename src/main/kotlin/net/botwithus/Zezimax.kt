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

class Zezimax(
    name: String,
    scriptConfig: ScriptConfig,
    scriptDefinition: ScriptDefinition
) : LoopingScript(name, scriptConfig, scriptDefinition) {

    enum class ZezimaxBotState {
        INITIALIZING,
        IDLE,
        START_MINING,
        MINING_BANKING,
        START_SMITHING_ORE,
        SMITHING_ORE,
        START_GEODE_CRACKER,
        GEODE_CRACKER
    }



    companion object {
        var botState: ZezimaxBotState = ZezimaxBotState.INITIALIZING
    }
    var someBoolean: Boolean = true
    object Logger {
        private var scriptConsole: ScriptConsole? = null
        fun initialize(console: ScriptConsole) {
            scriptConsole = console
        }
        fun log(message: String) {
            scriptConsole?.addLineToConsole(message) ?: println(message)
        }
    }



    override fun initialize(): Boolean {
        super.initialize()
        val console = this.console // Assuming `this.console` gives you the ScriptConsole
        Logger.initialize(console)
        Logger.log("Script initialized. State set to INITIALIZING.")
        return true
    }

    override fun onLoop() {
        val player = Client.getLocalPlayer()
        if (Client.getGameState() != Client.GameState.LOGGED_IN || player == null) {
            println("Player not logged in. Delaying execution.")
            Execution.delay(Navi.random.nextLong(2500, 7500))
            return
        }

        // IDLE AND INITIALIZING STATES
        when (botState) {
            ZezimaxBotState.INITIALIZING -> {
                if (initializeBanking()) {
                    botState = ZezimaxBotState.IDLE
                }
            }
            ZezimaxBotState.IDLE -> {
                println("Bot state is IDLE. Decision: ${DecisionTree.decision}")

                // TESTING FUNCTIONS HERE
                grandExchangeBuy("runite ore", "6")
                Execution.delay(Navi.random.nextLong(25000, 75000))


                if (DecisionTree.decision == null) {
                    DecisionTree.makeRandomDecision()
                } else {
                    when (DecisionTree.decision) {
                        0 -> botState = ZezimaxBotState.START_MINING
                        1 -> botState = ZezimaxBotState.START_SMITHING_ORE
                        2 -> botState = ZezimaxBotState.START_GEODE_CRACKER
                    }
                }
            }




            // MINING STATES
            ZezimaxBotState.START_MINING -> {
                println("Decided to Mine...")
                botState = ZezimaxBotState.MINING_BANKING
            }
            ZezimaxBotState.MINING_BANKING -> {
                println("Mining ${DecisionTree.oreToCollect} at Location: ${DecisionTree.mineLocation}...")
                Mining(DecisionTree.mineLocation, DecisionTree.bankLocation, DecisionTree.rockToMine, DecisionTree.oreToCollect, DecisionTree.oreBoxName, 200).mine(player)
            }




            // SMITHING ORE STATES
            ZezimaxBotState.START_SMITHING_ORE -> {
                println("Decided to Smith Ore...")
                depositOreInFurnace()
                Execution.delay(Navi.random.nextLong(500, 2000))
                botState = ZezimaxBotState.SMITHING_ORE
            }

            ZezimaxBotState.SMITHING_ORE -> {
                smithOre()
            }



            // GEODE CRACKING STATES
            ZezimaxBotState.START_GEODE_CRACKER -> {
                println("Decided to crack geodes...")
                botState = ZezimaxBotState.GEODE_CRACKER
            }
            ZezimaxBotState.GEODE_CRACKER -> {
                geodeCracker()
            }
        }
    }





    private fun initializeBanking(): Boolean {
        val player = Client.getLocalPlayer() ?: return false
        val nearestBank = Navi.getNearestBank(player.coordinate) ?: return false

        if (!nearestBank.contains(player.coordinate)) {
            println("**INITIALIZING** Walking to the nearest Bank...")
            val path = NavPath.resolve(nearestBank.randomWalkableCoordinate)
            if (path != null && Movement.traverse(path) != TraverseEvent.State.NO_PATH) {
                Execution.delay(Navi.random.nextLong(700, 1500))
                return false
            }
        }

        if (!Bank.isOpen()) {
            // Open the bank
            Bank.open()
            Execution.delay(Navi.random.nextLong(1500, 3000))
        }

        // Deposit all items into the bank
        if (Bank.isOpen()) {
            println("**INITIALIZING** Depositing all items.")
            Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay
            Bank.depositAll()
            Execution.delay(Navi.random.nextLong(2000, 4000)) // Simulate deposit delay

            // Make random decision on what to do next
            DecisionTree.makeRandomDecision()

            // Close the bank
            Bank.close()
            Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate bank closing delay
            println("Initialization complete. Starting main script.")
            return true
        } else {
            println("Bank is not open, retrying.")
            Execution.delay(Navi.random.nextLong(1500, 3000))
            return false
        }
    }
}


