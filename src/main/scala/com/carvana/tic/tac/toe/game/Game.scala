package com.carvana.tic.tac.toe.game

import com.carvana.tic.tac.toe.models.Move

import scala.util.{Failure, Success, Try}

/**
  * An trait representing an instance of a Tic Tac Toe Game.
  *  Game generally handles the interaction between Players and a GameBoard
  */
trait Game {

  /**
    * The current state/instance of the GameBoard in play
    */
  val gameBoard: GameBoard

  /**
    * A stream of Players, in order of turn
    */
  val playerQueue: LazyList[Player]

  /**
    * The current Player, as determined from the playerQueue
    */
  val currentPlayer: Player

  /**
    * An action to make a Move for a Player, thus altering the state of the Game.
    * @param player
    * @param move
    * @return Upon Success, the move with result in either an option of a Player (winner if defined,
    *         tie if undefined) or the current state of the Game. Keep making moves until the Game is over :-)
    *         Upon Failure, perhaps handle and retry with the previous state ;-)
    * @throws TicTacUhOh
    */
  def makeMoveForPlayer(
      player: Player,
      move: Move
  ): Try[Either[Option[Player], Game]]
}

class TicTacUhOh(message: String) extends Exception(message)

/**
  * A classic instance of a Tic Tac Toe game
  * @param gameBoard The current state/instance of the GameBoard
  * @param playerQueue A stream of Players, in turn order
  */
case class ClassicGame(gameBoard: GameBoard, playerQueue: LazyList[Player])
    extends Game {

  override val currentPlayer: Player = playerQueue.head

  override def makeMoveForPlayer(
      player: Player,
      move: Move
  ): Try[Either[Option[Player], Game]] = {
    if (gameBoard.isMoveValid(move)) {
      val moveResult = if (gameBoard.isGameOver) {
        val winningPlayer = gameBoard.winningMarker match {
          case Some(marker) =>
            playerQueue.find(player => player.marker == marker)
          case None => None
        }
        Left(winningPlayer)
      } else {
        val newGameBoard = gameBoard.makeMove(move)
        val newGame = copy(
          gameBoard = newGameBoard,
          playerQueue = playerQueue.tail :+ currentPlayer
        )
        Right(newGame)
      }
      Success(moveResult)
    } else {
      Failure(new TicTacUhOh("You tried an invalid move"))
    }
  }
}
