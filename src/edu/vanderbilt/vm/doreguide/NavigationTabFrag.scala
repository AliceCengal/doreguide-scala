package edu.vanderbilt.vm.doreguide

import scala.actors.Actor

class NavigationTabFrag {

}

class NavigationController extends Actor {
  override def act(): Unit= {
    loop { react {
      case _ => {}
    } }
  }
}
