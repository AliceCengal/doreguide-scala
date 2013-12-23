package edu.vanderbilt.vm.doreguide

import android.view.View
import android.view.ViewGroup
import android.content.Context

abstract class ItemViewWrapper {
  
  def getView: View
  def setView(item: Any, position: Int, convView: View, parent: ViewGroup): Unit
}

object ItemViewWrapper {
  type ItemViewFactory = (() => ItemViewWrapper)
}

