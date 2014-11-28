import edu.clayton.lochchat.marshallers.*

// Place your Spring DSL code here
beans = {
  customObjectMarshallers(CustomObjectMarshallers) {
    marshallers = [
      new MessageMarshaller(),
      new ChatMarshaller(),
      new LogMarshaller(),
      new HttpStatusMarshaller()
    ]
  }
}
