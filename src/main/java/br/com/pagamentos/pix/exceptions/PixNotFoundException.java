package br.com.pagamentos.pix.exceptions;

public class PixNotFoundException extends RuntimeException {
	public PixNotFoundException() {
		super();
	}

	public PixNotFoundException(String message) {
		super(message);
	}

}
