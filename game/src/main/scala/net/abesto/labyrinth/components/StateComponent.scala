package net.abesto.labyrinth.components

import com.artemis.Component
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.fsm.States

class StateComponent extends Component {
  var current: States.State = Constants.initialState

  def isActive(s: States.State): Boolean = s.getClass.isAssignableFrom(current.getClass)
  def isActive[T <: States.State](implicit mf: Manifest[T]): Boolean = mf.runtimeClass.isAssignableFrom(current.getClass)
}
