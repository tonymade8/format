package format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Thread-safe.
 */
class JsonFormatter implements StringFormatter {

	private final static JsonParser parser = new JsonParser();
	private final static Gson gson = new GsonBuilder()
		.serializeNulls()
		.setLenient()
		.setPrettyPrinting()
		.create();
	
	public String format(String s) {
		
		if (s == null) {
			return null;
		}
		
		JsonElement el = parser.parse(s);
		return gson.toJson(el);
		
    }
	
}
