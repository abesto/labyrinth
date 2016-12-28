package net.abesto.labyrinth.macros

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

@compileTimeOnly("SubscribeDeferred  should've been removed by EventHandlerSystemMacros. Did you forget to add DeferredEventHandlerSystem to the containing class?")
class SubscribeDeferred extends StaticAnnotation

@compileTimeOnly("SubscribeDeferredContainer should've been removed by EventHandlerSystemMacros")
class DeferredEventHandlerSystem extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro EventHandlerSystemMacros.impl
}

// Based heavily on http://stackoverflow.com/a/33294530
object EventHandlerSystemMacros {
  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val result = annottees map (_.tree) match {
      // Match a class, and expand.
      case (q"$mods class $tpname[..$tparams] $ctorMods(...$paramss) extends { ..$earlydefns } with ..$parents { $self => ..$stats }") :: _ =>
        val newStats = stats.map {
          case q"@SubscribeDeferred def $tname[..$tparams]($arg: $eventType): $tpt = $expr" =>
            val lname = TermName(s"__listen${tname.toString}")
            q"""
             def $tname[..$tparams]($arg: $eventType): $tpt = $expr
             subscribe[$eventType]($tname)
             @Subscribe def $lname[..$tparams](e: $eventType): Unit = enqueue(e)
             """
          case other => other
        }

        q"""
          $mods class $tpname[..$tparams] $ctorMods(...$paramss) extends { ..$earlydefns } with ..$parents with net.abesto.labyrinth.macros.DeferredEventHandlerSystemImpl { $self =>
            import net.mostlyoriginal.api.event.common.Subscribe
            ..$newStats
          }
        """
      // Not a class.
      case _ => c.abort(c.enclosingPosition, "Invalid annotation target: not a class")
    }

    c.Expr[Any](result)
  }
}
