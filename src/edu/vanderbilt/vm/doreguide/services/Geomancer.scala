package edu.vanderbilt.vm.doreguide.services

import scala.actors.Actor
import android.content.Context
import android.location.Criteria
import android.location.LocationManager
import android.location.LocationListener
import android.os.Bundle
import android.location.Location
import java.text.DecimalFormat
import edu.vanderbilt.vm.doreguide.utils.LogUtil
import edu.vanderbilt.vm.doreguide.utils.Listenable
import edu.vanderbilt.vm.doreguide.Initialize
import edu.vanderbilt.vm.doreguide.Goodbye

class Geomancer extends Actor 
    with LogUtil
    with Listenable {

  import Geomancer._

  private var mLocation: Location = DEFAULT_LOC
  private var locationManager: LocationManager = null
  private var serviceStatus: LocationServiceStatus = Enabled
  private val locationListener: LocationListener = getLocListener()
  private var provider: String = ""

  private var mTimer: Actor = null
  
  override def logId = "DoreGuide::Geomancer"

  override def act(): Unit = {
    loop {
      react {
        listenerHandler orElse {
          case GetLocation     => sender ! CurrentLoc(mLocation.getLatitude(), mLocation.getLongitude())
          case GetStatus       => sender ! serviceStatus
          case UpdateLocation =>
            val newLoc = locationManager.getLastKnownLocation(provider)
            notifyListeners(
              CurrentLoc(
                newLoc.getLatitude(),
                newLoc.getLongitude()))

            if (newLoc.distanceTo(mLocation) > DEFAULT_RADIUS) {
              mLocation = newLoc
              debug("receiving location: " +
                mLocation.getLatitude() + ", " +
                mLocation.getLongitude())
            }
            
          case Initialize(ctx) => initialize(ctx)
          case Goodbye(ctx) =>
            //mTimer ! TimerStop
            //mTimer = null
        }
      }
    }
  }

  override def exceptionHandler = {
    case e => error(e.getMessage())
  }
  
  private def initialize(ctx: Context): Unit = {
    locationManager = ctx.
      getSystemService(Context.LOCATION_SERVICE).
      asInstanceOf[LocationManager]
    provider = locationManager.
      getBestProvider(
        getCriteriaA(),
        true);

    if (provider != null) {
      mTimer = new TimerActor(5000, this, UpdateLocation)
      mTimer.start() }
    else
      serviceStatus = Disabled
  }

  private def getCriteriaA(): Criteria = {
    val crit = new Criteria();
    crit setAccuracy         Criteria.ACCURACY_FINE
    crit setAltitudeRequired false
    crit setBearingRequired  false
    crit setSpeedRequired    false
    crit setCostAllowed      true
    crit
  }

  private def getLocListener(): LocationListener = {
    new LocationListener() {
      override def onLocationChanged(loc: Location): Unit = {
        mLocation = loc
        debug("receiving location: " + mLocation.getLatitude() + ", " + mLocation.getLongitude())
        notifyListeners(CurrentLoc(mLocation.getLatitude(), mLocation.getLongitude()))
      }

      override def onStatusChanged(provider: String, status: Int, extras: Bundle): Unit = {}

      override def onProviderEnabled(provider: String): Unit = {}

      override def onProviderDisabled(provider: String): Unit = {}
    }
  }
  
}

object Geomancer {

  val DEFAULT_LONGITUDE = -86.803889;
  val DEFAULT_LATITUDE = 36.147381;
  val DEFAULT_LOC = {
    val l = new Location("default")
    l.setLatitude(DEFAULT_LATITUDE)
    l.setLongitude(DEFAULT_LONGITUDE)
    l
  }
  
  val FEET_PER_METER = 3.28083989501312;
  val FEET_PER_MILE = 5280;

  val DEFAULT_TIMEOUT = 5000
  val DEFAULT_RADIUS = 5
  
  val EARTH_RADIUS = 6378100 // meters

  def getDistanceString(distanceInMeter: Double): String = {
    val distanceInFeet = distanceInMeter * FEET_PER_METER
    if (distanceInFeet < 1000)
      distanceInFeet.toInt.toString() + " ft"
    else
      new DecimalFormat("#.##").format(distanceInFeet / FEET_PER_MILE) + " mi"
  }

  def calcDistance(
    lat1: Double,
    lng1: Double,
    lat2: Double,
    lng2: Double): Double = {
    import Math._
    
    val lat1R = toRadians(lat1)
    val lat2R = toRadians(lat2)
    val dLatR = abs(lat2R - lat1R)
    val dLngR = abs(toRadians(lng2 - lng1))
    val a = pow(sin(dLatR / 2), 2) +
      cos(lat1R) * cos(lat2R) * sin(dLngR / 2) * sin(dLngR / 2)
    
    2 * atan2(sqrt(a), sqrt(1 - a)) * EARTH_RADIUS
  }
  
  case object GetLocation
  case class CurrentLoc(lat: Double, lon: Double)
  case object GetStatus
  case object UpdateLocation
  case object TimerStop

  sealed abstract class LocationServiceStatus
  case object Disabled extends LocationServiceStatus
  case object Enabled extends LocationServiceStatus

  class TimerActor(val timeout: Long, val who: Actor, val reply: Any) extends Actor {
    
    import scala.actors._
    
    def act {
      loop {
        reactWithin(timeout) {
          case TimerStop => this.exit()
          case TIMEOUT   => who ! reply
        }
      }
    }
  }
}



