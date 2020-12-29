/*
 *
 * Copyright 2020 Salvatore Giampa'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jtuple;

import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a generic tuple of objects. The behavior of this structure allows
 * to write simple code, while aggregating objects in a single immutable
 * structure.<br>
 * A tuple is a statically sized structure, whose elements cannot be removed,
 * added neither replaced after its construction.<br>
 * A tuple object is {@link Serializable serializable}, {@link Iterable
 * iterable} and lexicographically {@link Comparable comparable} to any other
 * tuple object.<br>
 * A tuple object is perfectly suitable for using it as a key for hash maps and
 * sorted maps.<br>
 * <br>
 * The {@link #get(int)} method allows to get the object at the given index in
 * the tuple, casting it automatically to the destination type.<br>
 * <br>
 * The {@link #toArray()} and the {@link #toArray(Class)} methods allow to get
 * an array, of objects or of the specified type, respectively, containing the
 * values of the tuple. These methods can be used to pass the tuple values as
 * varargs to methods (e.g., to the {@link PrintStream#printf(String, Object...)}
 * method).
 * This class implements persistence (i.e., modifier operations generate new tuples,
 * rather than modifying the current one).
 * 
 * @author Salvatore Giampa'
 *
 */
public class Tuple implements Serializable, Iterable<Object>, Comparable<Object> {
	private static final long serialVersionUID = -175441961444205562L;

	private static Tuple EMPTY_TUPLE = new Tuple();

	/**
	 * Class used to build new tuples avoiding proliferation. To intantiate a new
	 * Tuple.Builder, the {@link Tuple#builder()} method must be used.
	 * 
	 * @author Salvatore Giampa'
	 *
	 */
	public static class Builder {
		private ArrayList<Object> values = new ArrayList<>();

		private Builder() {}

		/**
		 * Insert new tuple values at the specified index
		 * 
		 * @param index  the index at which values must be inserted. All values after
		 *               this index are shifted forward
		 * @param values the values to insert
		 * @return this same {@link Builder builder}
		 */
		public Builder insert(int index, Object... values) {
			this.values.addAll(index, Arrays.asList(values));
			return this;
		}

		/**
		 * Adds new values at the end of the tuple that is building
		 * 
		 * @param values the values to add
		 * @return this same {@link Builder builder}
		 */
		public Builder add(Object... values) {
			this.values.addAll(Arrays.asList(values));
			return this;
		}

		/**
		 * Insert values on at the head of the tuple
		 * @param values the values to add
		 * @return this same {@link Builder builder}
		 */
		public Builder insertHead(Object... values) {
			return insert(0, values);
		}

		public Tuple build() {
			return Tuple.of(values);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Creates a tuple of objects, from a collection.
	 * 
	 * @param values the objects to insert into the new values
	 * @return the new tuple
	 */
	public static <T> Tuple of(Collection<T> values) {
		return new Tuple(values);
	}

	/**
	 * Creates a tuple of objects.
	 * 
	 * @param values the objects to insert into the new values
	 * @return the new tuple
	 */
	public static Tuple of(Object... values) {
		return Tuple.of(Arrays.asList(values));
	}

	/**
	 * Creates a tuple of strings. Builds the tuple from the string
	 * representations of the given objects. The string representation of an object
	 * is given by the {@link Object#toString()} method.
	 * 
	 * @param values the objects whose string representations are inserted into the
	 *               new values
	 * @return the new tuple
	 */
	public static Tuple ofStrings(Object... values) {
		List<String> strValues = new LinkedList<>();
		for (Object v : values)
			strValues.add(v.toString());
		return Tuple.of(strValues);
	}

	/**
	 * Creates a tuple of strings from a collection. Builds the tuple from the
	 * string representations of the given objects. The string reprensentation of an
	 * object is given by the {@link Object#toString()} method.
	 * 
	 * @param values the objects whose string representations are inserted into the
	 *               new values
	 * @return the new tuple
	 */
	public static <T> Tuple ofStrings(Collection<T> values) {
		List<String> strValues = new LinkedList<>();
		for (Object v : values)
			strValues.add(v.toString());
		return Tuple.of(strValues);
	}

	/**
	 * Gets an empty tuple.
	 * 
	 * @return an empty tuple
	 */
	public static Tuple empty() {
		return EMPTY_TUPLE;
	}

	// values of the values
	private ArrayList<Object> values;

	/**
	 * Builds a new tuple of objects
	 * 
	 * @param values the values of the new tuple
	 */
	private Tuple(Object... values) {
		this(Arrays.asList(values));
	}

	/**
	 * Builds a new values from the given collection
	 * 
	 * @param values the collection containing the values of the new values
	 */
	private <T> Tuple(Collection<T> values) {
		this.values = new ArrayList<>(values);
		this.values.trimToSize();
	}

	/**
	 * Gets the value at the given index in the tuple, casting it automatically to
	 * the destination type.<br>
	 * 
	 * @param <T>   the destination type, given by the declaring type of the
	 *              destination variable or parameter
	 * @param index the index of the value to get, from 0 to {@link #length()}-1
	 * @return the value object at the given index
	 */
	public <T> T get(int index) {
		@SuppressWarnings("unchecked")
		T value = (T) values.get(index);
		return value;
	}

	/**
	 * Gets the value at the given index in the tuple, as String. Inoking this
	 * method has same effect of the following code:<br>
	 * <pre><code>get(index).toString()</code></pre><br>
	 * 
	 * @param index the index of the value to get, from 0 to {@link #length()}-1
	 * @return the value object at the given index as String
	 */
	public String getString(int index) {
		return values.get(index)
				.toString();
	}

	/**
	 * Builds a new tuple obtained by adding the specified values at the given
	 * index into the current tuple. However, the current tuple is not modified,
	 * and the new tuple is independent from it.
	 * 
	 * @param index  the index at which the values must be added
	 * @param values the values to add to the new tuple
	 * @return a new tuple with the new values or this same tuple if no value is
	 *         specified.
	 */
	public Tuple add(int index, Object... values) {
		if (index < 0)
			index = 0;
		if (index > this.values.size())
			index = this.values.size();
		if (values.length == 0)
			return this;
		Tuple tuple = new Tuple(this.values.subList(0, index));
		for (Object value : values)
			tuple.values.add(value);
		tuple.values.addAll(this.values.subList(index, this.values.size()));
		tuple.values.trimToSize();
		return tuple;
	}

	public Tuple add(Object... values) {
		return add(values.length, values);
	}

	public Tuple push(Object... values) {
		return add(0, values);
	}

	public Tuple remove(int... indexes) {
		Tuple tuple = new Tuple();
		Set<Integer> indexSet = new HashSet<>();
		for (int index : indexes)
			indexSet.add(index);
		for (int i = 0; i < this.values.size(); i++)
			if (!indexSet.contains(i))
				tuple.values.add(this.values.get(i));
		if (tuple.isEmpty())
			tuple = EMPTY_TUPLE;
		else
			tuple.values.trimToSize();
		return tuple;
	}

	public Tuple remove(int from, int to) {
		Tuple tuple = new Tuple();
		tuple.values.addAll(this.values.subList(0, from));
		tuple.values.addAll(this.values.subList(to, this.values.size()));
		tuple.values.trimToSize();
		return tuple;
	}

	/**
	 * Builds a new tuple containing the values of this tuple followed by the
	 * values of the specified one.<br>
	 * The current tuple is not modified.
	 * 
	 * @param tuple the values whose values must be added to the new values
	 * @return a new values with the new values or this values if the specified
	 *         values has its {@link #length()} equal to 0.
	 */
	public Tuple concat(Tuple tuple) {
		if (tuple.isEmpty())
			return this;
		Tuple concat = new Tuple();
		concat.values.addAll(this.values);
		concat.values.addAll(tuple.values);
		concat.values.trimToSize();
		return concat;
	}

	/**
	 * Get the length of this tuple
	 * 
	 * @return the number of values contained in this tuple
	 */
	public int length() {
		return values.size();
	}

	/**
	 * Gets a new tuple that contains all elements of this tuple in the spacified
	 * range.
	 * 
	 * @param from index range start, inclusive
	 * @param to   index range end, exclusive
	 * @return the sub-tuple, backed by this tuple
	 */
	public Tuple subTuple(int from, int to) {
		if (values.isEmpty())
			return this;
		return new Tuple(values.subList(from, to));
	}

	/**
	 * Gets the first element of this tuple.
	 * 
	 * @param <T> the destination type, given by the declaring type of the
	 *            destination variable or parameter
	 * @return the element at index 0 (same of invoking {@link #get(int) get}(0))
	 */
	public <T> T first() {
		@SuppressWarnings("unchecked")
		T result = (T) values.get(0);
		return result;
	}

	/**
	 * Gets the last element of this tuple.
	 * 
	 * @param <T> the destination type, given by the declaring type of the
	 *            destination variable or parameter
	 * @return the element at index {@link #length()}-1 (same of invoking
	 *         {@link #get(int) get}({@link #length()}-1))
	 */
	public <T> T last() {
		@SuppressWarnings("unchecked")
		T result = (T) values.get(values.size() - 1);
		return result;
	}

	/**
	 * Gets a tuple that contains all elements of this tuple, except for the last one.
	 * 
	 * @return a new tuple, backed by this tuple
	 */
	public Tuple head() {
		return subTuple(0, length() - 1);
	}

	/**
	 * Gets a tuple that contains all elements of this tuple, except for the first one.
	 * 
	 * @return a new tuple, backed by this tuple
	 */
	public Tuple tail() {
		return subTuple(1, length());
	}

	/**
	 * Gets the "empty" status of this tuple. <br>
	 * A tuple is "empty" (i.e. it is a "null values") if it has no elements, i.e.
	 * its {@link #length() length} is 0.
	 * 
	 * @return true if the tuple has no element, false otherwise
	 */
	public boolean isEmpty() {
		return values.isEmpty();
	}

	/**
	 * Gets all values of this tuple into an array of objects
	 * 
	 * @return an {@link Object} array
	 */
	public Object[] toArray() {
		return values.toArray();
	}

	/**
	 * Gets all values of this tuple into an array of the specified type
	 * 
	 * @param <T> the component type of the returned array
	 * @param cls the {@link Class} object relative to the component type of the
	 *            returned array
	 * @return an array of type T
	 */
	public <T> T[] toArray(Class<T> cls) {
		@SuppressWarnings("unchecked")
		T[] result = (T[]) Array.newInstance(cls, values.size());
		return values.toArray(result);
	}

	/**
	 * Put all the values of this tuple into a new {@link List list}
	 * 
	 * @return a {@link List list} containing the values of this tuple
	 */
	public List<Object> toList() {
		return new ArrayList<>(values);
	}

	/**
	 * Put all the values of this tuple into a new {@link List list}, whose element
	 * type is the same specified by cls parameter.
	 * 
	 * @param cls the type of the element of the returned list
	 * @return a {@link List list} containing the values of this tuple
	 */
	public <T> List<T> toList(Class<T> cls) {
		List<T> result = new ArrayList<T>();
		for (Object value : values) {
			T tValue = cls.cast(value);
			result.add(tValue);
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + ((values == null) ? 0 : values.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple other = (Tuple) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		if (values.size() > 0)
			sb.append(values.get(0));
		for (int i = 1; i < values.size(); i++)
			sb.append(", ")
					.append(values.get(i));
		sb.append(")");
		return sb.toString();
	}

	@Override
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {
			Iterator<Object> it = values.iterator();

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public Object next() {
				return it.next();
			}

			// no remove operation
		};
	}

	public <T> Iterator<T> typedIterator() {
		return new Iterator<T>() {
			Iterator<Object> it = values.iterator();

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@SuppressWarnings("unchecked")
			@Override
			public T next() {
				return (T) it.next();
			}

			// no remove operation
		};
	}

	@Override
	public int compareTo(Object o) {
		if (o == null)
			return 1;
		if (o instanceof Tuple) {
			Tuple t2 = (Tuple) o;
			if (t2.length() == 0 && length() != 0)
				return 1;
			if (length() == 0 && t2.length() != 0)
				return -1;
			int minLength = Math.min(this.length(), t2.length());
			for (int i = 0; i < minLength; i++) {
				int comp = values.get(i)
						.toString()
						.compareTo(t2.values.get(i)
								.toString());
				if (comp != 0)
					return comp;
			}
			return length() > t2.length() ? 1 : length() < t2.length() ? -1 : 0;
		} else {
			if (length() == 0)
				return -1;
			StringBuilder sb = new StringBuilder();
			for (Object v : values)
				sb.append(v.toString());
			return sb.toString()
					.compareTo(o.toString());
		}
	}
}