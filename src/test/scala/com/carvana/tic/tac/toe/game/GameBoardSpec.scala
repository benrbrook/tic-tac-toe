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

  it should "be able to make any move on the GameGrid, for either X or O" in {
    val nextGameBoard = cleanBoard.makeMove(Move(Position(0, 0), X))
    nextGameBoard shouldBe a[GameBoard]
  }

  it should "be able to make a move, resulting in a new and different state" in {
    val nextGameBoard = cleanBoard.makeMove(Move(Position(0, 0), X))
    assert(nextGameBoard != cleanBoard)
  }

  "A GameBoard" should "identify moves that are not valid" in {
    val nextGameBoard = cleanBoard.makeMove(Move(Position(0, 0), X))
    assert(!nextGameBoard.isMoveValid(Move(Position(0, 0), O)))
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
}
