package edu.vanderbilt.vm.doreguide.views

import edu.vanderbilt.vm.doreguide.container.Place
import edu.vanderbilt.vm.doreguide.utils.LogUtil

/**
 * It's... magic. You would need to be very comfortable with Functional Programming to
 * understand this thing. There's a friendlier Java procedural version in the original
 * Guide app, also written by me. Refer to the README for link.
 */
trait DataIndexer {

  def isHeader(position: Int): Boolean
  
  def getDataRow(position: Int): Int
  
  def getHeaderTitle(position: Int): String
  
  def categoriesCount: Int
  
}

object DataIndexer {
  
  def alphabetical(data: List[Place]): DataIndexer = new AlphabeticalIndexer(data)
  
  def pythagorean(data: List[Place]): DataIndexer = null
  
  def categorical(data: List[Place]): DataIndexer = null
  
  private case class HeaderRecord(title: String, id: Int, childs: List[Place])

  private abstract class AbsDataIndexer(val garble: List[Place]) extends DataIndexer
      with LogUtil {

    lazy val enigma: List[Int] = buildMap(headerRecords, garble)
    lazy val headerRecords: List[HeaderRecord] = indexingData(initializeHeaderRecord(), garble)
    
    override def logId = "AbsDataIndexer"
    
    def initializeHeaderRecord(): List[HeaderRecord]
    
    def indexingData(hrs: List[HeaderRecord], dat: List[Place]): List[HeaderRecord]
    
    def buildMap(hrs: List[HeaderRecord], plcData: List[Place]): List[Int]
    
    override def isHeader(position: Int) = enigma(position) < 0
    
    override def getDataRow(position: Int) = {
      val row = enigma(position)
      if (row > -1) row else 0
    }

    override def getHeaderTitle(position: Int): String = 
      if (isHeader(position)) headerRecords(-enigma(position) - 1).title
      else ""
    
    def categoriesCount: Int = headerRecords.length
  }
  
  private class AlphabeticalIndexer(garble: List[Place]) extends AbsDataIndexer(garble: List[Place]) {

    override def initializeHeaderRecord(): List[HeaderRecord] = {
      val chars = 'A' to 'Z'
      val catTitles = chars.map(_.toString()) :+ "0-9"
      val ids = 1 to catTitles.length
      
      val tups = (catTitles.toList).zip(ids.toList)
      
      for (tup <- tups) 
        yield HeaderRecord(tup._1, tup._2, List.empty)
    }
    
    override def indexingData(hrs: List[HeaderRecord], data1: List[Place]): List[HeaderRecord] = {

      def indexData(hs: List[HeaderRecord], ds: List[Place]): List[HeaderRecord] = {
        val thisRecord = hs.head

        if (hs.length == 1) {
          List(HeaderRecord(
            thisRecord.title,
            thisRecord.id,
            ds))
        }
        
        else {
          val matchesAndNonMatches = ds.partition(
            plc => plc.name.head.toUpper == thisRecord.title.head);

          HeaderRecord(
            thisRecord.title,
            thisRecord.id,
            matchesAndNonMatches._1) :: indexData(hs.tail, matchesAndNonMatches._2)
        }
      }
      
      indexData(hrs, data1).filter(hr => hr.childs.length > 0)
    }
    
    override def buildMap(hrs: List[HeaderRecord], data2: List[Place]): List[Int] = {
      
      def childTranslation(plc: Place): Int = data2.indexOf(plc)
      
      def headerTranslation(hr: HeaderRecord): Int = -(hrs.indexOf(hr) + 1)

      hrs
        .map(hr =>
          headerTranslation(hr) ::
            hr.childs.map(childTranslation(_)))
        .foldLeft(List.empty[Int])(_ ++ _);
    }
  }
  
  
  
  
  
  
  
  
  
}