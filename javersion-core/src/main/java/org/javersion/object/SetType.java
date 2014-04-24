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
package org.javersion.object;

import java.util.Set;

import org.javersion.path.PropertyPath;
import org.javersion.path.PropertyTree;
import org.javersion.util.Check;

import com.google.common.collect.Sets;

public class SetType implements ValueType {

    private final IdentifiableType identifiableType;
    
    public SetType(IdentifiableType identifiableType) {
        this.identifiableType = Check.notNull(identifiableType, "keyType");
    }
    
    @Override
    public Object instantiate(PropertyTree propertyTree, Object value, ReadContext context) throws Exception {
        Set<Object> set = Sets.newLinkedHashSetWithExpectedSize((Integer) value);
        for (PropertyTree elementPath : propertyTree.getChildren()) {
            set.add(context.getAndBindObject(elementPath));
        }
        return set;
    }

    @Override
    public void bind(PropertyTree propertyTree, Object object, ReadContext context) throws Exception {}

    @Override
    public void serialize(Object object, WriteContext context) {
        Set<?> set = (Set<?>) object;
        PropertyPath path = context.getCurrentPath();
        context.put(path, set.size());
        
        for (Object element : set) {
            String key = identifiableType.toString(element);
            context.serialize(path.index(key), element);
        }
    }

    @Override
    public Class<?> getTargetType() {
        return Set.class;
    }

}
