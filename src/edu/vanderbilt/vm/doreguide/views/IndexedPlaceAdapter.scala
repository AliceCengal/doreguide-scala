package edu.vanderbilt.vm.doreguide.views

import android.widget.BaseAdapter
import edu.vanderbilt.vm.doreguide.container.Place
import android.view.ViewGroup
import android.view.View
import android.content.Context
import android.view.LayoutInflater
import edu.vanderbilt.vm.doreguide.R
import android.widget.TextView

class IndexedPlaceAdapter(
    val dataList: List[Place],
    val itemFactory: ViewHolderFactory,
    val headerFactory: ViewHolderFactory,
    val indexer: DataIndexer
    ) extends BaseAdapter {
  
  override def getCount = dataList.length + indexer.categoriesCount

  override def getItem(position: Int) = dataList(indexer.getDataRow(position))
  
  override def getItemId(position: Int) = 
    if (indexer.isHeader(position)) -1 
    else dataList(indexer.getDataRow(position)).uniqueId
  
  override def getViewTypeCount() = 2
  
  override def getItemViewType(position: Int): Int = 
    if (indexer.isHeader(position)) 0 else 1
  
  override def getView(position: Int, convView: View, parent: ViewGroup) = {

    if (indexer.isHeader(position)) {
      val holder =
        if (convView == null ||
            convView.getTag().asInstanceOf[(Int, ViewHolder)]._1 != 0) {
          val h = headerFactory.createHolder()
          h.getView.setTag((1, h))
          h
        }
        else convView.getTag().asInstanceOf[(Int, ViewHolder)]._2
        
      holder.updateView(indexer.getHeaderTitle(position), position, convView, parent)
      holder.getView
    }
    
    else {
      val holder =
        if (convView == null ||
            convView.getTag().asInstanceOf[(Int, ViewHolder)]._1 != 1) {
          val h = itemFactory.createHolder()
          h.getView.setTag((2, h))
          h
        }
        else convView.getTag().asInstanceOf[(Int, ViewHolder)]._2
      
      holder.updateView(dataList(indexer.getDataRow(position)), position, convView, parent)
      holder.getView
    }
    
  }
}

class PlaceHeaderView(ctx: Context) extends ViewHolder {
  val mView = LayoutInflater.from(ctx).inflate(R.layout.place_list_header, null)
  
  override def getView: View = mView
  
  override def updateView(
      item: Any, 
      position: Int, 
      convView: View, 
      parent: ViewGroup): Unit = {

    mView.findViewById(R.id.textView1).
      asInstanceOf[TextView].
      setText(
        if (item.isInstanceOf[String]) item.asInstanceOf[String]
        else "Wrong Title")
  }
}

object PlaceHeaderView {
  private class PlaceHeaderViewFactory(val ctx: Context) extends ViewHolderFactory {
    def createHolder() = new PlaceHeaderView(ctx)
  }
  
  def getFactory(ctx: Context): ViewHolderFactory = {
    new PlaceHeaderViewFactory(ctx)
  }
}

class PlaceItemView(ctx: Context) extends ViewHolder {
  val mView = LayoutInflater.from(ctx).inflate(R.layout.place_list_item, null)
  override def getView: View = mView
  
  override def updateView(
      item: Any, 
      position: Int, 
      convView: View, 
      parent: ViewGroup): Unit = {
    mView.findViewById(R.id.placelist_item_title).
      asInstanceOf[TextView].
      setText(
        if (item.isInstanceOf[Place]) item.asInstanceOf[Place].name
        else "Wrong Name")
  }
}

object PlaceItemView {
  private class PlaceItemViewFactory(val ctx: Context) extends ViewHolderFactory {
    def createHolder() = new PlaceItemView(ctx)
  }
  
  def getFactory(ctx: Context): ViewHolderFactory = {
    new PlaceItemViewFactory(ctx)
  }
}


