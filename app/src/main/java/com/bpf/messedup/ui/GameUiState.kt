package com.bpf.messedup.ui

data class GameUiState(
    val currentMessedUpWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val currentWordCount : Int = 0,
    val score: Int = 0,
)
