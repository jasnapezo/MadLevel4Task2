package com.example.madlevel4task2

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HistoryFragment : Fragment() {

    private var game = arrayListOf<Game>()
    private var gameAdapter = GameAdapter(game)

    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameUnitRepository = GameUnitRepository(requireContext())

        initRv()

        getShoppingListFromDatabase()
    }

    private fun initRv() {
        // Initialize the recycler view with a linear layout manager, adapter
        rvGames.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvGames.adapter = gameUnitAdapter
        rvGames.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_history, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            R.id.action_delete -> {
                clearResultsHistory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getShoppingListFromDatabase() {
        mainScope.launch {
            var gameUnits = withContext(Dispatchers.IO) {
                ArrayList(gameUnitRepository.getAllGameUnits())
            }

            this@HistoryFragment.gameUnits.clear()
            this@HistoryFragment.gameUnits.addAll(gameUnits)

            gameUnitAdapter.notifyDataSetChanged()
        }
    }

    private fun clearResultsHistory() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameUnitRepository.deleteAllGameUnits()
            }

            getShoppingListFromDatabase()
        }
    }
}