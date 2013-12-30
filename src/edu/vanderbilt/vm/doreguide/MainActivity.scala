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
import com.google.android.gms.maps.MapFragment

class MainActivity extends Activity
    with ActivityUtil
    //with MessageUtil
    with LogUtil {

  lazy val mAction = getActionBar
  lazy val mView = new TextView(this)

  def logId = "DoreGuide::MainActivity"

  override def onCreate(saved: Bundle) {
    super.onCreate(saved)

    Dore.initialize(this)
    setupActionBar
    
    getFragmentManager().beginTransaction()
        .add(android.R.id.content, new MapFragment(), "placeList")
        .commit()
    
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater().inflate(R.menu.main, menu)
    true
  }
  
  private def setupActionBar {
    mAction setDisplayShowTitleEnabled  true
    //mAction setBackgroundDrawable       Dore.DECENT_GOLD
    mAction setSplitBackgroundDrawable  Dore.DECENT_GOLD
    mAction setTitle                    "Vanderbilt University"
  }

  val placesController = new Actor() {
    override def act() {
      loop {
        react {
          case PlaceServer.Count(count) =>
            debug("Count received")
            //request(Dore.placeServer) { PlaceServer.GetPlaceWithId(count % 10) }

          case PlaceList(list) => onUi {
            mView.setText("Place: " + list(0))
          }

          case _ => debug("Message not understood")
        }
      }
    }
  }
  
  val agendaController = new Actor() {
    override def act() {
      loop {
        react {
          case _ => debug("Message not understood")
        }
      }
    }
  }
  
}








