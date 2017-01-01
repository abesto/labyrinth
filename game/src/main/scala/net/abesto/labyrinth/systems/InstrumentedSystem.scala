package net.abesto.labyrinth.systems

import com.artemis.BaseSystem
import com.typesafe.scalalogging.StrictLogging

class InstrumentedSystem extends BaseSystem with StrictLogging {
  override def processSystem(): Unit = logger.trace("processSystem")
  override def begin(): Unit = logger.trace("begin")
  override def end(): Unit = logger.trace("end")
  override def isEnabled: Boolean = {
    logger.trace("isEnabled")
    super.isEnabled
  }
}
