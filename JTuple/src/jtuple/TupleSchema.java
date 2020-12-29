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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a schema used to bound the element types of a {@link BoundTuple
 * bound tuple}. A tuple schema is essentialy similar to a database schema,
 * through whitch we can define the types of the attributes of a tuple and their
 * names.
 * 
 * @author Salvatore Giampa'
 *
 */
public class TupleSchema implements Serializable {
	private static final long serialVersionUID = -6398594206283342329L;
	private Map<String, Integer> indexByAttribute = new HashMap<>();
	private Map<Integer, String> attributeByIndex = new HashMap<>();
	private Map<Integer, Class<?>> typeByIndex = new HashMap<>();

	private List<String> attributes = new ArrayList<>();

	/**
	 * Gets a {@link Builder builder} used to construct a new {@link TupleSchema
	 * tuple schema}.
	 * 
	 * @return a {@link Builder builder} of {@link TupleSchema}.
	 */
	public static Builder create() {
		return new Builder();
	}

	/**
	 * A builder class to construct new {@link TupleSchema tuple schemas}.
	 * 
	 * @author Salvatore Giampa'
	 *
	 */
	public static class Builder {
		private TupleSchema tupleSchema = new TupleSchema();
		private int attributeCount = 0;

		private Builder() {}

		public synchronized Builder addAttribute(String name) {
			return addAttribute(name, Object.class);
		}

		/**
		 * Add a new non-key attribute to the schema.
		 * 
		 * @param name the name of the attribute
		 * @param type the type of the attribute
		 * @return this builder
		 */
		public synchronized Builder addAttribute(String name, Class<?> type) {
			if (attributeExists(name))
				throw new IllegalStateException("Attribute " + name + " already exists.");
			tupleSchema.attributeByIndex.put(attributeCount, name);
			tupleSchema.indexByAttribute.put(name, attributeCount);
			tupleSchema.typeByIndex.put(attributeCount, type);
			tupleSchema.attributes.add(name);
			attributeCount++;
			return this;
		}

		/**
		 * Checks if a generic (key or non-key) attribute with the given name exists.
		 * 
		 * @param name the name of the attribute to check
		 * @return true if an attribute with the given name exists, false otherwise
		 */
		public synchronized boolean attributeExists(String name) {
			return tupleSchema.indexByAttribute.containsKey(name);
		}

		/**
		 * Gets all the attributes currently registered.
		 * 
		 * @return the list of all attributes
		 */
		public synchronized List<String> getCurrentAttributes() {
			return tupleSchema.getAttributes();
		}

		/**
		 * Builds the tuple schema.
		 * 
		 * @return a copy of the backed {@link TupleSchema schema}, so that the builder
		 *         can be used to create new schemas starting by the last one built.
		 */
		public synchronized TupleSchema build() {
			TupleSchema built = new TupleSchema(tupleSchema);
			return built;
		}
	}

	private TupleSchema() {}

	/**
	 * Copy constructor
	 * 
	 * @param tupleSchema instance to copy
	 */
	private TupleSchema(TupleSchema tupleSchema) {
		this.indexByAttribute.putAll(tupleSchema.indexByAttribute);
		this.attributeByIndex.putAll(tupleSchema.attributeByIndex);
		this.typeByIndex.putAll(tupleSchema.typeByIndex);
		this.attributes.addAll(tupleSchema.attributes);
	}

	public List<String> getAttributes() {
		return Collections.unmodifiableList(attributes);
	}

	public boolean hasAttribute(String attribute) {
		return indexByAttribute.containsKey(attribute);
	}

	public int getIndex(String attribute) {
		if (!indexByAttribute.containsKey(attribute))
			throw new TupleSchemaException("Attribute \"" + attribute + "\" not found in this tupleSchema");
		return indexByAttribute.get(attribute);
	}

	public String getAttribute(int attributeId) {
		if (!attributeByIndex.containsKey(attributeId))
			throw new TupleSchemaException(
					"No attribute with index=\"" + attributeId + "\" was found in this tupleSchema");
		return attributeByIndex.get(attributeId);
	}

	public Class<?> getType(Integer attributeId) {
		if (!typeByIndex.containsKey(attributeId))
			throw new TupleSchemaException(
					"No attribute type with index=\"" + attributeId + "\" was found in this tupleSchema");
		return typeByIndex.get(attributeId);
	}

	public Class<?> getType(String attribute) {
		return getType(getIndex(attribute));
	}

	public void check(String attribute, Class<?> actualType) throws TupleSchemaException {
		int id = indexByAttribute.get(attribute);
		check(id, actualType);
	}

	public void check(int id, Class<?> actualType) throws TupleSchemaException {
		Class<?> formalType = typeByIndex.get(id);
		if (!formalType.equals(actualType))
			throw new TupleSchemaException(
					String.format("Attribute \"%s\" (indexd=%s) is not assignable from actual type \"%s\"",
							attributeByIndex.get(id), id, actualType));
	}

	public void check(Tuple tuple) throws TupleSchemaException {
		check(tuple.toArray());
	}

	public void check(Object... values) throws TupleSchemaException {
		check(Arrays.asList(values));
	}

	public void check(List<Object> values) throws TupleSchemaException {
		if (values.size() != indexByAttribute.size())
			throw new TupleSchemaException(
					"the number of the given values is not equal to the number of attributes in this tupleSchema");

		for (int i = 0; i < values.size(); i++) {
			Object value = values.get(i);
			Class<?> actualType = value.getClass();
			check(i, actualType);
		}
	}

	public int length() {
		return indexByAttribute.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TupleSchema: {\n");
		for (int index = 0; index < attributeByIndex.size(); index++) {
			String attribute = attributeByIndex.get(index);
			Class<?> type = typeByIndex.get(index);
			sb.append(String.format("\t %s:  %s [%s]\n", index, attribute, type));
		}
		sb.append("}\n");
		return sb.toString();
	}
}
