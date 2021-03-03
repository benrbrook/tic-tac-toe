Tic-Tac-Test
============

This repo is a stubbed-out Tic-Tac-Toe game, where it is your mission,
should you choose to accept it, to implement the gameplay (and tests!)

Since this is a Scala game, we will toss mutability to the wind, use our
wonderful collections library, and use recursion...

The goal is to implement our `Main` method and all the plumbing, which will recursively play a `Game` -
consisting of `Players`, `GameBoards` and `GameGrids` until the `Game` ends with a winner, or tie if no moves
are remaining.

## Getting Started

To get started - please be sure to fork this repository and do your work in your own repo.  When you are done, send a public link to your repo so we can assess your efforts.

You may use any editor/IDE that you prefer to accomplish the task.

You probably must have SBT (https://www.scala-sbt.org/download.html) installed at the bare minimum as this project is built with SBT.

A JDK is also required. (JDK 13 or 15)

## Overview of packages

At the root (`com.carvana.tic.tac.toe`) there is a `Main` that needs to be implemented to run our game, once all the
other pieces are implemented.

### game
This package contains the items to implement: a `Game`, a `GameBoard`, a `GameGrid` and `Player`s. 
There are traits defined, and some implementing class stubbed out, ready to be defined.

### models
This package contains some simple case class models, already defined, to help you on your way. There are `Cell`s which
have a `Position` on a `GameGrid`, as well as holder of a possible `Marker` (an `X` or an `O` marked for a player).
There is also a `Move` model, to possibly place a `Marker` at a `Cell`s `Position`.

## A general note

As an example of the general pattern, we will look at the trait for `GameGrid` 
and stubbed out example implementation (follow along in the comments):

```scala
import com.carvana.tic.tac.toe.models.{Cell, Marker, Move, Position}
trait GameGrid {
  // 1 These are immutable fields that represent some state
  val dimension: Int
  val cells: Seq[Cell]
  // 2 These are things we can say/conclude about our state
  val unplacedPositions: Int
  def cellHasMarker(position: Position): Boolean
  val winningMarker: Option[Marker]
  // 3 This is an action we can perform that will mutate state, and give us back a new instance with the new state
  def placeMove(move: Move): GameGrid
}

// 1 Pass in our immutable fields as constructor arguments
case class ClassicGameGrid(dimension: Int = 3, cells: Seq[Cell]) extends GameGrid {

  // 2 What can we conclude about our immutable state with these?
  override val unplacedPositions: Int = ???
  override val winningMarker: Option[Marker] = ???
  override def cellHasMarker(position: Position): Boolean = ???

  // 3 Enacting this Move will alter the state of our cells, so we'll need to return a new instance with
  // the updated state.
  override def placeMove(move: Move): GameGrid = ???

}
```

## Tests

There are three test suites to make green: `GameSpec` has some tests defined, and ready to run upon implementation. `GameBoardSpec` has some tests described,
but need to be implemented, and `GameGridSpec` needs tests defined and implemented.

## Extra Credit

* Add and implement [LazyLogging](https://github.com/lightbend/scala-logging) as a dependency, to log what's going on in the background as state is being mutated.
* Make some custom components that aren't so classic (e.g. a dimension=9 GameGrid)