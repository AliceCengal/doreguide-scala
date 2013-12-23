package edu.vanderbilt.vm.doreway

import scala.actors.Actor
import android.content.Context

class Geomancer extends Actor
    with LogUtil {

  def logId = "DoreWay::Geomancer"
  
  def act() {
    loop {
      react {
        case Initialize(ctx) => initialize
      }
    }
  }

  def initialize {}
}

object Geomancer {
  sealed abstract class LocationServiceStatus
  case class Disabled extends LocationServiceStatus
  case class Enabled extends LocationServiceStatus
}