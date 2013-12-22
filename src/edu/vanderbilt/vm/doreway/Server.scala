package edu.vanderbilt.vm.guidedore

import akka.actor.Actor

class Server extends Actor {

  val mList = List(
      Place(0, 0, List.empty, "Mock Place 1", "", "", List.empty, 1),
      Place(0, 0, List.empty, "Mock Place 2", "", "", List.empty, 2),
      Place(0, 0, List.empty, "Mock Place 3", "", "", List.empty, 3),
      Place(0, 0, List.empty, "Mock Place 4", "", "", List.empty, 4))
  
  def receive = {
    case "PlaceList" => {}//sender ! PlaceList(mList)
    case _ => {}
  }
  
}