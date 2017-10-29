package cos30018.assignment.utils;

import java.io.IOException;

public class RuntimeIOException extends RuntimeException {
	private static final long serialVersionUID = 2521631433243999724L;
	public RuntimeIOException(IOException e) {
		super(e);
	}
}
