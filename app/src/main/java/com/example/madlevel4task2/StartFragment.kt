package com.example.madlevel4task2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

const val WIN = "win"
const val DRAW = "draw"
const val LOSE = "lose"

class StartFragment : Fragment() {

    private lateinit var gameRepository: GameRepository

    private val mainScope = CoroutineScope(Dispatchers.Main)

    // id corresponding to choice made
    private val imageId = arrayListOf(R.drawable.rock, R.drawable.paper, R.drawable.scissors)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameUnitRepository = GameUnitRepository(requireContext())

        initViews()
    }

    private fun initViews() {
        // click events
        imgBtn_rock.setOnClickListener { playGame(ROCK) }
        imgBtn_paper.setOnClickListener { playGame(PAPER) }
        imgBtn_scissors.setOnClickListener { playGame(SCISSOR) }

        getLastPlayedGame()
    }

    private fun getLastPlayedGame() {
        mainScope.launch {
            val lastGameUnit = withContext(Dispatchers.IO) {
                val allGameUnits = gameUnitRepository.getAllGameUnits()

                if (allGameUnits.isEmpty()) null
                else gameUnitRepository.getAllGameUnits().last()
            }

            if (lastGameUnit != null) {
                txtResult.text = getResultString(lastGameUnit.result)
                imgResultPlayer.setBackgroundResource(imageId[lastGameUnit.playerChoice.ordinal])
                imgResultComputer.setBackgroundResource(imageId[lastGameUnit.computerChoice.ordinal])
            }
        }
    }

    private fun playGame(choice: Choice) {
        // randomly choose for computer 0, 1 or 2 and convert into Choice
        val computerChoice = enumValues<Choice>()[Random.nextInt(0, 3)]

        // check who wins
        val result = getGameResult(computerChoice, choice)

        // modify UI
        txtResult.text = getResultString(result)
        imgResultPlayer.setBackgroundResource(imageId[choice.ordinal])
        imgResultComputer.setBackgroundResource(imageId[computerChoice.ordinal])

        // add to database
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameUnitRepository.insertGameUnit(GameUnit(result, Date(), choice, computerChoice))
            }
        }
    }

    private fun getGameResult(computerChoice: Choice, choice: Choice): String {
        return if (computerChoice == choice)
            DRAW
        else if (computerChoice == ROCK && choice == SCISSOR || computerChoice == SCISSOR && choice == PAPER || computerChoice == PAPER && choice == ROCK)
            LOSE
        else
            WIN
    }

    private fun getResultString(result: String): String {
        return when (result) {
            WIN -> getString(R.string.win)
            LOSE -> getString(R.string.lose)
            else -> getString(R.string.draw)
        }
    }
