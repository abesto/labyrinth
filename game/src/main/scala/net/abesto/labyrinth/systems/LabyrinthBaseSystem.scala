package net.abesto.labyrinth.systems

import com.artemis.BaseSystem
import com.typesafe.scalalogging.StrictLogging

class LabyrinthBaseSystem extends BaseSystem with StrictLogging {
  private var helpers: Helpers = _

  override def processSystem(): Unit = logger.trace("processSystem")
  override def begin(): Unit = logger.trace("begin")
  override def end(): Unit = logger.trace("end")

  override def checkProcessing(): Boolean = {
    logger.trace("checkProcessing")
    super.checkProcessing() && helpers.isEnabledInCurrentState(this)
  }
}
