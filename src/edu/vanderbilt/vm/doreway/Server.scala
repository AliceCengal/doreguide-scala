package edu.vanderbilt.vm.doreway

import scala.actors.Actor

class Server extends Actor 
		with LogUtil {

  val mList = List(
      Place(0, 0, List.empty, "Mock Place 1", "", "", List.empty, 1),
      Place(0, 0, List.empty, "Mock Place 2", "", "", List.empty, 2),
      Place(0, 0, List.empty, "Mock Place 3", "", "", List.empty, 3),
      Place(0, 0, List.empty, "Mock Place 4", "", "", List.empty, 4));
  
  private var count: Int = 0
  
  def logId = "DoreWay::Server"
  
  def act() {
    
    initializeData
    
    loop {
      react {
        case Incre	=> 
          count = count + 1
          debug("Incre received")
          
        case Request(requester, message) =>
            message match {
              case Get	=> 
                requester ! Count(count)
                debug("Get received in a Request")
              case GetPlaceWithId(id) =>
                if (id >= 0 && id < 9999) requester ! PlaceList(List(mList(id)))
            }
        case a:Any		=> { debug("Message not understood: " + a) }
      }
    }
  }
  
  def initializeData {
    
  }
  
}

case class Incre
case class Get
case class Count(c: Int)
case class GetPlaceWithId(id: Int)