package edu.vanderbilt.vm.doreguide

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
import edu.vanderbilt.vm.doreguide.utils.MessageUtil

class MainActivity extends Activity
    with ActivityUtil
    with Reactive
    with MessageUtil
    with LogUtil {

  lazy val mAction = getActionBar
  lazy val mView = new TextView(this)

  val mCounter: Actor = {
    val c = new PlaceServer()
    c.start()
    c ! Initialize(this)
    c
  }

  def logId = "DoreGuide::MainActivity"

  override def onCreate(saved: Bundle) {
    super.onCreate(saved)

    mView.setText("Hello")
    setContentView(mView)

    setupActionBar

    click(mView) {
      mCounter ! PlaceServer.Incre
      request(mCounter) { PlaceServer.Get }
      debug("TextView clicked")
    }

  }

  onReact {
    case PlaceServer.Count(count) => onUi {
      debug("Count received")
      request(mCounter) { PlaceServer.GetPlaceWithId(count % 10) }
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
    mAction setBackgroundDrawable       DoreGuide.DECENT_GOLD
    mAction setSplitBackgroundDrawable  DoreGuide.DECENT_GOLD
    mAction setTitle                    "Vanderbilt University"
  }

}