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

  def alphabetical(data: List[Place]): DataIndexer =
    new AlphabeticalIndexer(data)

  def pythagorean(data: List[Place], loc: (Double, Double)): DataIndexer =
    new PythagoreanIndexer(data, loc)

  def categorical(data: List[Place]): DataIndexer = 
    new CategoricalIndexer(data)

}

case class HeaderRecord(
  title: String,
  id: Int)

abstract class AbsDataIndexer extends DataIndexer
    with LogUtil {

  lazy val enigma = buildMap(headerRecords)
  lazy val headerRecords = indexingData(initializeHeaderRecord())

  override def logId = "AbsDataIndexer"

  def initializeHeaderRecord(): List[HeaderRecord]

  def indexingData(hrs: List[HeaderRecord]): List[(HeaderRecord, List[Place])]

  def buildMap(hrs: List[(HeaderRecord, List[Place])]): List[Int]

  override def isHeader(position: Int) = enigma(position) < 0

  override def getDataRow(position: Int) = {
    val row = enigma(position)
    if (row > -1) row 
    else 0
  }

  override def getHeaderTitle(position: Int): String =
    if (isHeader(position)) headerRecords(-enigma(position) - 1)._1.title
    else ""

  override def categoriesCount: Int = headerRecords.length
}

abstract class AbsAbsPlaceIndexer(val garble: List[Place])
    extends AbsDataIndexer {

  def placeSorter(hr: HeaderRecord, plc: Place): Boolean
  
  override def indexingData(hrs: List[HeaderRecord]) = {

    // Define inner function for recursion.
    def partitionAndPair(
      hs: List[HeaderRecord],
      ds: List[Place]): List[(HeaderRecord, List[Place])] = {

      if (hs.length == 1) {
        List((hs.head, ds))
      }

      else {
        val matchesAndNonMatches = ds.partition(
          plc => placeSorter(hs.head, plc))

        (hs.head, matchesAndNonMatches._1) ::
          partitionAndPair(hs.tail, matchesAndNonMatches._2)
      }
    }

    partitionAndPair(hrs, garble).
      filter(tup => tup._2.length > 0)
  }

  override def buildMap(hrs: List[(HeaderRecord, List[Place])]): List[Int] = {

    def childTranslation(plc: Place): Int = garble.indexOf(plc)

    def headerTranslation(hr: (HeaderRecord, List[Place])): Int = -(hrs.indexOf(hr) + 1)

    hrs
      .map(hr =>
        headerTranslation(hr) ::
          hr._2.map(childTranslation(_)))
      .foldLeft(List.empty[Int])(_ ++ _);
  }
  
}

private class AlphabeticalIndexer(garble: List[Place])
    extends AbsAbsPlaceIndexer(garble: List[Place]) {

  override def initializeHeaderRecord() = {
    val chars = 'A' to 'Z' toList
    val catTitles = chars.map(_.toString()) :+ "0-9"
    val ids = (1 to catTitles.length) toList

    val tups = catTitles zip ids
    for (tup <- tups)
      yield HeaderRecord(tup._1, tup._2)
  }

  override def placeSorter(hr: HeaderRecord, plc: Place) =
    plc.name.head.toUpper == hr.title.head
  
}

private class PythagoreanIndexer(
    garble: List[Place],
    val latLon: (Double, Double)) extends AbsAbsPlaceIndexer(garble: List[Place]) {

  import edu.vanderbilt.vm.doreguide.services.Geomancer._
  
  override def initializeHeaderRecord() = {
    List(
      HeaderRecord("100 ft", 30),
      HeaderRecord("200 ft", 61),
      HeaderRecord("400 ft", 122),
      HeaderRecord("800 ft", 244),
      HeaderRecord("1000 ft", 304),
      HeaderRecord("0.3 mi", 483),
      HeaderRecord("0.6 mi", 965),
      HeaderRecord("1.2 mi", 1931),
      HeaderRecord("2.4 mi", 3862),
      HeaderRecord("In a galaxy far far away", 10000000))
  }

  override def placeSorter(hr: HeaderRecord, plc: Place) =
    calcDistance(
      plc.latitude, plc.longitude,
      latLon._1, latLon._2) <
      hr.id
  
}

private class CategoricalIndexer(garble: List[Place])
    extends AbsAbsPlaceIndexer(garble: List[Place]) {

  import edu.vanderbilt.vm.doreguide.container._
  
  def initializeHeaderRecord() = {
    List(
      ResidenceHall,
      Recreation,
      Dining,
      Academics,
      Everything,
      Facility,
      Medical,
      Local,
      Athletics,
      GreekLife,
      StudentLife,
      Library,
      Others).
      map(pCat => HeaderRecord(pCat.name, pCat.id))
  }

  override def placeSorter(hr: HeaderRecord, plc: Place) =
    plc.categories.length > 0 &&
      plc.categories.head.id == hr.id

} 
