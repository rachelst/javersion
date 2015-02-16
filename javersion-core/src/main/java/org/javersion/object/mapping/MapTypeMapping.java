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

import org.javersion.object.DescribeContext;
import org.javersion.object.LocalTypeDescriptor;
import org.javersion.object.types.IdentifiableType;
import org.javersion.object.types.MapType;
import org.javersion.object.types.ValueType;
import org.javersion.path.PropertyPath;
import org.javersion.path.PropertyPath.SubPath;
import org.javersion.reflect.TypeDescriptor;

public class MapTypeMapping implements TypeMapping {

    @Override
    public boolean applies(PropertyPath path, LocalTypeDescriptor localTypeDescriptor) {
        return localTypeDescriptor.typeDescriptor.getRawType().equals(Map.class);
    }

    @Override
    public ValueType describe(PropertyPath path, TypeDescriptor mapType, DescribeContext context) {
        SubPath valuePath = path.anyKey();
        TypeDescriptor keyType = mapType.resolveGenericParameter(Map.class, 0);
        TypeDescriptor valueType = mapType.resolveGenericParameter(Map.class, 1);

        IdentifiableType identifiableKeyType = (IdentifiableType)
                context.describeComponent(valuePath.property(MapType.KEY), mapType, keyType);

        context.describeComponent(valuePath, mapType, valueType);

        return new MapType(identifiableKeyType);
    }

}
