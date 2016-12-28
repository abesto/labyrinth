package net.abesto.labyrinth.idea

import org.jetbrains.plugins.scala.lang.psi.api.toplevel.typedef.ScTypeDefinition
import org.jetbrains.plugins.scala.lang.psi.impl.toplevel.typedef.SyntheticMembersInjector

class EventHandlerSystemInjector extends SyntheticMembersInjector {
  override def injectSupers(source: ScTypeDefinition): Seq[String] = {
    println(source)
    if (source.hasAnnotation("net.abesto.labyrinth.macros.DeferredEventHandlerSystem")) {
      Seq("net.abesto.labyrinth.macros.DeferredEventHandlerSystemImpl")
    } else {
      Seq.empty
    }
  }
}