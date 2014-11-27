import edu.clayton.lochchat.marshallers.ChatMarshaller
import edu.clayton.lochchat.marshallers.CustomObjectMarshallers
import edu.clayton.lochchat.marshallers.HttpStatusMarshaller
import edu.clayton.lochchat.marshallers.LogMarshaller
import edu.clayton.lochchat.marshallers.MessageMarshaller

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
