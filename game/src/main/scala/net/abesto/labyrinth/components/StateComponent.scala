package net.abesto.labyrinth.components

import com.artemis.Component
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.fsm.States

class StateComponent extends Component {
  var current: States.State = Constants.initialState
}
