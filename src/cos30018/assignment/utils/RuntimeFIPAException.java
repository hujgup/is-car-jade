package cos30018.assignment.utils;

import jade.domain.FIPAException;

public class RuntimeFIPAException extends RuntimeException {
	private static final long serialVersionUID = 3156196559866998112L;
	public RuntimeFIPAException(FIPAException e) {
		super(e);
	}

}
