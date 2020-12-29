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

import java.io.Serializable;
import java.util.Iterator;

/**
 * Represents a tuple whose elements type are bound to a given
 * {@link TupleSchema tuple schema}. An instance of this class is a particular
 * instance of a {@link TupleSchema tuple schema}.
 * 
 * @author Salvatore Giampa'
 *
 */
public class BoundTuple implements Serializable, Iterable<Object>, Comparable<Object> {
	private static final long serialVersionUID = -8621645127811882743L;
	private TupleSchema tupleSchema;
	private Tuple tuple;

	/**
	 * Creates a new {@link BoundTuple} object basing on the given
	 * {@link TupleSchema schema}
	 * 
	 * @param tupleSchema the tuple schema to bound this tuple to
	 *
	 * @param values      the values associated to the attributes of the given
	 *                    {@link TupleSchema tupleSchema}, in the order they are
	 *                    represented.
	 * @return a new bound tuple
	 */
	public static BoundTuple of(TupleSchema tupleSchema, Object... values) {
		return new BoundTuple(tupleSchema, values);
	}

	/**
	 * Creates a new {@link BoundTuple} object basing on the given
	 * {@link TupleSchema
	 * 
	 * @param tupleSchema the tupleSchema to boud this {@link BoundTuple} to
	 * @param values      the values associated to the attributes of the given
	 *                    {@link TupleSchema tupleSchema}, in the order they are
	 *                    represented.
	 */
	private BoundTuple(TupleSchema tupleSchema, Object... values) throws TupleSchemaException {
		this.tupleSchema = tupleSchema;
		tupleSchema.check(values);
		tuple = Tuple.of(values);
	}

	/**
	 * Gets the {@link TupleSchema tupleSchema} this {@link BoundTuple} is bound to.
	 * 
	 * @return the {@link TupleSchema tupleSchema} of this {@link BoundTuple}.
	 */
	public TupleSchema getSchema() {
		return tupleSchema;
	}

	/**
	 * Gets a {@link Tuple tuple} (untyped) representation of this
	 * {@link BoundTuple}.
	 * 
	 * @return a tuple containing the values of this {@link BoundTuple}
	 */
	public Tuple asTuple() {
		return tuple;
	}

	/**
	 * Gets the value that corresponds to the given attribute. This method allows to
	 * check the expected formal type at runtime, and to prevent supposed casual
	 * runtime errors due to dynamic type checking.
	 * 
	 * @param <T>        the return type of the value
	 * @param attribute  the attribute to get the value of
	 * @param actualType the expected actual type to compare with the formal type
	 *                   registered in the tupleSchema.
	 * @return the value that corresponds to the given attribute in this
	 *         {@link BoundTuple}
	 * @throws TupleSchemaException If the attribute is not defined for the
	 *                              tupleSchema or the specified return type T is
	 *                              not the same of the type that corresponds to the
	 *                              requested attribute in the tupleSchema
	 */
	public <T> T get(String attribute, Class<T> actualType) throws TupleSchemaException {
		int index = tupleSchema.getIndex(attribute);
		tupleSchema.check(index, actualType);
		return tuple.get(index);
	}

	/**
	 * Gets the value that corresponds to the given attribute. This method do not
	 * check the actual type as long as the {@link #get(String, Class)} method. If
	 * the given T type cannot be assigned to the formal parameter, invoking this
	 * method can cause a {@link ClassCastException} at runtime in some unexpected
	 * way, due to programming errors.
	 * 
	 * @param <T>       the return type of the value
	 * @param attribute the attribute to get the value of
	 * @return the value that corresponds to the given attribute in this
	 *         {@link BoundTuple}
	 * @throws TupleSchemaException If the attribute is not defined for the
	 *                              tupleSchema
	 * @throws ClassCastException   If the specified return type T is not compatible
	 *                              with the type that corresponds to the requested
	 *                              attribute in the tupleSchema
	 */
	public <T> T get(String attribute) throws TupleSchemaException {
		return tuple.get(tupleSchema.getIndex(attribute));
	}

	/**
	 * Gets the value that corresponds to the given attribute. This method do not
	 * check the actual type as long as the {@link #get(String, Class)} method. If
	 * the given T type cannot be assigned to the formal parameter, invoking this
	 * method can cause a {@link ClassCastException} at runtime in some unexpected
	 * way, due to programming errors.
	 * 
	 * @param <T>       the return type of the value
	 * @param index the index of the value to get
	 * @return the value that corresponds to the given attribute in this
	 *         {@link BoundTuple}
	 * @throws IndexOutOfBoundsException If the attribute index is not defined for
	 *                                   the tupleSchema
	 */
	public <T> T get(int index) {
		return tuple.get(index);
	}

	/**
	 * Gets the number of values in this {@link BoundTuple} (i.e. the number of
	 * attributes of the {@link TupleSchema} this {@link BoundTuple} is bound to)
	 * 
	 * @return the length of this {@link BoundTuple}
	 */
	public int length() {
		return tupleSchema.length();
	}

	@Override
	public String toString() {
		return BoundTuple.class.getSimpleName() + ": " + tuple;
	}

	@Override
	public int compareTo(Object o) {
		return tuple.compareTo(o);
	}

	@Override
	public Iterator<Object> iterator() {
		return tuple.iterator();
	}
}
