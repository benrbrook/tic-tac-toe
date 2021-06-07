package com.carvana.tic.tac.toe.game

import com.carvana.tic.tac.toe.GameSetUp
import com.carvana.tic.tac.toe.models.{Cell, Move, O, Position, X}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class GameGridSpec extends AnyFlatSpec with should.Matchers with GameSetUp {

  "A (empty) GameGrid" should "contain dimension^2 cells" in {
    assert(emptyGrid.cells.length == emptyGrid.dimension * emptyGrid.dimension)
  }

  it should "contain all empty cells" in {
    assert(
      emptyGrid.cells.forall(cell => !emptyGrid.cellHasMarker(cell.position))
    )
  }

  it should "contain dimension^2 unplaced positions" in {
    assert(
      emptyGrid.unplacedPositions == emptyGrid.dimension * emptyGrid.dimension
    )
  }

  "A GameGrid" should "identify a cell with a marker" in {
    val gameGrid = ClassicGameGrid(1, Seq(Cell(Position(0, 0), Some(X))))
    assert(gameGrid.cellHasMarker(Position(0, 0)))
  }

  it should "be able to place a move and return the new state" in {
    val newGameGrid = emptyGrid.placeMove(Move(Position(0, 0), O))
    assert(newGameGrid != emptyGrid)
    assert(newGameGrid.cellHasMarker(Position(0, 0)))
    assert(newGameGrid.cells.head.placedMarker.contains(O))
  }

  it should "identify the number of unplaced positions" in {
    val gameGrid = ClassicGameGrid(1, Seq(Cell(Position(0, 0), Some(X))))
    assert(gameGrid.unplacedPositions == 0)
  }

  it should "identify a winning marker" in {
    val winningCells = for {
      row <- 0 until dimension
      col <- 0 until dimension
    } yield Cell(Position(row, col), if (row == 0) Some(X) else None)
    val gameGrid = ClassicGameGrid(dimension, winningCells)
    assert(gameGrid.winningMarker.contains(X))
  }
}
