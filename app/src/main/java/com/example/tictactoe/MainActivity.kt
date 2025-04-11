package com.example.tictactoe

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var turnIndicator: TextView
    private lateinit var resetButton: Button
    private lateinit var exitButton: Button
    private lateinit var buttons: Array<Array<Button>>

    private var currentPlayer = 1 // 1 for X, 2 for O
    private var gameActive = true
    private var boardState = arrayOf(
        intArrayOf(0, 0, 0),
        intArrayOf(0, 0, 0),
        intArrayOf(0, 0, 0)
    )

    private val winningPositions = arrayOf(
        // Rows
        arrayOf(intArrayOf(0, 0), intArrayOf(0, 1), intArrayOf(0, 2)),
        arrayOf(intArrayOf(1, 0), intArrayOf(1, 1), intArrayOf(1, 2)),
        arrayOf(intArrayOf(2, 0), intArrayOf(2, 1), intArrayOf(2, 2)),
        // Columns
        arrayOf(intArrayOf(0, 0), intArrayOf(1, 0), intArrayOf(2, 0)),
        arrayOf(intArrayOf(0, 1), intArrayOf(1, 1), intArrayOf(2, 1)),
        arrayOf(intArrayOf(0, 2), intArrayOf(1, 2), intArrayOf(2, 2)),
        // Diagonals
        arrayOf(intArrayOf(0, 0), intArrayOf(1, 1), intArrayOf(2, 2)),
        arrayOf(intArrayOf(0, 2), intArrayOf(1, 1), intArrayOf(2, 0))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        turnIndicator = findViewById(R.id.turnIndicator)
        resetButton = findViewById(R.id.resetButton)
        exitButton = findViewById(R.id.exitButton)

        // Initialize buttons array
        buttons = arrayOf(
            arrayOf(findViewById(R.id.btn1), findViewById(R.id.btn2), findViewById(R.id.btn3)),
            arrayOf(findViewById(R.id.btn4), findViewById(R.id.btn5), findViewById(R.id.btn6)),
            arrayOf(findViewById(R.id.btn7), findViewById(R.id.btn8), findViewById(R.id.btn9))
        )

        // Set click listeners for all buttons
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].setOnClickListener {
                    onCellClicked(i, j)
                }
            }
        }

        resetButton.setOnClickListener {
            resetGame()
        }

        exitButton.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onCellClicked(row: Int, col: Int) {
        if (!gameActive || boardState[row][col] != 0) {
            return
        }

        boardState[row][col] = currentPlayer
        buttons[row][col].text = if (currentPlayer == 1) "X" else "O"
        buttons[row][col].setTextColor(if (currentPlayer == 1) Color.BLUE else Color.RED)

        if (checkForWin()) {
            gameActive = false
            val winner = if (currentPlayer == 1) "X" else "O"
            turnIndicator.text = "Player $winner wins!"
            Toast.makeText(this, "Player $winner wins!", Toast.LENGTH_SHORT).show()
            highlightWinningCells()
        } else if (isBoardFull()) {
            gameActive = false
            turnIndicator.text = "Game ended in a draw!"
            Toast.makeText(this, "Game ended in a draw!", Toast.LENGTH_SHORT).show()
        } else {
            currentPlayer = if (currentPlayer == 1) 2 else 1
            updateTurnIndicator()
        }
    }

    private fun checkForWin(): Boolean {
        for (positions in winningPositions) {
            val position1 = positions[0]
            val position2 = positions[1]
            val position3 = positions[2]

            if (boardState[position1[0]][position1[1]] != 0 &&
                boardState[position1[0]][position1[1]] == boardState[position2[0]][position2[1]] &&
                boardState[position1[0]][position1[1]] == boardState[position3[0]][position3[1]]
            ) {
                return true
            }
        }
        return false
    }

    private fun highlightWinningCells() {
        for (positions in winningPositions) {
            val position1 = positions[0]
            val position2 = positions[1]
            val position3 = positions[2]

            if (boardState[position1[0]][position1[1]] != 0 &&
                boardState[position1[0]][position1[1]] == boardState[position2[0]][position2[1]] &&
                boardState[position1[0]][position1[1]] == boardState[position3[0]][position3[1]]
            ) {

                buttons[position1[0]][position1[1]].setBackgroundColor(Color.GREEN)
                buttons[position2[0]][position2[1]].setBackgroundColor(Color.GREEN)
                buttons[position3[0]][position3[1]].setBackgroundColor(Color.GREEN)
                break
            }
        }
    }

    private fun isBoardFull(): Boolean {
        for (i in 0..2) {
            for (j in 0..2) {
                if (boardState[i][j] == 0) {
                    return false
                }
            }
        }
        return true
    }

    private fun updateTurnIndicator() {
        turnIndicator.text = if (currentPlayer == 1) "Player X's Turn" else "Player O's Turn"
    }

    private fun resetGame() {
        currentPlayer = 1
        gameActive = true
        boardState = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(0, 0, 0),
            intArrayOf(0, 0, 0)
        )

        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].text = ""
                buttons[i][j].setBackgroundColor(Color.WHITE)
            }
        }

        updateTurnIndicator()
    }
}