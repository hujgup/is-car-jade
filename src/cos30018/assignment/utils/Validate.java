package cos30018.assignment.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides utility methods for input validation. Do NOT use this for validating user input, as these methods all throw IllegalArgumentException - these are for validating method arguments.
 * 
 * @author Jake
 */
public class Validate {
	private static final String EXPECT_NUMBER = "must be a number";
	private static final String EXPECT_FINITE = "must be finite";
	private static final String EXPECT_POSITIVE = "must be positive";
	private static final String EXPECT_NEGATIVE = "must be negative";
	private static final String NOT_POSITIVE = "cannot be positive";
	private static final String NOT_NEGATIVE = "cannot be negative";
	private static final String NOT_NULL = "cannot be null";
	private static final String NOT_EMPTY = "cannot be empty";
	private static final String EXPECT_STARTS_WITH = "must start with the string";
	private static final String NOT_CONTAINS_STRING = "cannot contain any of the strings";
	private static final String EXPECT_EQUAL = "must be equal to";
	private static final String NOT_EQUAL = "must not be equal to";
	private static final String EXPECT_LT = "must be less than";
	private static final String EXPECT_LE = "must be less than or equal to";
	private static final String EXPECT_GT = "must be greater than";
	private static final String EXPECT_GE = "must be greater than or equal to";
	private static final String EXPECT_IN_RANGE = "must be inside range";
	private static final String EXPECT_OUT_RANGE = "must be outside range";
	private static final String EXPECT_IN_SET = "must be in set";
	private static final String EXPECT_KEY_IN_MAP = "must be a key in map";
	private static final String NOT_KEY_IN_MAP = "must not be a key in map";
	private static final String NOT_CONTAINS_LIST = "must not be in list";
	private static final String NOT_CLASS = "must not bey of type";
	private Validate() {
		// Pure static class
	}
	private static String strVal(String str) {
		return str != null ? str : "null";
	}
	private static String strVal(Object obj) {
		return obj != null ? obj.toString() : "null";
	}
	/**
	 * Throws a validation error derived from one value.
	 * 
	 * @param argVal The value that caused the error.
	 * @param argName The name of the variable that caused the error.
	 * @param details The details of the error.
	 */
	public static void throwUnary(String argVal, String argName, String details) {
		throw new IllegalArgumentException(strVal(argName) + " " + details + " (was " + argVal + ").");
	}
	/**
	 * Throws a validation error derived from one value.
	 * 
	 * @param argVal The value that caused the error.
	 * @param argName The name of the variable that caused the error.
	 * @param details The details of the error.
	 */
	public static void throwUnary(Object argVal, String argName, String details) {
		throwUnary(strVal(argVal), argName, details);
	}
	/**
	 * Throws a validation error derived from one value.
	 * 
	 * @param argVal The value that caused the error.
	 * @param argName The name of the variable that caused the error.
	 * @param details The details of the error.
	 */
	public static void throwUnary(double argVal, String argName, String details) {
		throwUnary(Double.toString(argVal), argName, details);
	}
	/**
	 * Throws a validation error derived from how two values are related.
	 * 
	 * @param aVal The first value that caused the error.
	 * @param bVal The second value that caused the error.
	 * @param aName The name of the first variable that caused the error.
	 * @param bName The name of the second variable that caused the error.
	 * @param details The details of the error.
	 */
	public static void throwBinary(String aVal, String bVal, String aName, String bName, String details) {
		throw new IllegalArgumentException(aName + " " + details + " " + bName + " (were " + strVal(aVal) + " and " + strVal(bVal) + " respectively).");
	}
	/**
	 * Throws a validation error derived from how two values are related.
	 * 
	 * @param aVal The first value that caused the error.
	 * @param bVal The second value that caused the error.
	 * @param aName The name of the first variable that caused the error.
	 * @param bName The name of the second variable that caused the error.
	 * @param details The details of the error.
	 */
	public static void throwBinary(Object aVal, Object bVal, String aName, String bName, String details) {
		throwBinary(strVal(aVal), strVal(bVal), aName, bName, details);
	}
	/**
	 * Throws a validation error derived from how two values are related, where one value is a constant.
	 * 
	 * @param aVal The variable value that caused the error.
	 * @param bVal The constant value whose relation to aVal caused the error.
	 * @param aName The name of the variable that caused the error.
	 * @param details The details of the error.
	 */
	public static void throwBinary(String aVal, String bVal, String aName, String details) {
		throw new IllegalArgumentException(aName + " " + details + " " + strVal(bVal) + " (was " + strVal(aVal) + ").");
	}
	/**
	 * Throws a validation error derived from how two values are related, where one value is a constant.
	 * 
	 * @param aVal The variable value that caused the error.
	 * @param bVal The constant value whose relation to aVal caused the error.
	 * @param aName The name of the variable that caused the error.
	 * @param details The details of the error.
	 */
	public static void throwBinary(Object aVal, Object bVal, String aName, String details) {
		throwBinary(strVal(aVal), strVal(bVal), aName, details);
	}
	/**
	 * Makes sure arg is a number.
	 * 
	 * @param arg The arg to check.
	 * @param argName The name of the arg.
	 */
	public static void isNumber(double arg, String argName) {
		if (Double.isNaN(arg)) {
			throwUnary(arg, argName, EXPECT_NUMBER);
		}
	}
	/**
	 * Makes sure arg is a finite number.
	 * 
	 * @param arg The arg to check.
	 * @param argName The name of the arg.
	 */
	public static void finite(double arg, String argName) {
		if (!Double.isFinite(arg)) {
			throwUnary(arg, argName, EXPECT_FINITE);
		}
	}
	/**
	 * Makes sure arg is a positive number.
	 * 
	 * @param arg The arg to check.
	 * @param argName The name of the arg.
	 */
	public static void positive(double arg, String argName) {
		if (arg <= 0) {
			throwUnary(arg, argName, EXPECT_POSITIVE);
		}
	}
	/**
	 * Makes sure arg is a negative number.
	 * 
	 * @param arg The arg to check.
	 * @param argName The name of the arg.
	 */
	public static void negative(double arg, String argName) {
		if (arg >= 0) {
			throwUnary(arg, argName, EXPECT_NEGATIVE);
		}
	}
	/**
	 * Makes sure arg is not a positive number.
	 * 
	 * @param arg The arg to check.
	 * @param argName The name of the arg.
	 */
	public static void notPositive(double arg, String argName) {
		if (arg > 0) {
			throwUnary(arg, argName, NOT_POSITIVE);
		}
	}
	/**
	 * Makes sure arg is not a negative number.
	 * 
	 * @param arg The arg to check.
	 * @param argName The name of the arg.
	 */
	public static void notNegative(double arg, String argName) {
		if (arg < 0) {
			throwUnary(arg, argName, NOT_NEGATIVE);
		}
	}
	/**
	 * Makes sure arg is not null.
	 * 
	 * @param arg The arg to check.
	 * @param argName The name of the arg.
	 */
	public static void notNull(Object arg, String argName) {
		if (arg == null) {
			throwUnary(arg, argName, NOT_NULL);
		}
	}
	/**
	 * Makes sure arg is not the empty string.
	 * 
	 * @param arg The arg to check.
	 * @param argName The name of the arg.
	 */
	public static void notEmpty(String arg, String argName) {
		if (arg.isEmpty()) {
			throwUnary(arg, argName, NOT_EMPTY);
		}
	}
	/**
	 * Makes sure arg starts with the string start.
	 * 
	 * @param arg The arg to check.
	 * @param start The string that arg should start with.
	 * @param argName The name of arg.
	 * @param startName The name of start.
	 */
	public static void startsWith(String arg, String start, String argName, String startName) {
		if (!arg.startsWith(start)) {
			throwBinary(arg, start, argName, startName, EXPECT_STARTS_WITH);
		}
	}
	/**
	 * Makes sure arg starts with the string start.
	 * 
	 * @param arg The arg to check.
	 * @param start The string that arg should start with.
	 * @param argName The name of the arg.
	 */
	public static void startsWith(String arg, String start, String argName) {
		if (!arg.startsWith(start)) {
			throwBinary(arg, start, argName, EXPECT_STARTS_WITH);
		}
	}
	/**
	 * Makes sure arg does not contain any of the specified strings.
	 * 
	 * @param arg The arg to check.
	 * @param argName The name of the arg.
	 * @param strings The strings that arg should not contain.
	 */
	public static void stringNotContains(String arg, String argName, String... strings) {
		String str;
		for (int i = 0; i < strings.length; i++) {
			str = strings[i];
			if (arg.indexOf(str) > -1) {
				throwBinary(arg, Arrays.toString(strings), argName, NOT_CONTAINS_STRING);
			}
		}
	}
	/**
	 * Makes sure that a and b are equal.
	 * 
	 * @param a The first arg.
	 * @param b The second arg.
	 * @param aName The name of the first arg.
	 * @param bName The name of the second arg.
	 */
	public static void equal(Object a, Object b, String aName, String bName) {
		if (!a.equals(b)) {
			throwBinary(a, b, aName, bName, EXPECT_EQUAL);
		}
	}
	/**
	 * Makes sure that a and b are equal.
	 * 
	 * @param a The first arg.
	 * @param b The second arg.
	 * @param aName The name of the first arg.
	 */
	public static void equal(Object a, Object b, String aName) {
		if (!a.equals(b)) {
			throwBinary(a, b, aName, EXPECT_EQUAL);
		}
	}
	/**
	 * Makes sure that a and b are not equal.
	 * 
	 * @param a The first arg.
	 * @param b The second arg.
	 * @param aName The name of the first arg.
	 * @param bName The name of the second arg.
	 */
	public static void notEqual(Object a, Object b, String aName, String bName) {
		if (a.equals(b)) {
			throwBinary(a, b, aName, bName, NOT_EQUAL);
		}
	}
	/**
	 * Makes sure that a and b are not equal.
	 * 
	 * @param a The first arg.
	 * @param b The second arg.
	 * @param aName The name of the first arg.
	 */
	public static void notEqual(Object a, Object b, String aName) {
		if (a.equals(b)) {
			throwBinary(a, b, aName, NOT_EQUAL);
		}		
	}
	/**
	 * Makes sure that left is less than right.
	 * 
	 * @param left The value on the left-hand side of the comparison.
	 * @param right The value on the right-hand side of the comparison.
	 * @param leftName The name of the left arg.
	 * @param rightName The name of the right arg.
	 */
	public static <T extends Comparable<T>> void lessThan(T left, T right, String leftName, String rightName) {
		if (left.compareTo(right) >= 0) {
			throwBinary(left, right, leftName, rightName, EXPECT_LT);
		}
	}
	/**
	 * Makes sure that left is less than right.
	 * 
	 * @param left The value on the left-hand side of the comparison.
	 * @param right The value on the right-hand side of the comparison.
	 * @param leftName The name of the left arg.
	 */
	public static <T extends Comparable<T>> void lessThan(T left, T right, String leftName) {
		if (left.compareTo(right) >= 0) {
			throwBinary(left, right, leftName, EXPECT_LT);
		}
	}
	/**
	 * Makes sure that left is less than or equal to right.
	 * 
	 * @param left The value on the left-hand side of the comparison.
	 * @param right The value on the right-hand side of the comparison.
	 * @param leftName The name of the left arg.
	 * @param rightName The name of the right arg.
	 */
	public static <T extends Comparable<T>> void lessThanOrEqualTo(T left, T right, String leftName, String rightName) {
		if (left.compareTo(right) > 0) {
			throwBinary(left, right, leftName, rightName, EXPECT_LE);
		}
	}
	/**
	 * Makes sure that left is less than or equal to right.
	 * 
	 * @param left The value on the left-hand side of the comparison.
	 * @param right The value on the right-hand side of the comparison.
	 * @param leftName The name of the left arg.
	 */
	public static <T extends Comparable<T>> void lessThanOrEqualTo(T left, T right, String leftName) {
		if (left.compareTo(right) > 0) {
			throwBinary(left, right, leftName, EXPECT_LE);
		}
	}
	/**
	 * Makes sure that left is greater than right.
	 * 
	 * @param left The value on the left-hand side of the comparison.
	 * @param right The value on the right-hand side of the comparison.
	 * @param leftName The name of the left arg.
	 * @param rightName The name of the right arg.
	 */
	public static <T extends Comparable<T>> void greaterThan(T left, T right, String leftName, String rightName) {
		if (left.compareTo(right) <= 0) {
			throwBinary(left, right, leftName, leftName, EXPECT_GT);
		}
	}
	/**
	 * Makes sure that left is greater than right.
	 * 
	 * @param left The value on the left-hand side of the comparison.
	 * @param right The value on the right-hand side of the comparison.
	 * @param leftName The name of the left arg.
	 */
	public static <T extends Comparable<T>> void greaterThan(T left, T right, String leftName) {
		if (left.compareTo(right) <= 0) {
			throwBinary(left, right, leftName, EXPECT_GT);
		}
	}
	/**
	 * Makes sure that left is greater than or equal to right.
	 * 
	 * @param left The value on the left-hand side of the comparison.
	 * @param right The value on the right-hand side of the comparison.
	 * @param leftName The name of the left arg.
	 * @param rightName The name of the right arg.
	 */
	public static <T extends Comparable<T>> void greaterThanOrEqualTo(T left, T right, String leftName, String rightName) {
		if (left.compareTo(right) < 0) {
			throwBinary(left, right, leftName, rightName, EXPECT_GE);
		}
	}
	/**
	 * Makes sure that left is greater than or equal to right.
	 * 
	 * @param left The value on the left-hand side of the comparison.
	 * @param right The value on the right-hand side of the comparison.
	 * @param leftName The name of the left arg.
	 */
	public static <T extends Comparable<T>> void greaterThanOrEqualTo(T left, T right, String leftName) {
		if (left.compareTo(right) < 0) {
			throwBinary(left, right, leftName, EXPECT_GE);
		}
	}
	/**
	 * Makes sure that arg is inside the specified range.
	 * 
	 * @param arg The arg to check.
	 * @param range The range that arg should be inside.
	 * @param argName The name of the arg.
	 * @param rangeName The name of the range.
	 */
	public static <T extends Comparable<T>> void inRange(T arg, Range<?, T> range, String argName, String rangeName) {
		if (range.argNotInRange(arg)) {
			throwBinary(arg, range, argName, rangeName, EXPECT_IN_RANGE);
		}
	}
	/**
	 * Makes sure that arg is inside the specified range.
	 * 
	 * @param arg The arg to check.
	 * @param range The range that arg should be inside.
	 * @param argName The name of the arg.
	 */
	public static <T extends Comparable<T>> void inRange(T arg, Range<?, T> range, String argName) {
		if (range.argNotInRange(arg)) {
			throwBinary(arg, range, argName, EXPECT_IN_RANGE);
		}
	}
	/**
	 * Makes sure that arg is outside the specified range.
	 * 
	 * @param arg The arg to check.
	 * @param range The range that arg should be outside.
	 * @param argName The name of the arg.
	 * @param rangeName The name of the range.
	 */
	public static <T extends Comparable<T>> void notInRange(T arg, Range<?, T> range, String argName, String rangeName) {
		if (range.argInRange(arg)) {
			throwBinary(arg, range, argName, rangeName, EXPECT_OUT_RANGE);
		}
	}
	/**
	 * Makes sure that arg is outside the specified range.
	 * 
	 * @param arg The arg to check.
	 * @param range The range that arg should be outside.
	 * @param argName The name of the arg.
	 */
	public static <T extends Comparable<T>> void notInRange(T arg, Range<?, T> range, String argName) {
		if (range.argInRange(arg)) {
			throwBinary(arg, range, argName, EXPECT_OUT_RANGE);
		}
	}
	/**
	 * Makes sure that arg is in the specified set.
	 * 
	 * @param arg The arg to check.
	 * @param set The set that arg should be in.
	 * @param argName The name of the arg.
	 * @param setName The name of the set.
	 */
	public static <T> void inSet(T arg, Set<? extends T> set, String argName, String setName) {
		if (!set.contains(arg)) {
			throwBinary(arg, set, argName, setName, EXPECT_IN_SET);
		}
	}
	/**
	 * Makes sure that arg is in the specified set.
	 * 
	 * @param arg The arg to check.
	 * @param set The set that arg should be in.
	 * @param argName The name of the arg.
	 */
	public static <T> void inSet(T arg, Set<? extends T> set, String argName) {
		if (!set.contains(arg)) {
			throwBinary(arg, set, argName, EXPECT_IN_SET);
		}
	}
	/**
	 * Makes sure that arg is a key in the specified map.
	 * 
	 * @param arg The arg to check.
	 * @param map The map that arg should be a key in.
	 * @param argName The name of the arg.
	 * @param mapName The name of the map.
	 */
	public static <T> void keyInMap(T arg, Map<? extends T, ?> map, String argName, String mapName) {
		if (!map.containsKey(arg)) {
			throwBinary(arg, map, argName, mapName, EXPECT_KEY_IN_MAP);
		}
	}
	/**
	 * Makes sure that arg is a key in the specified map.
	 * 
	 * @param arg The arg to check.
	 * @param map The map that arg should be a key in.
	 * @param argName The name of the arg.
	 */
	public static <T> void keyInMap(T arg, Map<? extends T, ?> map, String argName) {
		if (!map.containsKey(arg)) {
			throwBinary(arg, map, argName, EXPECT_KEY_IN_MAP);
		}
	}
	/**
	 * Makes sure that arg is not a key in the specified map.
	 * 
	 * @param arg The arg to check.
	 * @param map The map that arg should not be a key in.
	 * @param argName The name of the arg.
	 * @param mapName The name of the map.
	 */
	public static <T> void keyNotInMap(T arg, Map<? extends T, ?> map, String argName, String mapName) {
		if (map.containsKey(arg)) {
			throwBinary(arg, map, argName, mapName, NOT_KEY_IN_MAP);
		}
	}
	/**
	 * Makes sure that arg is not a key in the specified map.
	 * 
	 * @param arg The arg to check.
	 * @param map The map that arg should not be a key in.
	 * @param argName The name of the arg.
	 */
	public static <T> void keyNotInMap(T arg, Map<? extends T, ?> map, String argName) {
		if (map.containsKey(arg)) {
			throwBinary(arg, map, argName, NOT_KEY_IN_MAP);
		}
	}
	/**
	 * Makes sure that arg is not in the specified list.
	 * 
	 * @param arg The arg to check.
	 * @param list The list that arg should not be in.
	 * @param argName The name of the arg.
	 * @param listName The name of the list.
	 */
	public static <T> void listNotContains(T arg, List<? extends T> list, String argName, String listName) {
		if (list.contains(arg)) {
			throwBinary(arg, list, argName, listName, NOT_CONTAINS_LIST);
		}
	}
	/**
	 * Makes sure that arg is not in the specified list.
	 * 
	 * @param arg The arg to check.
	 * @param list The list that arg should not be in.
	 * @param argName The name of the arg.
	 */
	public static <T> void listNotContains(T arg, List<? extends T> list, String argName) {
		if (list.contains(arg)) {
			throwBinary(arg, list, argName, NOT_CONTAINS_LIST);
		}
	}
	/**
	 * Makes sure that arg is not of the specified type.
	 * 
	 * @param arg The arg to check.
	 * @param type The type that arg should not be.
	 * @param argName The name of the arg.
	 * @param typeName The name of the type.
	 */
	public static void notClass(Object arg, Class<?> type, String argName, String typeName) {
		if (arg.getClass().isAssignableFrom(type)) {
			throwBinary(arg, type, argName, typeName, NOT_CLASS);
		}
	}
	/**
	 * Makes sure that arg is not of the specified type.
	 * 
	 * @param arg The arg to check.
	 * @param type The type that arg should not be.
	 * @param argName The name of the arg.
	 */
	public static void notClass(Object arg, Class<?> type, String argName) {
		if (arg.getClass().isAssignableFrom(type)) {
			throwBinary(arg, type, argName, NOT_CLASS);
		}
	}
}
