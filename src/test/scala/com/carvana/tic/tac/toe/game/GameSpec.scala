package com.carvana.tic.tac.toe.game

import com.carvana.tic.tac.toe.models.{Move, O, Position, X}
import com.carvana.tic.tac.toe.{GamePlayLogic, GameSetUp, TicTacIO}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

// Implement there to take a pre-determined collection of moves that results in...

case class PositionCollection(positions: List[Position]) {}

// 1. Player X wining
object PlayerXWins extends TicTacIO {
  var playerXPositions = List(Position(0, 0), Position(1, 1), Position(2, 2))
  var playerOPositions = List(Position(1, 0), Position(2, 0), Position(0, 2))

  override def getMoveForPlayer(player: Player): Move = {
    player.marker match {
      case X => {
        val position = playerXPositions.head
        playerXPositions = playerXPositions.tail
        Move(position, X)
      }
      case O => {
        val position = playerOPositions.head
        playerOPositions = playerOPositions.tail
        Move(position, O)
      }
    }
  }
}
// 2. Player O winning
object PlayerOWins extends TicTacIO {
  var playerXPositions = List(Position(1, 0), Position(2, 0), Position(0, 2))
  var playerOPositions = List(Position(0, 0), Position(1, 1), Position(2, 2))

  override def getMoveForPlayer(player: Player): Move = {
    player.marker match {
      case X => {
        val position = playerXPositions.head
        playerXPositions = playerXPositions.tail
        Move(position, X)
      }
      case O => {
        val position = playerOPositions.head
        playerOPositions = playerOPositions.tail
        Move(position, O)
      }
    }
  }
}
// 3. A tie
object TieGame extends TicTacIO {
  var playerXPositions = List(
    Position(0, 1),
    Position(1, 2),
    Position(2, 0),
    Position(2, 2),
    Position(1, 1)
  )
  var playerOPositions =
    List(Position(0, 0), Position(1, 0), Position(2, 1), Position(0, 2))

  override def getMoveForPlayer(player: Player): Move = {
    player.marker match {
      case X => {
        val position = playerXPositions.head
        playerXPositions = playerXPositions.tail
        Move(position, X)
      }
      case O => {
        val position = playerOPositions.head
        playerOPositions = playerOPositions.tail
        Move(position, O)
      }
    }
  }
}

class GameSpec
    extends AnyFlatSpec
    with should.Matchers
    with GameSetUp
    with GamePlayLogic {

  "A Game" should "make sure the currentPlayer is first in queue" in {
    assert(newGame.currentPlayer == newGame.playerQueue.head)
  }

  it should "not let the current player move twice" in {
    assert(newGame.currentPlayer != newGame.playerQueue.drop(1).head)
  }

  it should "not allow two players to use the same Marker" in {
    val player1 = newGame.currentPlayer
    val player2 = newGame.playerQueue.drop(1).head
    assert(player1.marker != player2.marker)
  }

  it should "play itself out where Player X wins in" in {
    assert(playGame(newGame)(PlayerXWins).map(_.marker).contains(X))
  }

  it should "play itself out where Player O wins in" in {
    assert(playGame(newGame)(PlayerOWins).map(_.marker).contains(O))
  }

  it should "play itself out where there is a tie" in {
    assert(playGame(newGame)(TieGame).isEmpty)
  }

}
