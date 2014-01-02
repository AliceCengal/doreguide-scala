package edu.vanderbilt.vm.doreguide

import android.app.Fragment
import android.widget.ListView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import scala.actors.Actor
import edu.vanderbilt.vm.doreguide.utils.MessageUtil
import android.view.Menu
import android.view.MenuInflater
import edu.vanderbilt.vm.doreguide.container.Place
import edu.vanderbilt.vm.doreguide.utils.LogUtil

class PlaceListFrag(val controller: Actor) extends Fragment 
    with LogUtil {

  private lazy val mView: ListView = new ListView(getActivity())
  private var mPlaceList: List[Place] = Nil
  
  override def onCreateView(
      inflater: LayoutInflater, 
      container: ViewGroup, 
      saved: Bundle): View = mView;
  
  override def onActivityCreated(saved: Bundle) {
    super.onActivityCreated(saved)
    //request(Dore.placeServer) { PlaceServer.GetAllPlaces }
    debug("requesting all Places")
  }
  override def logId = "DoreGuide::PlaceListFrag";
  
  def setPlaceList(pl: List[Place]): Unit = {
    mPlaceList = pl;
    mView.setAdapter(
        new DataAdapter(
            mPlaceList, 
            PlaceView.getFactory(getActivity())));
    mView.invalidate()
  }
  
}
