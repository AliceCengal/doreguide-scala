package edu.vanderbilt.vm.doreway

import scala.actors.Actor

class TourServer extends Actor with LogUtil {

  def logId = "DoreWay::TourServer"

  def act() {
    loop {
      react {
        case Initialize(ctx) => initialize
      }
    }
  }

  def initialize {

  }
}

object TourServer {
  val rawDataUrl = "https://raw.github.com/VandyMobile/guide-android/master/GuideAndroid/assets/tours.json"
}