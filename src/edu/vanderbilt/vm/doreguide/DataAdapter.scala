package edu.vanderbilt.vm.doreguide

import android.widget.BaseAdapter
import android.view.ViewGroup
import android.view.View
import edu.vanderbilt.vm.doreguide.container.DataObject

class DataAdapter(
    dataList: List[DataObject], 
    factory: ViewHolderFactory) extends BaseAdapter {
  
  private val mDataList = dataList
  private val mFactory = factory
  
  override def getCount = mDataList.length
  
  override def getItem(position: Int) = mDataList(position)
  
  override def getItemId(position: Int) = mDataList(position).uniqueId
  
  override def getView(position: Int, convView: View, parent: ViewGroup) = {
    
    val holder =	if (convView == null) {
				      val h = mFactory.createHolder
				      h.getView.setTag(h)
				      h
				    }
    				else convView.getTag.asInstanceOf[ViewHolder]
    
    holder.updateView(mDataList(position), position, convView, parent)
    holder.getView
  }
  
}