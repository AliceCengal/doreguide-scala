package edu.vanderbilt.vm.doreway

sealed trait PlayerRef {
  def !(message: Message): Unit
}

object PlayerRef {
  
  def apply(player: Player): PlayerRef = new IPlayerRef(player)
  
  private class IPlayerRef(player: Player) extends PlayerRef {
    
    private val mPlayer = player
    
    def !(message: Message) {
      mPlayer.sender = message.sender
      mPlayer.receive(message.data) 
    }
  }
  
  
  
}

case class Message(sender: PlayerRef, data: Any)