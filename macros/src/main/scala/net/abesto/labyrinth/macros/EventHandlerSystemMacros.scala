package net.abesto.labyrinth.macros

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

class SubscribeDeferred extends StaticAnnotation

@compileTimeOnly("SubscribeDeferredContainer should've been removed by EventHandlerSystemMacros")
class SubscribeDeferredContainer extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro EventHandlerSystemMacros.impl
}

// Based heavily on http://stackoverflow.com/a/33294530
object EventHandlerSystemMacros {
  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val result = annottees map (_.tree) match {
      // Match a class, and expand.
      case (classDef @ q"$mods class $tpname[..$tparams] $ctorMods(...$paramss) extends { ..$earlydefns } with ..$parents { $self => ..$stats }") :: _ =>
        val extraEntries: Seq[c.universe.Tree] = for {
          q"@SubscribeDeferred def $tname[..$tparams]($_: $eventType): $tpt = $expr" <- stats
          lname = TermName(s"__listen${tname.toString}")
        } yield {
          q"""
           subscribe[$eventType]($tname)
           @Subscribe def $lname[..$tparams](e: $eventType): Unit = enqueue(e)
           """
        }

        if(extraEntries.nonEmpty) {
          q"""
            $mods class $tpname[..$tparams] $ctorMods(...$paramss) extends { ..$earlydefns } with ..$parents { $self =>
              import net.mostlyoriginal.api.event.common.Subscribe
              ..$stats
              ..$extraEntries
            }
          """
        } else classDef
      // Not a class.
      case _ => c.abort(c.enclosingPosition, "Invalid annotation target: not a class")
    }

    c.Expr[Any](result)
  }
}
