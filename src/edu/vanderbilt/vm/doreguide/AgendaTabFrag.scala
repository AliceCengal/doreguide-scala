package edu.vanderbilt.vm.doreguide

import scala.actors.Actor
import edu.vanderbilt.vm.doreguide.utils.LogUtil
import edu.vanderbilt.vm.doreguide.utils.ViewUtil
import android.app.Fragment
import edu.vanderbilt.vm.doreguide.container.Place
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.os.Bundle
import android.widget.ListView
import edu.vanderbilt.vm.doreguide.views.DataAdapter
import edu.vanderbilt.vm.doreguide.views.PlaceView

class AgendaTabFrag(val cont: Actor) extends Fragment with ViewUtil with LogUtil {

  private var mAgenda: List[Place] = List.empty
  
  override def logId = "AgendaTabFrag"
  
  override def onCreateView(inflater: LayoutInflater,
                            container: ViewGroup,
                            saved: Bundle): View = {
    inflater.inflate(R.layout.agenda_tab, container, false)
  }
  
  override def onActivityCreated(saved: Bundle) {
    super.onActivityCreated(saved)
    debug("onActivityCreated")
    inGroup(getView()) {
      case (v: ListView, R.id.listview1) =>
        v.setAdapter(new DataAdapter(mAgenda, PlaceView.getFactory(getActivity())))
      case _ => {}
    }
  }
}

class AgendaController extends Actor with LogUtil {
  
  import Geomancer._
  import MainController._
  import PlaceServer._
  
  private val frag = new AgendaTabFrag(this)
  private var mLocation = ""
  
  override def logId = "AgendaController"
  
  override def act(): Unit = {
    loop { react {
      case Initialize(ctx) => 
      case _ => debug("Message not understood.")
    } }
  }
  
  override def exceptionHandler = {
    case e => error(e.getMessage())
  }
  
}




