/*
 * Copyright 2014 Samppa Saarela
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.javersion.object.mapping;

import org.javersion.object.TypeContext;
import org.javersion.object.types.PrimitiveValueType;
import org.javersion.object.types.ValueType;
import org.javersion.path.PropertyPath;

public abstract class PrimitiveTypeMapping implements TypeMapping {

    private final Class<?> primitiveType;

    private final Class<?> wrapperType;

    private PrimitiveTypeMapping(Class<?> wrapperType, Class<?> primitiveType) {
        this.wrapperType = wrapperType;
        this.primitiveType = primitiveType;
    }

    @Override
    public boolean applies(PropertyPath path, TypeContext typeContext) {
        Class<?> clazz = typeContext.type.getRawType();
        return wrapperType.equals(clazz) || primitiveType.equals(clazz);
    }

    public static final PrimitiveTypeMapping LONG = new PrimitiveTypeMapping(Long.class, long.class) {
        @Override
        public ValueType getValueType() {
            return PrimitiveValueType.LONG;
        }
    };

    public static final PrimitiveTypeMapping INT = new PrimitiveTypeMapping(Integer.class, int.class) {
        @Override
        public ValueType getValueType() {
            return PrimitiveValueType.INT;
        }
    };

    public static final PrimitiveTypeMapping SHORT = new PrimitiveTypeMapping(Short.class, short.class) {
        @Override
        public ValueType getValueType() {
            return PrimitiveValueType.SHORT;
        }
    };

    public static final PrimitiveTypeMapping BYTE = new PrimitiveTypeMapping(Byte.class, byte.class) {
        @Override
        public ValueType getValueType() {
            return PrimitiveValueType.BYTE;
        }
    };

    public static final PrimitiveTypeMapping BOOLEAN = new PrimitiveTypeMapping(Boolean.class, boolean.class) {
        @Override
        public ValueType getValueType() {
            return PrimitiveValueType.BOOLEAN;
        }
    };

    public static final PrimitiveTypeMapping DOUBLE = new PrimitiveTypeMapping(Double.class, double.class) {
        @Override
        public ValueType getValueType() {
            return PrimitiveValueType.DOUBLE;
        }
    };

    public static final PrimitiveTypeMapping FLOAT = new PrimitiveTypeMapping(Float.class, float.class) {
        @Override
        public ValueType getValueType() {
            return PrimitiveValueType.FLOAT;
        }
    };

    public static final PrimitiveTypeMapping CHAR = new PrimitiveTypeMapping(Character.class, char.class) {
        @Override
        public ValueType getValueType() {
            return PrimitiveValueType.CHAR;
        }
    };

}