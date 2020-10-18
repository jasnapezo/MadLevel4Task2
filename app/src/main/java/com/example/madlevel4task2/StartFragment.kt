package com.example.madlevel4task2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_start.*
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
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameRepository = GameRepository(requireContext())

        initViews()
    }

    private fun initViews() {
        // statistics
        setStatistics()

        // click events
        imageButton_rock.setOnClickListener { playGame(Choice.ROCK) }
        imageButton_paper.setOnClickListener { playGame(Choice.PAPER) }
        imageButton_scissors.setOnClickListener { playGame(Choice.SCISSOR) }

        getLastPlayedGame()
    }

    private fun getLastPlayedGame() {
        mainScope.launch {
            val lastGame = withContext(Dispatchers.IO) {
                val allGame = gameRepository.getAllGames()

                if (allGame.isEmpty()) null
                else gameRepository.getAllGames().last()
            }

            if (lastGame != null) {
                textResult.text = getResultString(lastGame.result)
                imageResultPlayer.setBackgroundResource(imageId[lastGame.playerChoice.ordinal])
                imageResultComputer.setBackgroundResource(imageId[lastGame.computerChoice.ordinal])
            }
        }
    }

    private fun playGame(choice: Choice) {
        val computerChoice = enumValues<Choice>()[Random.nextInt(0, 3)]

        val result = getGameResult(computerChoice, choice)

        textResult.text = getResultString(result)
        imageResultPlayer.setBackgroundResource(imageId[choice.ordinal])
        imageResultComputer.setBackgroundResource(imageId[computerChoice.ordinal])

        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(Game(result, Date(), choice, computerChoice))
            }
        }

        setStatistics()

    }

    private fun getGameResult(computerChoice: Choice, choice: Choice): String {
        return if (computerChoice == choice)
            DRAW
        else if (computerChoice == Choice.ROCK && choice == Choice.SCISSOR || computerChoice == Choice.SCISSOR && choice == Choice.PAPER || computerChoice == Choice.PAPER && choice == Choice.ROCK)
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

    private fun setStatistics() {
        mainScope.launch {
            val statistics = withContext(Dispatchers.IO) {
                getString(R.string.win_draw_lose, gameRepository.getWins(), gameRepository.getDraws(), gameRepository.getLoses())
            }
            TextViewStatisticsResult.text = statistics
        }
    }
}
