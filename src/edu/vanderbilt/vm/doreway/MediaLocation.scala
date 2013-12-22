package edu.vanderbilt.vm.doreway

case class MediaLocation(mediatype: MediaType, location: String) {}

abstract class MediaType {}
case class ImageMedia extends MediaType {}
case class VideoMedia extends MediaType {}
case class AudioMedia extends MediaType {}
