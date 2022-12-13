package com.bpf.messedup.ui


import androidx.lifecycle.ViewModel
import com.bpf.messedup.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())

    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var currentWord: String

    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()

    private fun pickARandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before

        currentWord = allWords.random()
        return if (usedWords.contains(currentWord)) {
            pickARandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            shuffleCurrentWord(currentWord)
        }
    }

    private fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(pickARandomWordAndShuffle())
    }

    init {
        resetGame()
    }
}

private fun shuffleCurrentWord(word: String): String {
    val tempWord = word.toCharArray()
    tempWord.shuffle()
    while (tempWord.equals(word)) {
        tempWord.shuffle()
    }
    return String(tempWord)
}

