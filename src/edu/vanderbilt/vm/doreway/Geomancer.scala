package edu.vanderbilt.vm.doreway

import scala.actors.Actor
import android.content.Context
import android.location.Criteria

class Geomancer extends Actor
    with LogUtil
    with Listenable {

  private var mLocation = null

  def logId = "DoreWay::Geomancer"

  def act() {
    loop {
      react {
        listenerHandler orElse {
          case Request(requester, message) => message match {
            case GetLocation => requester ! Location(0, 0)
            case GetStatus   => requester ! Enabled
          }
          case Initialize(ctx) => initialize
        }
      }
    }
  }

  def initialize {

  }

  private def getCriteriaA: Criteria = {
    val crit = new Criteria();
    crit.setAccuracy(Criteria.ACCURACY_FINE);
    crit.setAltitudeRequired(false);
    crit.setBearingRequired(false);
    crit.setSpeedRequired(false);
    crit.setCostAllowed(true);
    return crit;
  }

}

case object GetLocation
case class Location(lat: Double, lon: Double)
case object GetStatus

sealed abstract class LocationServiceStatus
case object Disabled extends LocationServiceStatus
case object Enabled extends LocationServiceStatus