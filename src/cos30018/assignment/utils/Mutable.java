package cos30018.assignment.utils;

public class Mutable<T> {
	public T value;
	public Mutable(T value) {
		this.value = value;
	}
	public Mutable() {
		this.value = null;
	}
}
