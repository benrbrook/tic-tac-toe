package com.carvana.tic.tac.toe.game

import com.carvana.tic.tac.toe.models.{Marker, Move}
import com.typesafe.scalalogging.LazyLogging

/**
  * A trait representing an instance of Tic Tac Toe GameBoard.
  * A GameBoard generally interfaces interactions between a Game, and the underlying GameGrid.
  */
trait GameBoard {

  /**
    * The underlying GameGrid, which houses the Cell's, and any Marker placement
    */
  val grid: GameGrid

  /**
    * A logical helper to indication if the Game is over, and winningMarker is a valid response.
    */
  val isGameOver: Boolean

  /**
    * A holder of the clear winner, if any
    */
  val winningMarker: Option[Marker]

  /**
    * A logical helper to determine if the Move about to be made is valid.
    * @param move The Move about to be made.
    * @return
    */
  def isMoveValid(move: Move): Boolean

  /**
    * An action to make a Move, and place a Marker into the GameGrid, resulting in a new state for the GameBoard
    * @param move The Move to make
    * @return
    */
  def makeMove(move: Move): GameBoard
}

/**
  * A classic implementation of a Tic Tac Toe GameBoard
  * @param grid
  */
case class ClassicGameBoard(grid: GameGrid) extends GameBoard with LazyLogging {

  override val isGameOver: Boolean = grid.winningMarker match {
    case Some(_) =>
      logger.debug("Game is over with a winner")
      true
    case None =>
      grid.unplacedPositions match {
        // A game that has no winner with no unplaced positions has ended in a tie
        case 0 =>
          logger.debug("Game is over with a tie")
          true
        case _ => false
      }
  }

  override val winningMarker: Option[Marker] = grid.winningMarker

  override def isMoveValid(move: Move): Boolean = {
    val rowWithinBounds = 0 until grid.dimension contains move.position.col
    val colWithinBounds = 0 until grid.dimension contains move.position.row
    !grid.cellHasMarker(
      move.position
    ) && rowWithinBounds && colWithinBounds
  }

  override def makeMove(move: Move): GameBoard = {
    val nextGrid = grid.placeMove(move)
    copy(grid = nextGrid)
  }

}
