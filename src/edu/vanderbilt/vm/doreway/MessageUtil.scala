package edu.vanderbilt.vm.doreway

import scala.actors.Actor

trait MessageUtil extends Actor { self: Actor =>

  def request(provider: Actor)(message: Any) {
    provider ! Request(self, message)
  }

}

case class Request(requester: Actor, message: Any)