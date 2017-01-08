package net.abesto.labyrinth.macros

import com.artemis.BaseSystem
import com.typesafe.scalalogging.Logger
import net.mostlyoriginal.api.event.common.Event

import scala.collection.immutable.Queue
import scala.collection.mutable


trait DeferredEventHandlerSystemImpl extends BaseSystem {
  var handlers: mutable.Map[Manifest[_], Handler[_]] = mutable.Map()

  class Handler[T <: Event](f: T => Unit) {
    val outer: DeferredEventHandlerSystemImpl = DeferredEventHandlerSystemImpl.this
    val logger = Logger(outer.getClass)
    var inbox: Queue[T] = Queue()

    def enqueue(e: T): Unit = {
      logger.trace(s"Enqueued event: $e")
      inbox :+= e
    }

    def processEvents(): Unit = {
      while (inbox.nonEmpty) {
        val (e, _inbox) = inbox.dequeue
        inbox = _inbox
        logger.trace(s"Processing event: $e")
        f(e)
      }
    }
  }

  def subscribe[T <: Event](f: T => Unit)(implicit mf: Manifest[T]): Handler[T] = {
    val h = new Handler(f)
    handlers.put(mf, h)
    h
  }

  def enqueue[T <: Event](e: T)(implicit mf: Manifest[T]): Unit = {
    if (isEnabled) {
      handlers(mf).asInstanceOf[Handler[T]].enqueue(e)
    }
  }

  def handlersWithNonEmptyInboxes: Iterable[Handler[_]] = handlers.values.filter(_.inbox.nonEmpty)

  override def checkProcessing(): Boolean = handlersWithNonEmptyInboxes.nonEmpty

  abstract override def processSystem(): Unit = {
    super.processSystem()
    while (checkProcessing()) {
      handlersWithNonEmptyInboxes.foreach(_.processEvents())
    }
  }
}
