package net.botwithus

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


    fun makeRandomDecision() {
        decision = Navi.random.nextInt(2) // Adjust range if more tasks are added
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
                Zezimax.Logger.log("rock to mine: $actionToMine")
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
            // Add more cases for other tasks here
        }
    }
}