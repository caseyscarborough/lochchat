package edu.clayton.lochchat.marshallers

class CustomObjectMarshallers {
  List<ObjectMarshaller> marshallers = new ArrayList<ObjectMarshaller>()

  def registerAllMarshallers() {
    marshallers.each { marshaller -> marshaller.register() }
  }
}
