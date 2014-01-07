package edu.vanderbilt.vm.doreguide

import scala.actors.Actor

class AgendaTabFrag {

}

class AgendaController extends Actor {
  override def act(): Unit = {
    loop { react {
      case _ => {}
    } }
  }
}
