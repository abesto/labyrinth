package net.abesto.labyrinth.systems

import com.artemis.BaseSystem
import com.typesafe.scalalogging.StrictLogging

class LabyrinthBaseSystem extends BaseSystem with StrictLogging {
  override def processSystem(): Unit = logger.trace("processSystem")
  override def begin(): Unit = logger.trace("begin")
  override def end(): Unit = logger.trace("end")
}
