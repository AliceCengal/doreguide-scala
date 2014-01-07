package edu.vanderbilt.vm.doreguide.views

import android.view.View
import android.view.ViewGroup

trait ViewHolder {
  def getView: View
  def updateView(item: Any, position: Int, convView: View, parent: ViewGroup): Unit
}

trait ViewHolderFactory {
  def createHolder(): ViewHolder
}