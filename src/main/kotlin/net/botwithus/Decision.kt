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
    var stoneSpirit: String = ""
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
    var fishingLocation: String = ""
    var fishToCollect: String = ""
    var spotToFish: String = ""
    var actionToFish: String = ""
    var feathersNeeded: Boolean = false
    var baitNeeded: Boolean = false

    fun makeRandomDecision() {


        decision = Navi.random.nextInt(6) // bound starts at 0

        /*
         decision = 5

         */


        Zezimax.Logger.log("Decision made: $decision")

        when (decision) {



            0 -> {

                // MINING
                val runeCount = Bank.getItems().filter { it.id == 451 }.sumOf { it.stackSize }
                val runeStoneSpirit = Bank.getItems().filter { it.id == 44808}.sumOf { it.stackSize }
                val luminiteCount = Bank.getItems().filter { it.id == 44820 }.sumOf { it.stackSize }
                val luminiteStoneSpirit = Bank.getItems().filter { it.id == 44806}.sumOf { it.stackSize }
                val adamantiteCount = Bank.getItems().filter { it.id == 449 }.sumOf { it.stackSize }
                val adamantiteStoneSpirit = Bank.getItems().filter { it.id == 44807 }.sumOf { it.stackSize }
                val mithrilCount = Bank.getItems().filter { it.id == 447 }.sumOf { it.stackSize }
                val mithrilStoneSpirit = Bank.getItems().filter { it.id == 44805 }.sumOf { it.stackSize }
                val coalCount = Bank.getItems().filter { it.id == 453 }.sumOf { it.stackSize }
                val coalStoneSpirit = Bank.getItems().filter { it.id == 44804 }.sumOf { it.stackSize }
                val ironCount = Bank.getItems().filter { it.id == 441 }.sumOf { it.stackSize }
                val ironStoneSpirit = Bank.getItems().filter { it.id == 44801 }.sumOf { it.stackSize }
                val tinCount = Bank.getItems().filter { it.id == 438 }.sumOf { it.stackSize }
                val tinStoneSpirit = Bank.getItems().filter { it.id == 44800 }.sumOf { it.stackSize }
                val copperCount = Bank.getItems().filter { it.id == 436 }.sumOf { it.stackSize }
                val copperStoneSpirit = Bank.getItems().filter { it.id == 44799 }.sumOf { it.stackSize }
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
                        stoneSpirit = "Runite stone spirit"
                        rockToMine = "Runite rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                    else if (luminiteCount <= 300) {
                        mineLocation = "FaladorLuminite"
                        bankLocation = "FaladorSmithBank"
                        oreToCollect = "Luminite"
                        stoneSpirit = "Luminite stone spirit"
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
                        startRand = 8000
                        endRand = 15000
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
                        startRand = 6500
                        endRand = 15000
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

            5 -> {

                // FISHING
                val baitCount = Bank.getItems().filter { it.id == 313 }.sumOf { it.stackSize }
                val featherCount = Bank.getItems().filter { it.id == 314 }.sumOf { it.stackSize }
                val rawShrimpCount= Bank.getItems().filter { it.id == 36222 }.sumOf { it.stackSize }
                val rawAnchoviesCount= Bank.getItems().filter { it.id == 43849 }.sumOf { it.stackSize }
                val rawSardineCount= Bank.getItems().filter { it.id == 43866 }.sumOf { it.stackSize }
                val rawHerringCount= Bank.getItems().filter { it.id == 345 }.sumOf { it.stackSize }
                val rawTroutCount= Bank.getItems().filter { it.id == 43871 }.sumOf { it.stackSize }
                val rawSalmonCount= Bank.getItems().filter { it.id == 331 }.sumOf { it.stackSize }
                val rawPikeCount= Bank.getItems().filter { it.id == 349 }.sumOf { it.stackSize }
                val rawTunaCount= Bank.getItems().filter { it.id == 359 }.sumOf { it.stackSize }
                val rawLobsterCount= Bank.getItems().filter { it.id == 337 }.sumOf { it.stackSize }
                val rawSwordfishCount= Bank.getItems().filter { it.id == 43870 }.sumOf { it.stackSize }
                var taskAssigned = false
                Zezimax.Logger.log("Selected Task: Fishing")
                val fishingLevel = Skills.FISHING.level

                if (fishingLevel >= 30 && !taskAssigned) {
                    // Which FIshing Task Decided
                    if (rawSalmonCount <= 150) {
                        if (featherCount <= 100) {
                            grandExchangeBuy("feather", "200")
                        }
                        fishingLocation = "BarbarianVillageFishing"
                        bankLocation = "EdgevilleBank"
                        fishToCollect = "Raw salmon"
                        spotToFish = "Fishing spot"
                        actionToFish = "Lure"
                        feathersNeeded = true
                        baitNeeded = false
                        taskAssigned = true

                    }
                    else if (rawTroutCount <= 150) {
                        if (featherCount <= 100) {
                            grandExchangeBuy("feather", "200")
                        }
                        fishingLocation = "BarbarianVillageFishing"
                        bankLocation = "EdgevilleBank"
                        fishToCollect = "Raw trout"
                        spotToFish = "Fishing spot"
                        actionToFish = "Lure"
                        feathersNeeded = true
                        baitNeeded = false
                        taskAssigned = true
                    }
                }
                if (fishingLevel >= 20 && !taskAssigned) {
                    // Which FIshing Task Decided
                    if (rawTroutCount <= 150) {
                        if (featherCount <= 100) {
                            grandExchangeBuy("feather", "200")
                        }
                        fishingLocation = "BarbarianVillageFishing"
                        bankLocation = "EdgevilleBank"
                        fishToCollect = "Raw trout"
                        spotToFish = "Fishing spot"
                        actionToFish = "Lure"
                        feathersNeeded = true
                        baitNeeded = false
                        taskAssigned = true
                    }
                    else if (rawAnchoviesCount <= 150) {
                        fishingLocation = "AlkharidWestFishing"
                        bankLocation = "AlkharidWestBank"
                        fishToCollect = "Raw anchovies"
                        spotToFish = "Fishing spot"
                        actionToFish = "Net"
                        feathersNeeded = false
                        baitNeeded = false
                        taskAssigned = true
                    }
                }


                Zezimax.Logger.log("Fishing Location: $fishingLocation")
                Zezimax.Logger.log("bank location: $bankLocation")
                Zezimax.Logger.log("fish to collect: $fishToCollect")
                Zezimax.Logger.log("spot to fish: $spotToFish")
                Zezimax.Logger.log("action to fish: $actionToFish")
                withdrawFishingSupplies()

            }

        }
    }
}