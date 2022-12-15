package com.bpf.messedup.ui

data class GameUiState(
    val currentMessedUpWord: String = "",
    val isGuessedWordWrong: Boolean = false,
)
