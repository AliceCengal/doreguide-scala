package edu.vanderbilt.vm.doreway

case class MapVertex(
    neighbours: List[Int], 
    latitude: Double, 
    longitude: Double, 
    id: Int) {}