package edu.vanderbilt.vm.doreguide

case class MapVertex(
    neighbours: List[Int], 
    latitude: Double, 
    longitude: Double, 
    id: Int) {}