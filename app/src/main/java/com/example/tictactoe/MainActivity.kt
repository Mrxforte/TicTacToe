package com.example.tictactoe

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get player selection from intent
        currentPlayer = intent.getIntExtra("PLAYER_SELECTION", 1) // Default to X if not specified

        // Set click listeners for all buttons
        binding.btn1.setOnClickListener { onCellClicked(0, 0) }
        binding.btn2.setOnClickListener { onCellClicked(0, 1) }
        binding.btn3.setOnClickListener { onCellClicked(0, 2) }
        binding.btn4.setOnClickListener { onCellClicked(1, 0) }
        binding.btn5.setOnClickListener { onCellClicked(1, 1) }
        binding.btn6.setOnClickListener { onCellClicked(1, 2) }
        binding.btn7.setOnClickListener { onCellClicked(2, 0) }
        binding.btn8.setOnClickListener { onCellClicked(2, 1) }
        binding.btn9.setOnClickListener { onCellClicked(2, 2) }

        // Update the turn indicator based on the starting player
        updateTurnIndicator()
    }

    @SuppressLint("SetTextI18n")
    private fun onCellClicked(row: Int, col: Int) {
        if (!gameActive || boardState[row][col] != 0) {
            return
        }

        val button = when {
            row == 0 && col == 0 -> binding.btn1
            row == 0 && col == 1 -> binding.btn2
            row == 0 && col == 2 -> binding.btn3
            row == 1 && col == 0 -> binding.btn4
            row == 1 && col == 1 -> binding.btn5
            row == 1 && col == 2 -> binding.btn6
            row == 2 && col == 0 -> binding.btn7
            row == 2 && col == 1 -> binding.btn8
            else -> binding.btn9
        }

        boardState[row][col] = currentPlayer
        button.setBackgroundResource(if (currentPlayer == 1) R.drawable.x_button_game else R.drawable.zero_game_button)
//        button.setTextColor(if (currentPlayer == 1) Color.BLUE else Color.RED)

        if (checkForWin()) {
            gameActive = false
            val winner = if (currentPlayer == 1) "X" else "O"
            binding.turnIndicator.text = "Player $winner wins!"
            var intent = Intent(this, WinnerScreen::class.java)
            startActivity(intent)
            highlightWinningCells()
        } else if (isBoardFull()) {
            gameActive = false
            var intent = Intent(this, DrawScreen::class.java)
            startActivity(intent)
            binding.turnIndicator.text = "Game ended in a draw!"
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
                getButton(position1[0], position1[1]).setBackgroundColor(Color.GREEN)
                getButton(position2[0], position2[1]).setBackgroundColor(Color.GREEN)
                getButton(position3[0], position3[1]).setBackgroundColor(Color.GREEN)
                break
            }
        }
    }

    private fun getButton(row: Int, col: Int) = when {
        row == 0 && col == 0 -> binding.btn1
        row == 0 && col == 1 -> binding.btn2
        row == 0 && col == 2 -> binding.btn3
        row == 1 && col == 0 -> binding.btn4
        row == 1 && col == 1 -> binding.btn5
        row == 1 && col == 2 -> binding.btn6
        row == 2 && col == 0 -> binding.btn7
        row == 2 && col == 1 -> binding.btn8
        else -> binding.btn9
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

    @SuppressLint("SetTextI18n")
    private fun updateTurnIndicator() {
        binding.turnIndicator.text =
            if (currentPlayer == 1) "Player X's Turn" else "Player O's Turn"
    }

    @SuppressLint("SetTextI18n")
    private fun resetGame() {
        currentPlayer =
            intent.getIntExtra("PLAYER_SELECTION", 1) // Reset to original player selection
        gameActive = true
        boardState = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(0, 0, 0),
            intArrayOf(0, 0, 0)
        )

        binding.btn1.text = ""
        binding.btn2.text = ""
        binding.btn3.text = ""
        binding.btn4.text = ""
        binding.btn5.text = ""
        binding.btn6.text = ""
        binding.btn7.text = ""
        binding.btn8.text = ""
        binding.btn9.text = ""

        binding.btn1.setBackgroundColor(Color.parseColor("#ECECEC"))
        binding.btn2.setBackgroundColor(Color.parseColor("#ECECEC"))
        binding.btn3.setBackgroundColor(Color.parseColor("#ECECEC"))
        binding.btn4.setBackgroundColor(Color.parseColor("#ECECEC"))
        binding.btn5.setBackgroundColor(Color.parseColor("#ECECEC"))
        binding.btn6.setBackgroundColor(Color.parseColor("#ECECEC"))
        binding.btn7.setBackgroundColor(Color.parseColor("#ECECEC"))
        binding.btn8.setBackgroundColor(Color.parseColor("#ECECEC"))
        binding.btn9.setBackgroundColor(Color.parseColor("#ECECEC"))

        updateTurnIndicator()
    }
}