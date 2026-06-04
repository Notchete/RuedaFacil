package Exceptions;

public class ElementoDuplicadoException extends Exception {

	private static final long serialVersionUID = 1L;

	public ElementoDuplicadoException(String mensaje) {
		super(mensaje);
    }
}