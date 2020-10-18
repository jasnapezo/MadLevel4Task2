package com.example.madlevel4task2

import androidx.room.*

@Dao
interface GameDao {
    @Query("SELECT * FROM game_table")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(product: Game)

    @Delete
    suspend fun deleteGame(product: Game)

    @Query("DELETE FROM game_table")
    suspend fun deleteAllGames()

    @Query("SELECT COUNT('result') FROM game_table where result = 'win'")
    suspend fun getWins(): Int

    @Query("SELECT COUNT('result') FROM game_table where result ='draw'")
    suspend fun getDraws(): Int

    @Query("SELECT COUNT('result') FROM game_table where result ='lose'")
    suspend fun getLose(): Int
}