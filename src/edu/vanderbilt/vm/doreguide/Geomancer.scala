package edu.vanderbilt.vm.doreguide

import scala.actors.Actor
import android.content.Context
import android.location.Criteria
import edu.vanderbilt.vm.doreguide.utils.Request
import android.location.LocationManager
import android.location.LocationListener
import android.os.Bundle
import android.location.Location

class Geomancer extends Actor
    with LogUtil
    with Listenable {

  private var mLocation: Location = null
  val FEET_PER_METER = 3.28083989501312;
  val FEET_PER_MILE = 5280;
  var locationManager: LocationManager = null
  val locationListener: LocationListener = new LocationListener() {
    override def onLocationChanged(loc: Location) {
      mLocation = loc
      debug("receiving location: " + mLocation.getLatitude() + ", " + mLocation.getLongitude())
      notifyListeners(CurrentLoc(mLocation.getLatitude(), mLocation.getLongitude()))
    }
    
    override def onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    
    override def onProviderEnabled(provider: String) {}
    
    override def onProviderDisabled(provider: String) {}
  }
  
  def logId = "DoreGuide::Geomancer"

  def act() {
    loop {
      react {
        listenerHandler orElse {
          case Request(requester, message) => message match {
            case GetLocation => requester ! CurrentLoc(mLocation.getLatitude(), mLocation.getLongitude())
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
case class CurrentLoc(lat: Double, lon: Double)
case object GetStatus

sealed abstract class LocationServiceStatus
case object Disabled extends LocationServiceStatus
case object Enabled extends LocationServiceStatus

