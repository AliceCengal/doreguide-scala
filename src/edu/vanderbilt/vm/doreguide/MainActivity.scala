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
  
  Dore.initialize(this)
  
  def logId = "DoreGuide::MainActivity"

  override def onCreate(saved: Bundle): Unit =  {
    super.onCreate(saved)
    setContentView(R.layout.main_activity)
    setupActionBar
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater().inflate(R.menu.main, menu)
    true
  }
  
  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    val id = item.getItemId() 
    id match {
      case R.id.menu_main_places =>
        handleTabChange(PLACE_TAB)
        true
      case R.id.menu_main_agenda =>
        handleTabChange(AGENDA_TAB)
        true
      case R.id.menu_main_tours =>
        handleTabChange(TOUR_TAB)
        true
      case R.id.menu_main_navigate =>
        handleTabChange(NAV_TAB)
        true
      case _ => { false }
    }
  }
  
  private def setupActionBar {
    mAction setDisplayShowTitleEnabled  true
    //mAction setBackgroundDrawable       Dore.DECENT_GOLD
    mAction setSplitBackgroundDrawable  Dore.DECENT_GOLD
    mAction setTitle                    "Vanderbilt University"
  }
  
  private def handleTabChange(inputId: Int): Unit = {
    if (mSelectedTab == inputId) {
      mMain ! TabReselected(mSelectedTab);
      mSelectedTab = -1 }
    else {
      mMain ! TabUnselected(mSelectedTab);
      mSelectedTab = inputId;
      mMain ! TabSelected(mSelectedTab); }
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
  import MainActivity._
  
  private val mControllers: Array[Actor] = {
    val conts = Array(
      new PlaceController(this).start(), 
      new AgendaController().start(),
      new ToursController().start(),
      new NavigationController().start());
    for (c <- conts) c ! Initialize(activity);
    conts
  }
  
  def logId = "DoreGuide::MainController"
  
  def act(): Unit = {
    loop { react { 
      case ShowFragment(frag) =>
        activity.getFragmentManager().
            beginTransaction().
            replace(R.id.main_base_overlay, frag, "overlay").
            commit();
        debug("Showing fragment from " + sender.toString())
        
      case TabSelected(tab: Int) => tab match {
          case PLACE_TAB  => { mControllers(0) ! ShowTab }
          case AGENDA_TAB => {}
          case TOUR_TAB   => {}
          case NAV_TAB    => {}
        }

        case TabReselected(tab: Int) => 
          hideTab(tab)
          removeFragment()

        case TabUnselected(tab: Int) => hideTab(tab)
        case DebugSystem             => Dore.placeServer ! Get
        case Count(c)                => debug("Received Count")
        case _                       => debug("Message not understood")
    } }
  }
  
  private def hideTab(tab: Int): Unit = {
    tab match {
      case PLACE_TAB  => { mControllers(0) ! HideTab }
      case AGENDA_TAB => {}
      case TOUR_TAB   => {}
      case NAV_TAB    => {}
    }
  }
  
  private def removeFragment(): Unit = {
    val fm = activity.getFragmentManager();
    fm.beginTransaction().
            remove(fm.findFragmentByTag("overlay")).
            commit();
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
        case Initialize(ctx) => Dore.placeServer ! PlaceServer.GetAllPlaces
        case PlaceList(list) => frag.setPlaceList(list)
        case ShowTab         => main ! ShowFragment(frag)
        case _               => debug("Message not understood")
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


