package edu.vanderbilt.vm.doreway

import scala.actors.Actor
import android.util.Log

class Server extends Actor {

  val mList = List(
      Place(0, 0, List.empty, "Mock Place 1", "", "", List.empty, 1),
      Place(0, 0, List.empty, "Mock Place 2", "", "", List.empty, 2),
      Place(0, 0, List.empty, "Mock Place 3", "", "", List.empty, 3),
      Place(0, 0, List.empty, "Mock Place 4", "", "", List.empty, 4));
  
  private var count: Int = 0
  
  
  def act() {
    loop {
      receive {
        case Incre	=> 
          count = count + 1
          Log.d("DoreWay::Server", "Incre received")
        case Get(a)	=> a ! Count(count)
        case _		=> {}
      }
    }
  }
  
  
}

case class Incre
case class Get(a: Actor)
case class Count(c: Int)