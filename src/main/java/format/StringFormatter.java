package format;

/**
 * Not guaranteed to be thread-safe.
 */
interface StringFormatter {

	String format(String s);
	
	default boolean canProbablyFormat(String s) {
		try {
			format(s);
			return true;
		} catch (RuntimeException e) {
			return false;	
		}
	}
	
}
