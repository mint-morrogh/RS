package net.botwithus

import net.botwithus.Zezimax.Companion.botState
import net.botwithus.Zezimax.ZezimaxBotState
import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery
import net.botwithus.rs3.game.skills.Skills
import net.botwithus.rs3.util.RandomGenerator

object DecisionTree {

    var decision: Int? = null
    var oreBoxName: String = ""
    var mineLocation: String = ""
    var bankLocation: String = ""
    var oreToCollect: Int = 0
    var stoneSpirit: Int = 0
    var rockToMine: String = ""
    var actionToMine: String = ""
    var woodBoxName: String = ""
    var woodcuttingLocation: String = ""
    var logsToCollect: Int = 0
    var treeToChop: String = ""
    var actionToChop: String = ""
    var startRand: Long = 0
    var endRand: Long = 0
    var bonfireFuel: String = ""
    var fishingLocation: String = ""
    var fishToCollect: Int = 0
    var spotToFish: String = ""
    var actionToFish: String = ""
    var feathersNeeded: Boolean = false
    var baitNeeded: Boolean = false
    var fishToCook: Int = 0
    var rangeLocation: String = ""
    var logToBurn: Int = 0
    var logToFletch: Int = 0

    fun makeRandomDecision() {

        // GET GP COUNT IN INVENTORY
        val goldInventorySlot = InventoryItemQuery.newQuery(623).ids(995).results()
        val gp = goldInventorySlot.sumOf {it.stackSize}


                decision = Navi.random.nextInt(8) // bound starts at 0







/*
         decision = 4

 */








        Zezimax.Logger.log("Decision made: $decision")

        when (decision) {


// MINING
            0 -> {

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
                        if (runeStoneSpirit <= 50 && gp >= 2000000) {
                            grandExchangeBuy("Runite stone spirit", "250", "30")
                        }
                        mineLocation = "MiningGuild"
                        bankLocation = "FaladorSmithBank"
                        oreToCollect = 451
                        stoneSpirit = 44808
                        rockToMine = "Runite rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                    else if (luminiteCount <= 300) {
                        if (luminiteStoneSpirit <= 50 && gp >= 2000000) {
                            grandExchangeBuy("Luminite stone spirit", "250", "30")
                        }
                        mineLocation = "FaladorLuminite"
                        bankLocation = "FaladorSmithBank"
                        oreToCollect = 44820
                        stoneSpirit = 44806
                        rockToMine = "Luminite rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                }
                if (miningLevel >= 40 && !taskAssigned) {
                    // Which Mine Task Decided
                    if (adamantiteCount <= 300) {
                        if (adamantiteStoneSpirit <= 50 && gp >= 2000000) {
                            grandExchangeBuy("Adamantite stone spirit", "250", "80")
                        }
                        mineLocation = "VarrockEastMine"
                        bankLocation = "VarrockEastBank"
                        oreToCollect = 449
                        stoneSpirit = 44807
                        rockToMine = "Adamantite rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                    else if (luminiteCount <= 300) {
                        if (luminiteStoneSpirit <= 50 && gp >= 2000000) {
                            grandExchangeBuy("Luminite stone spirit", "250", "30")
                        }
                        mineLocation = "FaladorLuminite"
                        bankLocation = "FaladorSmithBank"
                        oreToCollect = 44820
                        stoneSpirit = 44806
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
                        oreToCollect = 447
                        stoneSpirit = 0
                        rockToMine = "Mithril rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                    else if (coalCount <= 300) {
                        mineLocation = "MiningGuild"
                        bankLocation = "FaladorSmithBank"
                        oreToCollect = 453
                        stoneSpirit = 0
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
                        oreToCollect = 441
                        stoneSpirit = 0
                        rockToMine = "Iron rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                    else if (copperCount <= 300) {
                        mineLocation = "VarrockWestMine"
                        bankLocation = "VarrockWestBank"
                        oreToCollect = 436
                        stoneSpirit = 0
                        rockToMine = "Copper rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                    else if (tinCount <= 300) {
                        mineLocation = "VarrockWestMine"
                        bankLocation = "VarrockWestBank"
                        oreToCollect = 438
                        stoneSpirit = 0
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
                        oreToCollect = 436
                        stoneSpirit = 0
                        rockToMine = "Copper rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    } else if (tinCount <= 300) {
                        mineLocation = "VarrockWestMine"
                        bankLocation = "VarrockWestBank"
                        oreToCollect = 438
                        stoneSpirit = 0
                        rockToMine = "Tin rock"
                        actionToMine = "Mine"
                        taskAssigned = true
                    }
                }

                withdrawMiningSupplies(oreBoxName, stoneSpirit,1)
            }



// SMIHTING ORE
            1 -> {
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



// GEODES
            2 -> {
                Zezimax.Logger.log("Selected Task: Cracking Geodes")
                withdrawGeodes()

            }



// WOODCUTTING
            3 -> {

                val willowCount = Bank.getItems().filter { it.id == 1519 }.sumOf { it.stackSize }
                val oakCount = Bank.getItems().filter { it.id == 1521 }.sumOf { it.stackSize }
                val logCount = Bank.getItems().filter { it.id == 1511 }.sumOf { it.stackSize }
                var taskAssigned = false
                Zezimax.Logger.log("Selected Task: Woodcutting")
                val woodcuttingLevel = Skills.WOODCUTTING.level
                woodBoxName = "Willow wood box"

                if (woodcuttingLevel >= 20 && !taskAssigned) {
                    // Which Woodcutting Task Decided
                    if (willowCount <= 300) {
                        woodcuttingLocation = "DraynorWillows"
                        bankLocation = "DraynorBank"
                        logsToCollect = 1519
                        treeToChop = "Willow"
                        actionToChop = "Chop down"
                        startRand = 8000
                        endRand = 15000
                        taskAssigned = true
                    }
                    else if (logCount <= 300) {
                        woodcuttingLocation = "VarrockWestTrees"
                        bankLocation = "VarrockWestBank"
                        logsToCollect = 1511
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
                        logsToCollect = 1521
                        treeToChop = "Oak"
                        actionToChop = "Chop down"
                        startRand = 6500
                        endRand = 15000
                        taskAssigned = true
                    }
                    else if (logCount <= 300) {
                        woodcuttingLocation = "VarrockWestTrees"
                        bankLocation = "VarrockWestBank"
                        logsToCollect = 1511
                        treeToChop = "Tree"
                        actionToChop = "Chop down"
                        startRand = 2500
                        endRand = 6500
                        taskAssigned = true
                    }
                }
                if (!taskAssigned) {
                    // Which Woodcutting Task Decided
                        woodcuttingLocation = "VarrockWestTrees"
                        bankLocation = "VarrockWestBank"
                        logsToCollect = 1511
                        treeToChop = "Tree"
                        actionToChop = "Chop down"
                        startRand = 2500
                        endRand = 6500
                        taskAssigned = true

                    }

                withdrawWoodcuttingSupplies(woodBoxName, 1)
            }



//FIREMAKING

            4 -> {

                val willowCount = Bank.getItems().filter { it.id == 1519 }.sumOf { it.stackSize }
                val oakCount = Bank.getItems().filter { it.id == 1521 }.sumOf { it.stackSize }
                val logCount = Bank.getItems().filter { it.id == 1511 }.sumOf { it.stackSize }
                var taskAssigned = false
                Zezimax.Logger.log("Selected Task: Firemaking")
                val firemakingLevel = Skills.FIREMAKING.level

                if (firemakingLevel >= 30 && !taskAssigned) {
                    // Which Firemaking Task Decided
                    if (willowCount >= 150) {
                        logToBurn = 1519
                        taskAssigned = true

                    }
                    else if (oakCount >= 150) {
                        logToBurn = 1521
                        taskAssigned = true
                    }
                }
                if (firemakingLevel >= 15 && !taskAssigned) {
                    // Which Firemaking Task Decided
                    if (oakCount >= 150) {
                        logToBurn = 1521
                        taskAssigned = true

                    }
                    else if (logCount >= 150) {
                        logToBurn = 1511
                        taskAssigned = true
                    }
                }
                if (!taskAssigned) {
                    // Which Firemaking Task Decided
                    if (logCount >= 150) {
                        logToBurn = 1511
                        taskAssigned = true

                    }
                }

                if (!taskAssigned) {
                    Zezimax.Logger.log("Not enough logs found. Re-Initializing...")
                    decision = 999
                    return
                }

                startFiremaking()
            }


// FISHING
            5 -> {

                val baitCount = Bank.getItems().filter { it.id == 313 }.sumOf { it.stackSize }
                val featherCount = Bank.getItems().filter { it.id == 314 }.sumOf { it.stackSize }
                val rawCrayfishCount= Bank.getItems().filter { it.id == 13435 }.sumOf { it.stackSize }
                val rawShrimpCount= Bank.getItems().filter { it.id == 2514 }.sumOf { it.stackSize }
                val rawAnchoviesCount= Bank.getItems().filter { it.id == 321 }.sumOf { it.stackSize }
                val rawSardineCount= Bank.getItems().filter { it.id == 327 }.sumOf { it.stackSize }
                val rawHerringCount= Bank.getItems().filter { it.id == 345 }.sumOf { it.stackSize }
                val rawTroutCount= Bank.getItems().filter { it.id == 335 }.sumOf { it.stackSize }
                val rawSalmonCount= Bank.getItems().filter { it.id == 331 }.sumOf { it.stackSize }
                val rawPikeCount= Bank.getItems().filter { it.id == 349 }.sumOf { it.stackSize }
                val rawTunaCount= Bank.getItems().filter { it.id == 359 }.sumOf { it.stackSize }
                val rawLobsterCount= Bank.getItems().filter { it.id == 337 }.sumOf { it.stackSize }
                val rawSwordfishCount= Bank.getItems().filter { it.id == 371 }.sumOf { it.stackSize }
                var taskAssigned = false
                Zezimax.Logger.log("Selected Task: Fishing")
                val fishingLevel = Skills.FISHING.level

                if (fishingLevel >= 50 && !taskAssigned ) {
                    // Which Fishing Task Decided
                    if (rawSwordfishCount <= 300 && gp >= 8000) {
                        fishingLocation = "PortSarimFishing"
                        bankLocation = "FaladorSouthBank"
                        fishToCollect = 371
                        spotToFish = "Fishing spot"
                        actionToFish = "Harpoon"
                        feathersNeeded = false
                        baitNeeded = false
                        taskAssigned = true

                    }
                    else if (rawSalmonCount <= 300 && gp >= 50000) {
                        if (featherCount <= 100) {
                            grandExchangeBuy("feather", "400", "20")
                        }
                        fishingLocation = "BarbarianVillageFishing"
                        bankLocation = "EdgevilleBank"
                        fishToCollect = 331
                        spotToFish = "Fishing spot"
                        actionToFish = "Lure"
                        feathersNeeded = true
                        baitNeeded = false
                        taskAssigned = true
                    }
                }
                if (fishingLevel >= 40 && !taskAssigned) {
                    // Which Fishing Task Decided
                    if (rawLobsterCount <= 300 && gp >= 8000) {
                        fishingLocation = "PortSarimFishing"
                        bankLocation = "FaladorSouthBank"
                        fishToCollect = 337
                        spotToFish = "Fishing spot"
                        actionToFish = "Cage"
                        feathersNeeded = false
                        baitNeeded = false
                        taskAssigned = true

                    }
                    else if (rawSalmonCount <= 300 && gp >= 50000) {
                            if (featherCount <= 100) {
                                grandExchangeBuy("feather", "400", "20")
                            }
                            fishingLocation = "BarbarianVillageFishing"
                            bankLocation = "EdgevilleBank"
                            fishToCollect = 331
                            spotToFish = "Fishing spot"
                            actionToFish = "Lure"
                            feathersNeeded = true
                            baitNeeded = false
                            taskAssigned = true
                    }
                }
                if (fishingLevel >= 30 && !taskAssigned) {
                    // Which Fishing Task Decided
                    if (rawSalmonCount <= 300 && gp >= 50000) {
                        if (featherCount <= 100) {
                            grandExchangeBuy("feather", "400", "20")
                        }
                        fishingLocation = "BarbarianVillageFishing"
                        bankLocation = "EdgevilleBank"
                        fishToCollect = 331
                        spotToFish = "Fishing spot"
                        actionToFish = "Lure"
                        feathersNeeded = true
                        baitNeeded = false
                        taskAssigned = true

                    }
                    else if (rawTroutCount <= 300 && gp >= 50000) {
                        if (featherCount <= 100) {
                            grandExchangeBuy("feather", "400", "20")
                        }
                        fishingLocation = "BarbarianVillageFishing"
                        bankLocation = "EdgevilleBank"
                        fishToCollect = 335
                        spotToFish = "Fishing spot"
                        actionToFish = "Lure"
                        feathersNeeded = true
                        baitNeeded = false
                        taskAssigned = true
                    }
                }
                if (fishingLevel >= 20 && !taskAssigned) {
                    // Which Fishing Task Decided
                    if (rawTroutCount <= 300 && gp >= 50000) {
                        if (featherCount <= 100) {
                            grandExchangeBuy("feather", "400", "20")
                        }
                        fishingLocation = "BarbarianVillageFishing"
                        bankLocation = "EdgevilleBank"
                        fishToCollect = 335
                        spotToFish = "Fishing spot"
                        actionToFish = "Lure"
                        feathersNeeded = true
                        baitNeeded = false
                        taskAssigned = true
                    }
                    else if (rawAnchoviesCount <= 300) {
                        fishingLocation = "AlkharidWestFishing"
                        bankLocation = "AlkharidWestBank"
                        fishToCollect = 321
                        spotToFish = "Fishing spot"
                        actionToFish = "Net"
                        feathersNeeded = false
                        baitNeeded = false
                        taskAssigned = true
                    }
                }



                withdrawFishingSupplies()

            }


// COOKING
            6 -> {

                val rawCrayfishCount= Bank.getItems().filter { it.id == 13435 }.sumOf { it.stackSize }
                val rawShrimpCount= Bank.getItems().filter { it.id == 2514 }.sumOf { it.stackSize }
                val rawAnchoviesCount= Bank.getItems().filter { it.id == 321 }.sumOf { it.stackSize }
                val rawSardineCount= Bank.getItems().filter { it.id == 327 }.sumOf { it.stackSize }
                val rawHerringCount= Bank.getItems().filter { it.id == 345 }.sumOf { it.stackSize }
                val rawTroutCount= Bank.getItems().filter { it.id == 335 }.sumOf { it.stackSize }
                val rawSalmonCount= Bank.getItems().filter { it.id == 331 }.sumOf { it.stackSize }
                val rawPikeCount= Bank.getItems().filter { it.id == 349 }.sumOf { it.stackSize }
                val rawTunaCount= Bank.getItems().filter { it.id == 359 }.sumOf { it.stackSize }
                val rawLobsterCount= Bank.getItems().filter { it.id == 337 }.sumOf { it.stackSize }
                val rawSwordfishCount= Bank.getItems().filter { it.id == 371 }.sumOf { it.stackSize }
                var taskAssigned = false
                Zezimax.Logger.log("Selected Task: Cooking")
                val cookingLevel = Skills.COOKING.level

                if (cookingLevel >= 25 && !taskAssigned) {
                    // Which Cooking Task Decided
                    if (rawSalmonCount >= 150) {
                        fishToCook = 331
                        bankLocation = "AlkharidWestBank"
                        rangeLocation = "AlkharidWestRange"
                        taskAssigned = true

                    }
                    else if (rawPikeCount >= 150) {
                        fishToCook = 349
                        bankLocation = "AlkharidWestBank"
                        rangeLocation = "AlkharidWestRange"
                        taskAssigned = true
                    }
                }
                if (cookingLevel >= 20 && !taskAssigned) {
                    // Which Cooking Task Decided
                    if (rawPikeCount >= 150) {
                        fishToCook = 349
                        bankLocation = "AlkharidWestBank"
                        rangeLocation = "AlkharidWestRange"
                        taskAssigned = true

                    }
                    else if (rawTroutCount >= 150) {
                        fishToCook = 335
                        bankLocation = "AlkharidWestBank"
                        rangeLocation = "AlkharidWestRange"
                        taskAssigned = true
                    }
                }
                if (cookingLevel >= 15 && !taskAssigned) {
                    // Which Cooking Task Decided
                    if (rawTroutCount >= 150) {
                        fishToCook = 335
                        bankLocation = "AlkharidWestBank"
                        rangeLocation = "AlkharidWestRange"
                        taskAssigned = true

                    }
                    else if (rawHerringCount >= 150) {
                        fishToCook = 345
                        bankLocation = "AlkharidWestBank"
                        rangeLocation = "AlkharidWestRange"
                        taskAssigned = true
                    }
                }
                if (!taskAssigned) {
                    // Which Cooking Task Decided
                    if (rawAnchoviesCount >= 150) {
                        fishToCook = 321
                        bankLocation = "AlkharidWestBank"
                        rangeLocation = "AlkharidWestRange"
                        taskAssigned = true

                    }
                    else if (rawSardineCount >= 150) {
                        fishToCook = 327
                        bankLocation = "AlkharidWestBank"
                        rangeLocation = "AlkharidWestRange"
                        taskAssigned = true
                    }
                    else if (rawShrimpCount >= 150) {
                        fishToCook = 2514
                        bankLocation = "AlkharidWestBank"
                        rangeLocation = "AlkharidWestRange"
                        taskAssigned = true
                    }
                    else if (rawCrayfishCount >= 150) {
                        fishToCook = 13435
                        bankLocation = "AlkharidWestBank"
                        rangeLocation = "AlkharidWestRange"
                        taskAssigned = true
                    }
                }
                if (!taskAssigned) {
                    Zezimax.Logger.log("Not enough raw food found. Re-Initializing...")
                    decision = 999
                    return
                }

            }


// FLETCHING
            7 -> {
                val willowCount = Bank.getItems().filter { it.id == 1519 }.sumOf { it.stackSize }
                val oakCount = Bank.getItems().filter { it.id == 1521 }.sumOf { it.stackSize }
                val logCount = Bank.getItems().filter { it.id == 1511 }.sumOf { it.stackSize }
                var taskAssigned = false
                Zezimax.Logger.log("Selected Task: Fletching")
                val fletchingLevel = Skills.FLETCHING.level

                if (fletchingLevel >= 35 && !taskAssigned) {
                    // Which Fletching Task Decided
                    if (willowCount >= 150) {
                        logToFletch = 1519
                        taskAssigned = true

                    }
                    else if (oakCount >= 150) {
                        logToFletch = 1521
                        taskAssigned = true
                    }
                }
                if (fletchingLevel >= 20 && !taskAssigned) {
                    // Which Fletching Task Decided
                    if (oakCount >= 150) {
                        logToFletch = 1519
                        taskAssigned = true

                    }
                    else if (logCount >= 150) {
                        logToFletch = 1511
                        taskAssigned = true
                    }
                }
                if (!taskAssigned) {
                    // Which Fletching Task Decided
                    if (logCount >= 150) {
                        logToFletch = 1511
                        taskAssigned = true

                    }
                }
                if (!taskAssigned) {
                    Zezimax.Logger.log("Not enough logs found. Re-Initializing...")
                    decision = 999
                    return
                }

                startFletching()





            }

        }
    }
}