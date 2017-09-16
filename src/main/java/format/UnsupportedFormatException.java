package format;

@SuppressWarnings("serial")
class UnsupportedFormatException extends RuntimeException {

	UnsupportedFormatException() {
		super("Could not determine format of content");	
	}
	
}
