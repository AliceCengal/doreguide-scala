package edu.vanderbilt.vm.doreguide

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.content.Context
import scala.actors.Actor
import edu.vanderbilt.vm.doreguide.container.Place
import edu.vanderbilt.vm.doreguide.utils.LogUtil

object Dore extends LogUtil {
  
  val placeServer: Actor = new PlaceServer().start()
  
  //val nodeServer: Actor = new NodeServer().start()
  
  val agendaManager: Actor = new AgendaManager().start()
  
  val tourServer: Actor = new TourServer().start()
   
  //val geomancer: Actor = new Geomancer().start()
  
  /** This is the gold usually found on sports apparel */
  val DECENT_GOLD = new ColorDrawable(Color.rgb(182, 144, 0))

  /** Not really gold, just a soft color for shading */
  val LIGHT_GOLD = new ColorDrawable(Color.rgb(255, 255, 220))

  /** This is the gold found on the official "V" logo */
  val DARK_GOLD = new ColorDrawable(Color.rgb(162, 132, 72))

  /** The original gold used. It was meant to be just a placeholder */
  val OLD_GOLD = new ColorDrawable(Color.rgb(189, 187, 14))

  /** The color of pure gold, according to Wikipedia */
  val PURE_GOLD = new ColorDrawable(Color.rgb(240, 206, 46))

  /** white */
  val WHITE = new ColorDrawable(Color.WHITE)

  val BLACK = new ColorDrawable(Color.BLACK)
  
  override def logId = "DoreGuide::DoreGuide"
  
  def initialize(ctx: Context): Unit = {
    placeServer ! Initialize(ctx)
    //nodeServer ! Initialize(ctx)
    agendaManager ! Initialize(ctx)
    tourServer ! Initialize(ctx)
    //geomancer ! Initialize(ctx)
  }
  
  def goodbye(ctx: Context): Unit = {
    placeServer ! Goodbye(ctx)
    //nodeServer ! Goodbye(ctx)
    agendaManager ! Goodbye(ctx)
    tourServer ! Goodbye(ctx)
    //geomancer ! Goodbye(ctx)
  }
}

/**
 * Message for the proxy actors to initialze their state. This include downloading json
 * from servers and loading config files from storage. This is should be emmited by the
 * master controller to its servants at the start of the application.
 */
case class Initialize(ctx: Context)

/**
 * Message for passing Place data between Actors.
 */
case class PlaceList(list: List[Place])

/**
 * Message to signify that the application may be closing. Receipients should save any
 * persistent data and configs to storage or to server. 
 */
case class Goodbye(ctx: Context)
