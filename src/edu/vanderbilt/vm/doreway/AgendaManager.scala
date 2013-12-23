package edu.vanderbilt.vm.doreway

import scala.actors.Actor

class AgendaManager extends Actor
    with LogUtil {

  def logId = "DoreWay::AgendaManager"

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