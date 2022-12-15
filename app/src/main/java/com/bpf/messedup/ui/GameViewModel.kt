package com.bpf.messedup.ui


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bpf.messedup.data.MAX_NO_OF_WORDS
import com.bpf.messedup.data.SCORE_INCREASE
import com.bpf.messedup.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    var userGuess by mutableStateOf("")

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())

    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var currentWord: String

    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()

    init {
        resetGame()
    }

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

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(pickARandomWordAndShuffle())
    }


    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    fun checkUserGuess() {
        if (userGuess == (currentWord)) {
            val updateScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updateScore)
        } else {
            // User's guess is wrong, show an error
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
    }

    private fun updateGameState(updatedGameScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            //last round
            _uiState.update {
                it.copy(
                    isGuessedWordWrong = false,
                    currentWordCount = it.currentWordCount.inc(),
                    score = updatedGameScore,
                    isGameOver = true
                )
            }

        } else {
            _uiState.update {
                it.copy(
                    isGuessedWordWrong = false,
                    currentWordCount = it.currentWordCount.inc(),
                    currentMessedUpWord = pickARandomWordAndShuffle(),
                    score = updatedGameScore
                )
            }
        }
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        updateUserGuess("")
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

