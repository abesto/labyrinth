package net.abesto.labyrinth.events

import net.abesto.labyrinth.maze.Maze
import net.mostlyoriginal.api.event.common.Event

case class MazeLoadedEvent(path: String, maze: Maze, entityCount: Int) extends Event

