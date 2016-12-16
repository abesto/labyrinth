package net.abesto.labyrinth.components

import com.badlogic.ashley.core.Component
import net.abesto.labyrinth.actions.Action

import scala.collection.mutable

class ActionQueueComponent() extends Component {
  val actions: mutable.Queue[Action] = mutable.Queue()
}
