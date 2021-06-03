package com.carvana.tic.tac.toe.game

import com.carvana.tic.tac.toe.models.{Cell, Position, Marker, Move}

/**
  * A trait representing the state of a GameGrid. The GameGrid handles much of the logic
  * for Cells, and their contents.
  */
trait GameGrid {

  /**
    * The dimension of our grid, which is classically 3
    */
  val dimension: Int

  /**
    * A Collection of Cells, representing all possible Positions on the GameGrid.
    */
  val cells: Seq[Cell]

  /**
    * The number of un-Marked Cells
    */
  val unplacedPositions: Int

  /**
    * A logical helper to determine if a cell at a particular Position has been Marked or not.
    * @param position The position in the GameGrid.
    * @return
    */
  def cellHasMarker(position: Position): Boolean

  /**
    * A holder of the clear winner, if any. If this is None it means that the game is not over or it is a draw
    */
  val winningMarker: Option[Marker]

  /**
    * An action to place a Marker at a Position via a Move, resulting in a new state for the GameGrid.
    * @param move The Move to place
    * @return
    */
  def placeMove(move: Move): GameGrid
}

/**
  *
  * @param dimension The dimension of our GameGrid, defaulted to 3
  * @param cells The Collection of Cells, representing all possible Positions on the GameGrid.
  */
case class ClassicGameGrid(dimension: Int = 3, cells: Seq[Cell])
    extends GameGrid {

  override val unplacedPositions: Int =
    dimension * dimension - cells.count(cell => cellHasMarker(cell.position))

  override val winningMarker: Option[Marker] = {
    val rows = cells.groupBy(_.position.row).valuesIterator.toSeq
    val columns = cells.groupBy(_.position.col).valuesIterator.toSeq
    val diagonal = cells.filter(cell => cell.position.row == cell.position.col)
    val inverseDiagonal = cells.filter(cell =>
      cell.position.row + cell.position.col == dimension - 1
    )

    val possibleWinners = (rows ++ columns :+ diagonal :+ inverseDiagonal)
      .filter(hasWinningMarker)
    possibleWinners.headOption match {
      case Some(cells) => cells.head.placedMarker
      case None        => None
    }
  }

  override def cellHasMarker(position: Position): Boolean =
    cells.find(cell => cell.position == position) match {
      case Some(_) => true
      case None    => false
    }

  override def placeMove(move: Move): GameGrid = {
    val newCells = cells.map(cell =>
      cell.position match {
        case move.position => cell.copy(placedMarker = Some(move.marker))
        case _             => cell
      }
    )
    copy(cells = newCells)
  }

  private def hasWinningMarker(cells: Seq[Cell]) = {
    val firstMarker = cells.head.placedMarker
    cells.forall(cell =>
      cell.placedMarker == firstMarker && cell.placedMarker.isDefined
    )
  }
}
