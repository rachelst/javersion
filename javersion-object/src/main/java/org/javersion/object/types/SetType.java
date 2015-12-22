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
package org.javersion.object.types;

import static com.google.common.collect.Sets.newHashSetWithExpectedSize;

import java.util.Set;

import org.javersion.core.Persistent;
import org.javersion.object.ReadContext;
import org.javersion.object.WriteContext;
import org.javersion.path.PropertyPath;
import org.javersion.path.NodeId;
import org.javersion.path.PropertyTree;
import org.javersion.util.Check;

public class SetType implements ValueType {

    private final static Persistent.Object CONSTANT = Persistent.object();

    private final IdentifiableType identifiableType;

    public SetType(IdentifiableType identifiableType) {
        this.identifiableType = Check.notNull(identifiableType, "keyType");
    }

    @Override
    public Object instantiate(PropertyTree propertyTree, Object value, ReadContext context) throws Exception {
        prepareElements(propertyTree, context);
        return newSet(propertyTree.getChildren().size());
    }

    protected Set<Object> newSet(int size) {
        return newHashSetWithExpectedSize(size);
    }

    private void prepareElements(PropertyTree propertyTree, ReadContext context) {
        for (PropertyTree elementPath : propertyTree.getChildren()) {
            context.prepareObject(elementPath);
        }
    }

    @Override
    public void bind(PropertyTree propertyTree, Object object, ReadContext context) throws Exception {
        @SuppressWarnings("unchecked")
        Set<Object> set = (Set<Object>) object;
        for (PropertyTree elementPath : propertyTree.getChildren()) {
            set.add(context.getObject(elementPath));
        }
    }

    @Override
    public void serialize(PropertyPath path, Object object, WriteContext context) {
        Set<?> set = (Set<?>) object;
        context.put(path, CONSTANT);

        for (Object element : set) {
            NodeId key = identifiableType.toNodeId(element, context);
            context.serialize(path.node(key), element);
        }
    }

}
