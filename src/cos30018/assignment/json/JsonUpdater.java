package cos30018.assignment.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class JsonUpdater {
	protected boolean updateField(Object thisValue, Method carFieldSetter, Object instance) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		boolean res = thisValue != null;
		if (res) {
			carFieldSetter.invoke(instance, thisValue);
		}
		return res;
	}
}
