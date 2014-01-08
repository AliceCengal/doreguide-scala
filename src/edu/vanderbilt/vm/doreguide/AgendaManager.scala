package edu.vanderbilt.vm.doreguide

import scala.actors.Actor
import edu.vanderbilt.vm.doreguide.utils.LogUtil
import edu.vanderbilt.vm.doreguide.utils.Listenable

class AgendaManager extends Actor
    with Listenable
    with LogUtil {

  import AgendaManager._

  private var mAgenda: Set[Int] = (40 to 100) toSet

  override def logId = "DoreGuide::AgendaManager"

  override def act {
    loop {
      react {
        case Initialize(ctx) => initializeData()
        case GetUserAgenda   => sender ! Agenda(mAgenda.toList)
        
        case AddPlace(id) => 
          mAgenda = mAgenda + id
          notifyListeners(AgendaChanged)
        
        case RemovePlace(id) =>
          mAgenda = mAgenda - id
          notifyListeners(AgendaChanged)
          
        case Goodbye         => saveData()
        case a: Any          => debug("Message not understood: " + a)
      }
    }
  }
  
  override def exceptionHandler = {
    case e => error(e.getMessage())
  }

  private def initializeData(): Unit = {

  }

  private def saveData(): Unit = {

  }
}

object AgendaManager {
  case object GetUserAgenda
  case class Agenda(ids: List[Int])

  case class AddPlace(id: Int)
  case class RemovePlace(id: Int)

  case object AgendaChanged
}






