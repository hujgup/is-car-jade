package cos30018.assignment.logic;

import com.google.gson.annotations.SerializedName;
import cos30018.assignment.ui.json.JsonConvertible;
import cos30018.assignment.utils.Validate;

public class ActionResult<T> implements JsonConvertible<Object> {
	private class JsonResult {
		@SerializedName("result")
		private T result2;
		public JsonResult() {
			this.result2 = result;
		}
	}
	
	private class JsonError {
		@SerializedName("error")
		private String error2;
		public JsonError() {
			this.error2 = error;
		}
	}
	
	private T result;
	private String error;
	private ActionResult() {
	}
	public static <T> ActionResult<T> createResult(T result) {
		Validate.notNull(result, "result");
		ActionResult<T> res = new ActionResult<>();
		res.result = result;
		res.error = null;
		return res;
	}
	public static <T> ActionResult<T> createError(String error) {
		Validate.notNull(error, "error");
		Validate.notEmpty(error, "error");
		ActionResult<T> res = new ActionResult<>();
		res.result = null;
		res.error = error;
		return res;
	}
	public boolean hasResult() {
		return result != null;
	}
	public T getResult() {
		return result;
	}
	public boolean isError() {
		return error != null;
	}
	public String getError() {
		return error;
	}
	public Object toJson() {
		return hasResult() ? new JsonResult() : new JsonError();
	}
}
