package com.carvana.tic.tac.toe

import com.carvana.tic.tac.toe.game.{
  ClassicGame,
  ClassicGameBoard,
  ClassicGameGrid,
  Game,
  GameBoard,
  GameGrid,
  Player,
  StandardPlayer
}
import com.carvana.tic.tac.toe.models.{Cell, Move, O, Position, X}

import scala.io.StdIn.readLine
import scala.util.{Failure, Success}
import com.typesafe.scalalogging.LazyLogging

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
  def getMoveForPlayer(player: Player): Move = {
    println(
      s"Enter a move for player ${player.displayName} with marker ${if (player.marker == X) "X"
      else "O"}"
    )
    print("Enter position as row,col: ")

    readLine.replaceAll("\\s", "").split(",") match {
      case Array(row, col) if row.matches("\\d+") && col.matches("\\d+") =>
        Move(Position(row.toInt, col.toInt), player.marker)
      case _ => {
        println("Invalid input, please enter again")
        getMoveForPlayer(player)
      }
    }
  }

  /**
    * A utility method to display something useful about the current Game
    * state to the world.
    * @param game
    */
  def displayGameState(game: Game): Unit = {
    println(s"Current player: ${game.currentPlayer.displayName}")

    val dimension = game.gameBoard.grid.dimension
    val cells = game.gameBoard.grid.cells
    val gridWidth = dimension * 4 + 1
    val spacingPrefix = "    "
    val gridBoundary = spacingPrefix + "-".repeat(gridWidth)
    val rowMarkers =
      spacingPrefix + "| " + (0 until dimension).mkString(" | ") + " |"

    println(rowMarkers)
    println(gridBoundary)
    cells
      .grouped(dimension)
      .toSeq
      .zipWithIndex
      .foreach {
        case (line, index) => {
          print(s"| $index |")
          line.foreach(cell => {
            val cellContents = cell.placedMarker match {
              case Some(marker) => if (marker == X) "X" else "O"
              case None         => " "
            }
            print(s" ${cellContents} |")
          })
          println("")
        }
      }
    println(gridBoundary)
  }

}

/**
  * A trait that builds up the default, initial state of our game.
  */
trait GameSetUp {
  // Set Up
  val dimension: Int = 3
  val emptyCells: Seq[Cell] = for {
    row <- 0 until dimension
    col <- 0 until dimension
  } yield Cell(Position(row, col), None)
  val emptyGrid: GameGrid = ClassicGameGrid(cells = emptyCells)
  val cleanBoard: GameBoard = ClassicGameBoard(emptyGrid)
  val playerQueue: LazyList[Player] =
    LazyList(StandardPlayer("John", X), StandardPlayer("Jane", O))
  val newGame: Game = ClassicGame(cleanBoard, playerQueue)
}

trait GamePlayLogic extends LazyLogging {

  /**
    * A tail-recursive method that plays a Game to completion, with input given
    * from some TicTacIO
    * @param game The game in play
    * @param ioOperator The method we are getting input to make Moves for a User
    * @return
    */
  final def playGame(game: Game)(ioOperator: TicTacIO): Option[Player] = {
    ioOperator.displayGameState(game)
    val currentPlayer = game.currentPlayer
    logger.debug(s"Current player: ${currentPlayer.displayName}")

    val move = ioOperator.getMoveForPlayer(currentPlayer)
    val moveResult = game.makeMoveForPlayer(currentPlayer, move)

    moveResult match {
      case Success(validMove) => {
        validMove match {
          case Left(winner) => {
            ioOperator.displayGameState(game)
            winner
          }
          case Right(nextGame) => playGame(nextGame)(ioOperator)
        }
      }
      case Failure(ex) => {
        println(ex.getMessage)
        playGame(game)(ioOperator)
      }
    }
  }

}

/**
  * Our Main entry point - nothing new here _needs_ to be implemented, beyond the Traits above!
  */
object Main
    extends App
    with GameSetUp
    with GamePlayLogic
    with TicTacIO
    with LazyLogging {
  logger.info("Starting game...")
  // Play the game
  playGame(newGame)(ioOperator = this) match {
    case Some(player) => println(s"\n---\n${player.displayName} wins!")
    case None         => println("\n---\nThe game ended in a tie!")
  }

}
