package edu.vanderbilt.vm.doreguide

case class Node(
    id: Int, 
    latitude: Double, // in degrees
    longitude: Double,  // in degrees
    neighbours: Set[Int]) {
  
  val isPlace: Boolean = 
    if (id < 9999 && id >= 0) true else false // Don't kill me Dr Roth!!!
  
}

object Node {
  
  val DEFAULT_ID = -1
  val DEGPERRAD = 180/ java.lang.Math.PI
  val EPSILON = 0.000001 // Resolution of the Node network
  val RADIUS_EARTH = 6371 // kilometers
  def simpleNode(lat: Double, lon: Double) = 
    Node(
        DEFAULT_ID, 
        lat, 
        lon, 
        Set.empty)
        
  def fromPlace(place: Place) = 
    Node(
        place.uniqueId, 
        place.latitude,
        place.longitude,
        Set.empty)
  
}

class RichNode(val node: Node) {
  var score: Double = 0
  var previous: Int = Node.DEFAULT_ID
  
  val latitudeInRads = node.latitude/Node.DEGPERRAD
  val longitudeInRads = node.longitude/Node.DEGPERRAD
  
  def distanceTo(other: RichNode) = {
    val dy = other.latitudeInRads - this.latitudeInRads
    val dx = other.longitudeInRads - this.longitudeInRads
    Node.RADIUS_EARTH * Math.sqrt(
        dx * dx * Math.cos(this.latitudeInRads) * Math.cos(this.latitudeInRads +
        dy * dy))
  }
  
  def distanceNaive(other: RichNode) = {
    val dy = other.node.latitude - this.node.latitude
    val dx = other.node.longitude - this.node.longitude
    Math.sqrt(dy * dy + dx * dx)
  }
}










