package edu.vanderbilt.vm.doreway

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import java.io.StringReader
import android.widget.TextView
import android.app.ActionBar
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import scala.actors.Actor
import android.view.Menu

class MainActivity extends Activity
    with ActivityUtil
    with Reactive
    with MessageUtil
    with LogUtil {

  lazy val mAction = getActionBar
  lazy val mView = new TextView(this)

  val mCounter: Actor = {
    val c = new Server()
    c.start()
    c
  }

  def logId = "DoreWay::MainActivity"

  override def onCreate(saved: Bundle) {
    super.onCreate(saved)

    mView.setText("Hello")
    setContentView(mView)

    setupActionBar

    click(mView) {
      mCounter ! Server.Incre
      request(mCounter) { Server.Get }
      debug("TextView clicked")
    }

  }

  onReact {
    case Server.Count(count) => onUi {
      debug("Count received")
      request(mCounter) { Server.GetPlaceWithId(count % 10) }
    }

    case PlaceList(list) => onUi {
      mView.setText("Place: " + list(0))
    }

    case _ => debug("Message not understood")
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater().inflate(R.menu.main, menu)
    true
  }
  
  private def setupActionBar {
    mAction setNavigationMode           ActionBar.NAVIGATION_MODE_TABS
    mAction setDisplayShowTitleEnabled  true
    mAction setBackgroundDrawable       DoreWay.DECENT_GOLD
    mAction setSplitBackgroundDrawable  DoreWay.DECENT_GOLD
    mAction setTitle                    "Vanderbilt University"
  }

}
