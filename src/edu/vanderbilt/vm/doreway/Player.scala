package edu.vanderbilt.vm.doreway

trait Player {

  type Receive = PartialFunction[Any, Unit]
  
  def receive:Receive
  
  def handle:Receive
  
  val self: PlayerRef = PlayerRef(this)
  
  var sender: PlayerRef = null
  
  def message(data: Any): Message = Message(self, data)
}

