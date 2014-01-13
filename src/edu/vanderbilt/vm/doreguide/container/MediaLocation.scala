package edu.vanderbilt.vm.doreguide.container

case class MediaLocation(mediatype: MediaType, location: String) {}

abstract class MediaType {}
case object ImageMedia extends MediaType
case object VideoMedia extends MediaType
case object AudioMedia extends MediaType
case object ImageId    extends MediaType