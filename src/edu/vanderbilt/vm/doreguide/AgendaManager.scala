package edu.vanderbilt.vm.doreguide

import scala.actors.Actor

class AgendaManager extends Actor
    with LogUtil {

  override def logId = "DoreGuide::AgendaManager"

  override def act {
    loop {
      react {
        case Initialize(ctx) => initializeData
        case a: Any          => debug("Message not understood: " + a)
      }
    }
  }

  private def initializeData {
    
  }
}