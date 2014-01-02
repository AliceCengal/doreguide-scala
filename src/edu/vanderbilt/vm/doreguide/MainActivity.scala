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
import android.app.Fragment
import android.view.MenuItem
import edu.vanderbilt.vm.doreguide.utils.ActivityUtil
import edu.vanderbilt.vm.doreguide.utils.LogUtil

class MainActivity extends Activity
    with ActivityUtil
    with LogUtil {

  import MainController._
  import MainActivity._
  
  lazy val mAction = getActionBar
  var mSelectedTab = -1
  
  val mMain = new MainController(this).start()
  
  def logId = "DoreGuide::MainActivity"

  override def onCreate(saved: Bundle) {
    super.onCreate(saved)

    Dore.initialize(this)
    setupActionBar
    
    setContentView(R.layout.main_activity)
    
    //getFragmentManager().beginTransaction()
    //    .add(android.R.id.content, new MapFragment(), "placeList")
    //    .commit();
    
    //mMain ! DebugSystem
    
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater().inflate(R.menu.main, menu)
    true
  }
  
  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    val id = item.getItemId() 
    id match {
      case R.id.menu_main_places =>
        if (mSelectedTab == PLACE_TAB) 
          mMain ! TabReselected(mSelectedTab)
        else {
          mMain ! TabUnselected(mSelectedTab)
          mSelectedTab = PLACE_TAB
          mMain ! TabSelected(mSelectedTab)
          
        }
      case R.id.menu_main_agenda =>
        if (mSelectedTab == 0) mMain ! TabReselected(mSelectedTab)
        else {
          mMain ! TabUnselected(mSelectedTab)
          mSelectedTab = AGENDA_TAB
          mMain ! TabSelected(id)
          
        }
      case _ => {}
    }
    true
  }
  
  private def setupActionBar {
    mAction setDisplayShowTitleEnabled  true
    //mAction setBackgroundDrawable       Dore.DECENT_GOLD
    mAction setSplitBackgroundDrawable  Dore.DECENT_GOLD
    mAction setTitle                    "Vanderbilt University"
  }

}

object MainActivity {
  val PLACE_TAB = 0
  val AGENDA_TAB = 1
  val TOUR_TAB = 2
  val NAV_TAB = 3
}

class MainController(val activity: MainActivity) 
    extends Actor
    with LogUtil {
  
  import PlaceServer._
  import MainController._
  
  private val mControllers = Array(
      new PlaceController(this))
  
  def logId = "DoreGuide::MainController"
  
  def act(): Unit = {
    loop { react { 
      case ShowFragment(frag) => activity.onUi {
        activity.getFragmentManager().beginTransaction().
            add(android.R.id.content, frag, "fragment").
            commit();
        }
        debug("Showing fragment from " + sender.toString())
        
      case TabSelected(tab: Int) => tab match {
        case 0 => {}
        case 1 => {}
        case 2 => {}
        case 3 => {}
        }
        
        
      case DebugSystem => Dore.placeServer ! Get
      case Count(c) => debug("Received Count")
      case _ => { debug("Message not understood") }
    } }
  }
}

object MainController {
  case object ShowTab
  case object HideTab
  case class TabSelected(tab: Int)
  case class TabUnselected(tab: Int)
  case class TabReselected(tab: Int)
  case object DebugSystem
  case class ShowFragment(frag: Fragment)
  
}

class PlaceController(val main: Actor) extends Actor 
    with LogUtil {
  
  import MainController._
  
  private val frag = new PlaceListFrag(this)
  
  override def logId = "DoreGuide::PlaceController"
  
  override def act(): Unit = {
    loop { react {
      case ShowTab => main ! ShowFragment(frag)
      case _ => { debug("Message not understood") }
    } }
  }
}

object PlaceController {
  case object GetMarkedPlaces
}


class AgendaController extends Actor {
  override def act(): Unit = {
    loop { react {
      case _ => {}
    } }
  }
}

class ToursController extends Actor {
  override def act(): Unit = {
    loop { react {
      case _ => {}
    } }
  }
}

class NavigationController extends Actor {
  override def act(): Unit= {
    loop { react {
      case _ => {}
    } }
  }
}


