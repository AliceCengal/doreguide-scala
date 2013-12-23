package edu.vanderbilt.vm.doreway

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.content.Context

object DoreWay {

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

}

case class Initialize(ctx: Context)
case class PlaceList(list: List[Place])
case class Goodbye(ctx: Context)