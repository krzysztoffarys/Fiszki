package com.example.learnspanishwithcrys.other

data class Game(val status: GameStatus, val message: String?) {
    companion object {
        fun win(message: String?): Game {
            return Game(GameStatus.WIN, message)
        }
        fun lose(message: String?): Game {
            return Game(GameStatus.LOSE, message)
        }
        fun ongoing(message: String?): Game {
            return Game(GameStatus.ONGOING, message)
        }
    }
}

enum class GameStatus {
    WIN,
    LOSE,
    ONGOING
}