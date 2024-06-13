package net.botwithus

import net.botwithus.rs3.util.RandomGenerator

object DecisionTree {
    var decision: Int? = null

    fun makeRandomDecision() {
        decision = Navi.random.nextInt(2) // Adjust range if more tasks are added
        println("Decision made: $decision")
        when (decision) {
            0 -> {
                println("Selected Task: Mining")
                withdrawMiningSupplies("Rune ore box", 1)
            }

            1 -> {
                println("Selected Task: Smithing Ore")
                withdrawSmithingOreSupplies("Luminite" to null, "Runite ore" to null)
            }
            // Add more cases for other tasks here
        }
    }
}

/*

// THIS FILTERING STUFF MIGHT GO INTO THE .KT FILES FOR WHAT TO DO, TO KEEP IT SIMPLE LIKE ABOVE


            0 -> { // QUESTS
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



             1 -> { // MINING
                    println("Selected Task: Mining")
                    val miningLevel = Skills.MINING.level
                    if (miningLevel >= 50) {
                        if (bankContainsLessThan("Rune ore", 200)) {
                            // go mine rune ore
                        } else if (bankContainsLessThan("Luminite", 200)) {
                            // go mine luminite
                        }
                    } else if (miningLevel >= 40) {
                        if (bankContainsLessThan("Adamantite", 200)) {
                            // go mine adamantite
                        } else if (bankContainsLessThan("Luminite", 200)) {
                            // go mine luminite
                        }
                    } else if (miningLevel >= 30) {
                        if (bankContainsLessThan("Mithril", 200)) {
                            // go mine mithril
                        } else if (bankContainsLessThan("Coal", 300)) {
                            // go mine coal
                        }
                    } else if (miningLevel >= 20) {
                        if (bankContainsLessThan("Iron", 200)) {
                            // go mine iron
                        } else if (bankContainsLessThan("Coal", 300)) {
                            // go mine coal
                        }
                    } else {
                        if (bankContainsLessThan("Copper", 200)) {
                            // go mine copper
                        } else if (bankContainsLessThan("Tin", 200)) {
                            // go mine tin
                        }
                    }



                2 -> { // SMITHING ORE
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
 */