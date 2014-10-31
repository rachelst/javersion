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

import static com.google.common.base.Strings.isNullOrEmpty;

import org.javersion.object.DescribeContext;
import org.javersion.object.LocalTypeDescriptor;
import org.javersion.object.Versionable;
import org.javersion.object.types.ValueType;
import org.javersion.path.PropertyPath;
import org.javersion.reflect.TypeDescriptor;

import com.google.common.base.Strings;
import static org.javersion.object.mapping.VersionableTypeMapping.getAlias;

public class VersionableReferenceTypeMapping implements TypeMapping {

    @Override
    public boolean applies(PropertyPath path, LocalTypeDescriptor localTypeDescriptor) {
        TypeDescriptor type = localTypeDescriptor.typeDescriptor;
        Versionable versionable = type.getAnnotation(Versionable.class);
        return versionable != null
                && versionable.reference()
                && ReferenceTypeMapping.isReferencePath(getAlias(versionable, type), path);
    }

    @Override
    public ValueType describe(PropertyPath path, TypeDescriptor type, DescribeContext context) {
        Versionable versionable = type.getAnnotation(Versionable.class);
        return ReferenceTypeMapping.describeReference(path, type, getAlias(versionable, type), context);
    }

}
