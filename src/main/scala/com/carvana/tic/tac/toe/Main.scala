package com.carvana.tic.tac.toe

import com.carvana.tic.tac.toe.game.{
  ClassicGameGrid,
  Game,
  GameBoard,
  GameGrid,
  Player
}
import com.carvana.tic.tac.toe.models.{Cell, Move, Position}

import scala.annotation.tailrec

/* NOTE:

The 3 Traits here are mainly for ease of hooking into Tests.
Please implement them as "defaults", for use in Main

 */

trait TicTacIO {

  /**
    *  Get moves for a player, perhaps from reading stdin?
    * @param player The player the Move is for
    * @return
    */
  def getMoveForPlayer(player: Player): Move = ???

  /**
    * A utility method to display something useful about the current Game
    * state to the world.
    * @param game
    */
  def displayGameState(game: Game): Unit = ???

}

/**
  * A trait that builds up the default, initial state of our game.
  */
trait GameSetUp {
  // Set Up
  val dimension: Int = 3
  val emptyCells: Seq[Cell] = ???
  val emptyGrid: GameGrid = ClassicGameGrid(dimension, emptyCells)
  val cleanBoard: GameBoard = ???
  val playerQueue: LazyList[Player] = ???
  val newGame: Game = ???
}

trait GamePlayLogic {

  /**
    * A tail-recursive method that plays a Game to completion, with input given
    * from some TicTacIO
    * @param game The game in play
    * @param ioOperator The method we are getting input to make Moves for a User
    * @return
    */
  final def playGame(game: Game)(ioOperator: TicTacIO): Option[Player] = ???

}

/**
  * Our Main entry point - nothing new here _needs_ to be implemented, beyond the Traits above!
  */
object Main extends App with GameSetUp with GamePlayLogic with TicTacIO {

  // Play the game
  playGame(newGame)(ioOperator = this) match {
    case Some(player) => println(s"\n---\n${player.displayName} wins!")
    case None         => println("\n---\nThe game ended in a tie!")
  }

}
