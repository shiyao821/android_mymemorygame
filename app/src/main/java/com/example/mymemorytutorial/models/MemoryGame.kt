package com.example.mymemorytutorial.models

import com.example.mymemorytutorial.utils.DEFAULT_ICONS

class MemoryGame(
    private val boardSize: BoardSize,
    private val gameImages: List<String>?) {
    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var indexOfSingleSelectedCard: Int? = null
    private var numCardsFlips = 0

    init {
        if (gameImages == null) {
            val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages = (chosenImages + chosenImages).shuffled()
            cards = randomizedImages.map{ MemoryCard(it) }
        } else {
            val randomizedImages = (gameImages + gameImages).shuffled()
            cards = randomizedImages.map{ MemoryCard(it.hashCode(), it) }
        }
    }

    /**
     * @return boolean of whether match was found
     */
    fun flipCard(position: Int): Boolean {
        numCardsFlips++
        val card = cards[position]
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(pos1: Int, pos2: Int): Boolean {
        if (cards[pos1].identifier != cards[pos2].identifier) {
            return false
        }
        cards[pos1].isMatched = true
        cards[pos2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardsFlips / 2
    }
}