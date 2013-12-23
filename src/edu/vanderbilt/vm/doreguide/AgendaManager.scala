package edu.vanderbilt.vm.doreguide

import scala.actors.Actor

class AgendaManager extends Actor
    with LogUtil {

  def logId = "DoreGuide::AgendaManager"

  def act() {
    initializeData
    loop {
      react {

        case a: Any => debug("Message not understood: " + a)
      }
    }
  }

  def initializeData {

  }
}