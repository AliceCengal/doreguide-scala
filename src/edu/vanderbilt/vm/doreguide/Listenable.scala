package edu.vanderbilt.vm.doreguide

import scala.actors.Actor

trait Listenable extends Actor {

  private var mListeners: List[Actor] = List.empty

  protected def listenerHandler: PartialFunction[Any, Unit] = {
    case AddListener(who) => mListeners = who :: mListeners
  }

  protected def notifyListeners(event: Any) = {
    mListeners.foreach(_ ! event)
  }

}

case class AddListener(who: Actor)
case class RemoveListener(who: Actor)