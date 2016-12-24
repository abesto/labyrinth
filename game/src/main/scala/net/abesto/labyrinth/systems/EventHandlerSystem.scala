package net.abesto.labyrinth.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.signals.{Listener, Signal}

import scala.collection.mutable

abstract class EventHandlerSystem[T] extends EntitySystem {
  protected val inbox: mutable.Queue[(Signal[T], T)] = mutable.Queue()

  protected def subscribe(signal: Signal[T]): Unit = {
    signal.add(new Listener[T] {
      override def receive(signal: Signal[T], data: T): Unit = inbox += ((signal, data))
    })
  }

  protected def handle(deltaTime: Float, signal: Signal[T], data: T)

  override def update(deltaTime: Float): Unit = {
    while (inbox.nonEmpty) {
      val (signal, data) = inbox.dequeue()
      handle(deltaTime, signal, data)
    }
  }
}
