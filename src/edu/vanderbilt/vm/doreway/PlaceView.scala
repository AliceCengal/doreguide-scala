package edu.vanderbilt.vm.doreway

import android.content.Context
import android.view.ViewGroup
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class PlaceView(ctx: Context) extends ViewHolder {
  
  private val mView = new TextView(ctx)
  
  override def getView = mView
  
  override def updateView(item: Any, position: Int, convView: View, parent: ViewGroup) {
    mView.setText(item.asInstanceOf[Place].name)
  }
}

object PlaceView {
  
  private class PlaceViewFactory(val ctx: Context) extends ViewHolderFactory {
    def createHolder = new PlaceView(ctx)
  }
  
  def getFactory(ctx: Context): ViewHolderFactory = {
    new PlaceViewFactory(ctx)
  }
}