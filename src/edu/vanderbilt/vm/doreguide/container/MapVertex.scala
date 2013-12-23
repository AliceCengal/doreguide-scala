package edu.vanderbilt.vm.doreguide.container

case class MapVertex(
    neighbours: List[Int], 
    latitude: Double, 
    longitude: Double, 
    id: Int) {}