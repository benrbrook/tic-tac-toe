package com.carvana.tic.tac.toe.game

import com.carvana.tic.tac.toe.GameSetUp
import com.carvana.tic.tac.toe.models.{Cell, Move, O, Position, X}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class GameBoardSpec extends AnyFlatSpec with should.Matchers with GameSetUp {

  "A (clean) GameBoard" should "not be over before the game starts" in {
    assert(!cleanBoard.isGameOver)
  }

  it should "should not have a declared winner before the game starts" in {
    assert(cleanBoard.winningMarker.isEmpty)
  }

  trait NextGameBoard {
    val nextGameBoard: GameBoard = cleanBoard.makeMove(Move(Position(0, 0), X))
  }

  "A GameBoard" should "be able to make any move on the GameGrid, for either X or O" in new NextGameBoard {
    nextGameBoard shouldBe a[GameBoard]
  }

  it should "be able to make a move, resulting in a new and different state" in new NextGameBoard {
    assert(nextGameBoard != cleanBoard)
  }

  it should "identify moves that are not valid because the spot is occupied" in new NextGameBoard {
    assert(!nextGameBoard.isMoveValid(Move(Position(0, 0), O)))
  }

  it should "identify invalid moves that are out of bounds" in {
    assert(!cleanBoard.isMoveValid(Move(Position(-1, 10), O)))
  }

  it should "be over when the grid has a winning state" in {
    val winningCells = for {
      row <- 0 until dimension
      col <- 0 until dimension
    } yield Cell(Position(row, col), if (row == 0) Some(X) else None)
    val winningGrid = ClassicGameGrid(3, winningCells)
    val winningBoard = ClassicGameBoard(winningGrid)
    assert(winningBoard.isGameOver)
    assert(winningBoard.winningMarker.contains(X))
  }

  it should "be over when the grid has a diagonal winning state" in {
    val winningCells = for {
      row <- 0 until dimension
      col <- 0 until dimension
    } yield Cell(Position(row, col), if (row == col) Some(X) else None)
    val winningGrid = ClassicGameGrid(3, winningCells)
    val winningBoard = ClassicGameBoard(winningGrid)
    assert(winningBoard.isGameOver)
    assert(winningBoard.winningMarker.contains(X))
  }
}
