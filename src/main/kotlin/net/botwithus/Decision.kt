package net.botwithus

import net.botwithus.Zezimax.Companion.botState
import net.botwithus.Zezimax.ZezimaxBotState
import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.rs3.game.skills.Skills
import net.botwithus.rs3.util.RandomGenerator

object DecisionTree {

    var decision: Int? = null
    var oreBoxName: String = ""
    var mineLocation: String = ""
    var bankLocation: String = ""
    var oreToCollect: String = ""
    var rockToMine: String = ""
    var actionToMine: String = ""
    var woodBoxName: String = ""
    var woodcuttingLocation: String = ""
    var logsToCollect: String = ""
    var treeToChop: String = ""
    var actionToChop: String = ""
    var startRand: Long = 0
    var endRand: Long = 0
    var bonfireFuel: String = ""


    fun makeRandomDecision() {
/*
        decision = Navi.random.nextInt(3) // generates 0, 1, or 2

 */
         decision = 4

        Zezimax.Logger.log("Decision made: $decision")

        when (decision) {



            0 -> {

                // MINING
                val runeCount = Bank.getItems().filter { it.name == "Runite ore" }.sumOf { it.stackSize }
                val luminiteCount = Bank.getItems().filter { it.name == "Luminite" }.sumOf { it.stackSize }
                val adamantiteCount = Bank.getItems().filter { it.name == "Adamantite ore" }.sumOf { it.stackSize }
                val mithrilCount = Bank.getItems().filter { it.name == "Mithril ore" }.sumOf { it.stackSize }
                val coalCount = Bank.getItems().filter { it.name == "Coal" }.sumOf { it.stackSize }
                val ironCount = Bank.getItems().filter { it.name == "Iron ore" }.sumOf { it.stackSize }
                val tinCount = Bank.getItems().filter { it.name == "Tin ore" }.sumOf { it.stackSize }
                val copperCount = Bank.getItems().filter { it.name == "Copper ore" }.sumOf { it.stackSize }
                var taskAssigned = false
                Zezimax.Logger.log("Selected Task: Mining")
                val miningLevel = Skills.MINING.level
                oreBoxName = "Rune ore box"

                if (miningLevel >= 50 && !taskAssigned) {
                    // Which Mine Task Decided
                    if (runeCount <= 300) {
                        mineLocation = "MiningGuild"
                        bankLocation = "FaladorSmithBank"
                        oreToCollect = "Runite ore"
                        rockToMine = "Runite rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                    else if (luminiteCount <= 300) {
                        mineLocation = "FaladorLuminite"
                        bankLocation = "FaladorSmithBank"
                        oreToCollect = "Luminite"
                        rockToMine = "Luminite rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                }
                if (miningLevel >= 40 && !taskAssigned) {
                    // Which Mine Task Decided
                    if (adamantiteCount <= 300) {
                        mineLocation = "VarrockEastMine"
                        bankLocation = "VarrockEastBank"
                        oreToCollect = "Adamantite ore"
                        rockToMine = "Adamantite rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                    else if (luminiteCount <= 300) {
                        mineLocation = "FaladorLuminite"
                        bankLocation = "FaladorSmithBank"
                        oreToCollect = "Luminite"
                        rockToMine = "Luminite rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                }
                if (miningLevel >= 30 && !taskAssigned) {
                    // Which Mine Task Decided
                    if (mithrilCount <= 300) {
                        mineLocation = "VarrockWestMine"
                        bankLocation = "VarrockWestBank"
                        oreToCollect = "Mithril ore"
                        rockToMine = "Mithril rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                    else if (coalCount <= 300) {
                        mineLocation = "MiningGuild"
                        bankLocation = "FaladorSmithBank"
                        oreToCollect = "Coal"
                        rockToMine = "Coal rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                }
                if (miningLevel >= 20 && !taskAssigned) {
                    // Which Mine Task Decided
                    if (ironCount <= 300) {
                        mineLocation = "VarrockWestMine"
                        bankLocation = "VarrockWestBank"
                        oreToCollect = "Iron ore"
                        rockToMine = "Iron rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                    else if (copperCount <= 300) {
                        mineLocation = "VarrockWestMine"
                        bankLocation = "VarrockWestBank"
                        oreToCollect = "Copper ore"
                        rockToMine = "Copper rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                    else if (tinCount <= 300) {
                        mineLocation = "VarrockWestMine"
                        bankLocation = "VarrockWestBank"
                        oreToCollect = "Tin ore"
                        rockToMine = "Tin rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                }
                if (!taskAssigned){
                    // Which Mine Task Decided
                    if (copperCount <= 300) {
                        mineLocation = "VarrockWestMine"
                        bankLocation = "VarrockWestBank"
                        oreToCollect = "Copper ore"
                        rockToMine = "Copper rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    } else if (tinCount <= 300) {
                        mineLocation = "VarrockWestMine"
                        bankLocation = "VarrockWestBank"
                        oreToCollect = "Tin ore"
                        rockToMine = "Tin rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                }

                Zezimax.Logger.log("Mine Location: $mineLocation")
                Zezimax.Logger.log("bank location: $bankLocation")
                Zezimax.Logger.log("ore to collect: $oreToCollect")
                Zezimax.Logger.log("rock to mine: $rockToMine")
                Zezimax.Logger.log("action to mine: $actionToMine")
                withdrawMiningSupplies(oreBoxName, 1)
            }






            1 -> { // SMIHTING ORE
                Zezimax.Logger.log("Selected Task: Smithing Ore")
                withdrawSmithingOreSupplies(
                    44820 to null, // Luminite
                    453 to null, // Coal
                    451 to null, // Runite ore
                    449 to null, // Adamantite ore
                    447 to null, // Mithril ore
                    440 to null, // Iron ore
                    436 to null, // Copper ore
                    438 to null  // Tin ore
                )

            }
            2 -> {
                Zezimax.Logger.log("Selected Task: Cracking Geodes")
                withdrawGeodes()

            }


            3 -> {

                // WOODCUTTING
                val willowCount = Bank.getItems().filter { it.name == "Willow logs" }.sumOf { it.stackSize }
                val oakCount = Bank.getItems().filter { it.name == "Oak logs" }.sumOf { it.stackSize }
                val logCount = Bank.getItems().filter { it.name == "Logs" }.sumOf { it.stackSize }
                var taskAssigned = false
                Zezimax.Logger.log("Selected Task: Woodcutting")
                val woodcuttingLevel = Skills.WOODCUTTING.level
                woodBoxName = "Willow wood box"

                if (woodcuttingLevel >= 20 && !taskAssigned) {
                    // Which Woodcutting Task Decided
                    if (willowCount <= 300) {
                        woodcuttingLocation = "DraynorWillows"
                        bankLocation = "DraynorBank"
                        logsToCollect = "Willow logs"
                        treeToChop = "Willow"
                        actionToChop = "Chop down"
                        startRand = 11000
                        endRand = 22000
                        taskAssigned = true
                    }
                    else if (logCount <= 300) {
                        woodcuttingLocation = "VarrockWestTrees"
                        bankLocation = "VarrockWestBank"
                        logsToCollect = "Logs"
                        treeToChop = "Tree"
                        actionToChop = "Chop down"
                        startRand = 2500
                        endRand = 6500
                        taskAssigned = true
                    }
                }
                if (woodcuttingLevel >= 10 && !taskAssigned) {
                    // Which Woodcutting Task Decided
                    if (oakCount <= 300) {
                        woodcuttingLocation = "DraynorOaks"
                        bankLocation = "DraynorBank"
                        logsToCollect = "Oak logs"
                        treeToChop = "Oak"
                        actionToChop = "Chop down"
                        startRand = 8500
                        endRand = 19500
                        taskAssigned = true
                    }
                    else if (logCount <= 300) {
                        woodcuttingLocation = "VarrockWestTrees"
                        bankLocation = "VarrockWestBank"
                        logsToCollect = "Logs"
                        treeToChop = "Tree"
                        actionToChop = "Chop down"
                        startRand = 2500
                        endRand = 6500
                        taskAssigned = true
                    }
                }
                if (!taskAssigned){
                    // Which Woodcutting Task Decided
                    if (logCount <= 300) {
                        woodcuttingLocation = "VarrockWestTrees"
                        bankLocation = "VarrockWestBank"
                        logsToCollect = "Logs"
                        treeToChop = "Tree"
                        actionToChop = "Chop down"
                        startRand = 2500
                        endRand = 6500
                        taskAssigned = true

                    }
                    else if (logCount <= 500) {
                        woodcuttingLocation = "VarrockWestTrees"
                        bankLocation = "VarrockWestBank"
                        logsToCollect = "Logs"
                        treeToChop = "Tree"
                        actionToChop = "Chop down"
                        startRand = 2500
                        endRand = 6500
                        taskAssigned = true
                    }
                }

                Zezimax.Logger.log("Woodcutting Location: $woodcuttingLocation")
                Zezimax.Logger.log("bank location: $bankLocation")
                Zezimax.Logger.log("logs to collect: $logsToCollect")
                Zezimax.Logger.log("tree to chop: $treeToChop")
                Zezimax.Logger.log("action to chop: $actionToChop")
                withdrawWoodcuttingSupplies(woodBoxName, 1)
            }

            4 -> {

                //FIREMAKING
                Zezimax.Logger.log("Selected Task: Firemaking")
                startFiremaking()
            }

        }
    }
}