package edu.vanderbilt.vm.doreguide.utils

import scala.actors.Actor

trait Listenable extends Actor {
  
  import Listenable._

  private var mListeners: Set[Actor] = Set.empty

  protected def listenerHandler: PartialFunction[Any, Unit] = {
    case AddListener(who)    => mListeners = mListeners + who;
    case RemoveListener(who) => mListeners = mListeners - who
  }

  protected def notifyListeners(event: Any) = {
    mListeners.foreach(_ ! event)
  }

}

object Listenable {
  case class AddListener(who: Actor)
  case class RemoveListener(who: Actor)
}

