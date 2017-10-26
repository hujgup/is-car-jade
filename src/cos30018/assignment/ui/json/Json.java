package cos30018.assignment.ui.json;

import com.google.gson.reflect.TypeToken;

public class Json {
	private Json() {
	}
	public static <T> T deserialize(String json, TypeToken<T> type) {
		return Provider.OBJ.fromJson(json, type.getType());
	}
	public static <T> T deserialize(String json, Class<T> type) {
		return Provider.OBJ.fromJson(json, type);
	}
	public static String serialize(JsonConvertible<?> c) {
		return Provider.OBJ.toJson(c.toJson());
	}
}
