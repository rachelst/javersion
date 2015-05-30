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

import java.util.Map;
import java.util.Optional;

import org.javersion.object.DescribeContext;
import org.javersion.object.LocalTypeDescriptor;
import org.javersion.object.types.MapType;
import org.javersion.object.types.ScalarType;
import org.javersion.object.types.ValueType;
import org.javersion.path.PropertyPath;
import org.javersion.reflect.TypeDescriptor;

public class MapTypeMapping implements TypeMapping {

    private final Class<? extends Map> mapType;

    public MapTypeMapping() {
        this(Map.class);
    }

    public MapTypeMapping(Class<? extends Map> mapType) {
        this.mapType = mapType;
    }

    @Override
    public boolean applies(Optional<PropertyPath> path, LocalTypeDescriptor descriptor) {
        return path.isPresent() && descriptor.typeDescriptor.getRawType().equals(mapType);
    }

    @Override
    public ValueType describe(Optional<PropertyPath> path, TypeDescriptor mapType, DescribeContext context) {
        TypeDescriptor keyType = mapType.resolveGenericParameter(Map.class, 0);
        TypeDescriptor valueType = mapType.resolveGenericParameter(Map.class, 1);

        context.describeComponent(path.get().any(), mapType, valueType);

        ValueType keyValueType = context.describeComponent(null, mapType, keyType);
        return newMapType((ScalarType) keyValueType);
    }

    protected ValueType newMapType(ScalarType keyType) {
        return new MapType(keyType);
    }

}
