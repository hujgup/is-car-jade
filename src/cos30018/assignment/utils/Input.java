package cos30018.assignment.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import jade.lang.acl.ACLMessage;

public class Input {
	public static interface OisConsumer {
		void accept(ObjectInputStream ois) throws IOException, ClassNotFoundException;
	}
	
	private Input() {
	}
	public static void read(ACLMessage msg, OisConsumer reader) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(msg.getByteSequenceContent())) {
			ObjectInputStream ois = new ObjectInputStream(bis);
			reader.accept(ois);
		}
	}
}
