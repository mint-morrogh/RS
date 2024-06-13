package net.botwithus

import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.rs3.game.skills.Skills
import net.botwithus.rs3.util.RandomGenerator

object DecisionTree {
    var decision: Int? = null

    fun makeRandomDecision() {
        decision = Navi.random.nextInt(2) // Adjust range if more tasks are added
        println("Decision made: $decision")

        var mineLocation = ""
        var oreBoxName = ""
        var oreToCollect = ""
        var rockToMine = ""
        var actionToMine = ""

        when (decision) {
            0 -> { // MINING
                val runeCount = Bank.getItems().filter { it.name == "Runite ore" }.sumOf { it.stackSize }
                val luminiteCount = Bank.getItems().filter { it.name == "Luminite" }.sumOf { it.stackSize }
                val adamantiteCount = Bank.getItems().filter { it.name == "Adamantite ore" }.sumOf { it.stackSize }
                val mithrilCount = Bank.getItems().filter { it.name == "Mithril ore" }.sumOf { it.stackSize }
                val coalCount = Bank.getItems().filter { it.name == "Coal" }.sumOf { it.stackSize }
                val ironCount = Bank.getItems().filter { it.name == "Iron ore" }.sumOf { it.stackSize }
                val tinCount = Bank.getItems().filter { it.name == "Tin ore" }.sumOf { it.stackSize }
                val copperCount = Bank.getItems().filter { it.name == "Copper ore" }.sumOf { it.stackSize }
                println("Selected Task: Mining")
                val miningLevel = Skills.MINING.level

                if (miningLevel >= 50) {
                    // Which Tools Decided
                    oreBoxName = "Rune ore box"

                    // Which Mine Task Decided
                    if (runeCount <= 300) {
                        mineLocation = "MiningGuild"
                        oreToCollect = "Runite ore"
                        rockToMine = "Runite rock"
                        actionToMine = "Mine"
                        // go mine rune ore
                    } else if (luminiteCount <= 300) {
                        mineLocation = "FaladorLuminite"
                        oreToCollect = "Luminite"
                        rockToMine = "Luminite rock"
                        actionToMine = "Mine"
                        // go mine luminite
                    }
                } else if (miningLevel >= 40) {
                    // Which Tools Decided
                    oreBoxName = "Adamant ore box"

                    // Which Mine Task Decided
                    if (adamantiteCount <= 300) {
                        mineLocation = "VarrockEastMine"
                        oreToCollect = "Adamantite ore"
                        rockToMine = "Adamantite rock"
                        actionToMine = "Mine"
                        // go mine adamantite ore
                    } else if (luminiteCount <= 300) {
                        mineLocation = "FaladorLuminite"
                        oreToCollect = "Luminite"
                        rockToMine = "Luminite rock"
                        actionToMine = "Mine"
                        // go mine luminite
                    }
                } // else if (miningLevel >= 30) { ........

                    // finally at the end:
                    // } else {
                    //                        if (bankContainsLessThan("Copper", 200)) {
                    //                            // go mine copper
                    //                        } else if (bankContainsLessThan("Tin", 200)) {
                    //                            // go mine tin
                    //                        }
                    //                    }



                withdrawMiningSupplies(oreBoxName, 1)
            }





            1 -> { // SMIHTING ORE
                println("Selected Task: Smithing Ore")
                withdrawSmithingOreSupplies("Luminite" to null, "Runite ore" to null)
            }
            // Add more cases for other tasks here
        }
    }
}

/*




                1 -> { // SMITHING ORE
                        println("Selected Task: Smithing Ore")
                        val smithingLevel = Skills.SMITHING.level

                        if (smithingLevel >= 50) {
                            if (bank contains at least ("Rune ore", 100) && bank contains at least ("Luminite", 100)) {
                                // go smith rune bars
                            } else if (bank contains at least ("adamantite ore", 100) && bank contains at least ("Luminite", 100)) {
                                // go smith adamantite bars
                            } else if (bank contains at least ("mithril ore", 100) && bank contains at least ("Coal", 100)) {
                                // go smith mithril bars
                            }
                        } else if (smithingLevel >= 40) {
                            if (bank contains at least ("adamantite ore", 100) && bank contains at least ("Luminite", 100)) {
                                // go smith adamantite bars
                            } else if (bank contains at least ("mithril ore", 100) && bank contains at least ("Coal", 100)) {
                                // go smith mithril bars
                            }
                        } else if (smithingLevel >= 30) {
                            if (bank contains at least ("mithril ore", 100) && bank contains at least ("Luminite", 100)) {
                                // go smith mithril  bars
                            } else if (bank contains at least ("iron ore", 100) && bank contains at least ("Coal", 100)) {
                                // go smith steel bars
                            }
                        } else if (smithingLevel >= 10) {
                            if (bank contains at least ("iron ore", 100) {
                                // go smith iron bars
                            } else if (bank contains at least ("copper", 100) && bank contains at least ("tin", 100)) {
                                // go smith bronze bars
                            }
                        } else {
                            if (bank contains at least ("copper", 100) && bank contains at least ("tin", 100)) {
                                // go smith bronze bars
                                }
                        // otherwise if all are false make another decision


}

                2 -> { // QUESTS
                        println("Selected Task: Questing")
                        if quest 10 complete {
                        println("completed quest 10!")
                        } else { quest 10 (start where left off) }
                        elif quest 9 complete {
                        println("completed quest 9!")
                        } else { quest 9 (start where left off) }
                        elif quest 8 complete {
                        println("completed quest 8!")
                        } else { quest 8 (start where left off) }
                        elif quest 7 complete {
                        println("completed quest 8!")
                        } else { quest 17(start where left off) }
                        ...  quests here
                    }
 */