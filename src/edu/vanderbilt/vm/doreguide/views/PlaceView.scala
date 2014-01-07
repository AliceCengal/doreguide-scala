package edu.vanderbilt.vm.doreguide.views

import android.content.Context
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import edu.vanderbilt.vm.doreguide.container.Place

/**
 * Defines how a Place object should present itself in a ListView.
 */
class PlaceView(ctx: Context) extends ViewHolder {
  
  private val mView = new TextView(ctx)
  
  override def getView: View = mView
  
  override def updateView(
      item: Any, 
      position: Int, 
      convView: View, 
      parent: ViewGroup): Unit = {
    mView.setText(item.asInstanceOf[Place].name)
  }
}

object PlaceView {
  
  private class PlaceViewFactory(val ctx: Context) extends ViewHolderFactory {
    def createHolder() = new PlaceView(ctx)
  }
  
  def getFactory(ctx: Context): ViewHolderFactory = {
    new PlaceViewFactory(ctx)
  }
}