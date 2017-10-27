package cos30018.assignment.ui.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Json {
	public static final Gson OBJ = new Gson();
	private Json() {
	}
	public static <T> T deserialize(String json, TypeToken<T> type) {
		return OBJ.fromJson(json, type.getType());
	}
	public static <T> T deserialize(String json, Class<T> type) {
		return OBJ.fromJson(json, type);
	}
	public static String serialize(Object obj) {
		return OBJ.toJson(obj);
	}
}
