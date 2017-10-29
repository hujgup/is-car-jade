package cos30018.assignment.utils;

public class RuntimeClassNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 772931667039296760L;
	public RuntimeClassNotFoundException(ClassNotFoundException e) {
		super(e);
	}

}
