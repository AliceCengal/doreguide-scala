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
import edu.vanderbilt.vm.doreguide.container.Place
import edu.vanderbilt.vm.doreguide.services.PlaceServer

class MainActivity extends Activity
    with ActivityUtil
    with LogUtil {

  import MainController._
  import MainActivity._
  
  lazy val mAction = getActionBar
  var mSelectedTab = -1
  var mMain: Actor = null;
  
  override def logId = "DoreGuide::MainActivity"

  override def onCreate(saved: Bundle): Unit =  {
    super.onCreate(saved)
    setContentView(R.layout.main_activity)
    setupActionBar
    
    getFragmentManager().
        beginTransaction().
        add(R.id.main_base_map, new MapFragment(), "base_map").
        commit();
  }

  override def onStart(): Unit = {
    super.onStart()
    Dore.initialize(this)
    
    mMain = new MainController(this)
    mMain.start()
    mMain ! Initialize(this)
  }
  
  override def onStop(): Unit = {
    super.onStop()
    mMain ! Goodbye(this)
    Dore.goodbye(this)
  }
  
  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater().inflate(R.menu.main, menu)
    true
  }
  
  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    debug("Handling menu presses")
    val id = item.getItemId() 
    id match {
      case R.id.menu_main_places =>
        debug("Place tab selected.")
        handleTabChange(PLACE_TAB)
        true
      case R.id.menu_main_agenda =>
        debug("Agenda tab selected.")
        handleTabChange(AGENDA_TAB)
        true
      case R.id.menu_main_tours =>
        debug("Tour tab selected.")
        handleTabChange(TOUR_TAB)
        true
      case R.id.menu_main_navigate =>
        debug("Nav tab selected.")
        handleTabChange(NAV_TAB)
        true
      case _ => false
    }
  }
  
  private def setupActionBar {
    mAction setSplitBackgroundDrawable  Dore.DECENT_GOLD
  }
  
  private def handleTabChange(inputId: Int): Unit = {
    if (mSelectedTab == inputId) {
      debug("Hide the reselected tab.")
      mMain ! TabReselected(mSelectedTab);
      mSelectedTab = -1 }
    else {
      debug("Showing tab number " + inputId)
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

class MainController(val activity: MainActivity) extends Actor
    with LogUtil {
  
  import PlaceServer._
  import MainController._
  import MainActivity._
  
  private var mControllers: List[Actor] = List.empty
  
  override def logId = "DoreGuide::MainController"

  override def act(): Unit = {
    loop {
      react {
        case Initialize(ctx) =>
          mControllers = List(
              new PlaceController(), 
              new AgendaController(), 
              new ToursController(),
              new NavigationController());
          
          for (a <- mControllers) {
            a.start()
            a ! Initialize(ctx)
          }
          
        case ShowFragment(frag) =>
          activity.getFragmentManager().
            beginTransaction().
            replace(R.id.main_base_overlay, frag, "overlay").
            commit();
          debug("Showing fragment from " + sender.toString())

        case TabSelected(tab: Int) =>
          mControllers(tab) ! ShowTab;
          debug("TabSelected: " + tab);

        case TabReselected(tab: Int) =>
          hideTab(tab)
          removeFragment()

        case TabUnselected(tab: Int) => 
          hideTab(tab)
          removeFragment()
        case _                       => debug("Message not understood")
      }
    }
  }
  
  override def exceptionHandler = {
    case e => error(e.getMessage())
  }
  
  private def hideTab(tab: Int): Unit = {
    debug("HideTab message received for tab " + tab)
    mControllers(tab) ! HideTab
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
  case class ShowFragment(frag: Fragment) 
}





