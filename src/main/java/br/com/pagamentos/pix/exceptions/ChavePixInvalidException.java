package br.com.pagamentos.pix.exceptions;

public class ChavePixInvalidException extends RuntimeException {
	public ChavePixInvalidException() {
		super();
	}

	public ChavePixInvalidException(String message) {
		super(message);
	}

}
