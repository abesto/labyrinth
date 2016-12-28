package net.abesto.labyrinth.macros

import com.artemis.BaseSystem
import net.mostlyoriginal.api.event.common.Event

import scala.collection.immutable.Queue
import scala.collection.mutable


trait DeferredEventHandlerSystemImpl extends BaseSystem {
  var handlers: mutable.Map[Manifest[_], Handler[_]] = mutable.Map()

  class Handler[T <: Event](f: T => Unit) {
    var inbox: Queue[T] = Queue()

    def enqueue(e: T): Unit = {
      inbox :+= e
    }

    def processEvents(): Unit = {
      while (inbox.nonEmpty) {
        val (e, _inbox) = inbox.dequeue
        inbox = _inbox
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
    handlers(mf).asInstanceOf[Handler[T]].enqueue(e)
  }

  override def processSystem(): Unit = handlers.values.foreach(_.processEvents())
}
