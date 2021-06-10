package com.carvana.tic.tac.toe.game

import com.carvana.tic.tac.toe.models.Move
import scala.util.{Failure, Success, Try}
import com.typesafe.scalalogging.LazyLogging

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
    extends Game
    with LazyLogging {

  override val currentPlayer: Player = playerQueue.head

  override def makeMoveForPlayer(
      player: Player,
      move: Move
  ): Try[Either[Option[Player], Game]] = {
    logger.debug(
      s"${currentPlayer.displayName} attempted move: " +
        s"Position (${move.position.row}, ${move.position.col}), " +
        s"Marker ${move.marker}"
    )

    if (gameBoard.isMoveValid(move)) {
      val nextGameBoard = gameBoard.makeMove(move)
      logger.debug("Move was successful")

      if (nextGameBoard.isGameOver) {
        logger.debug("The game is over!")
        Success(Left(nextGameBoard.winningMarker match {
          case Some(marker) =>
            val winner = playerQueue.find(player => player.marker == marker)
            // Matching the winner to determine what to log
            winner match {
              case Some(player) =>
                logger.debug(s"Player ${player.displayName} won!")
              case None =>
                logger.error(
                  s"A player for marker ${marker} could not be found"
                )
            }
            winner
          case None =>
            logger.debug("The game ended in a tie!")
            None
        }))
      } else {
        val nextGame = copy(
          gameBoard = nextGameBoard,
          playerQueue = playerQueue.tail :+ currentPlayer
        )
        Success(Right(nextGame))
      }
    } else {
      val errorMessage =
        s"${player.displayName} made an invalid move: " +
          s"Position (${move.position.row}, ${move.position.col}), " +
          s"Marker: ${move.marker}"
      logger.error(errorMessage)
      Failure(
        new TicTacUhOh(errorMessage)
      )
    }
  }
}
