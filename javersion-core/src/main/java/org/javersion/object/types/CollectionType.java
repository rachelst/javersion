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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.javersion.object.Persistent;
import org.javersion.object.ReadContext;
import org.javersion.object.WriteContext;
import org.javersion.path.PropertyPath;
import org.javersion.path.PropertyTree;

public class CollectionType implements ValueType {

    private final static Persistent.Array CONSTANT = Persistent.array();

    @Override
    public Object instantiate(PropertyTree propertyTree, Object value, ReadContext context) throws Exception {
        Collection<PropertyTree> children = propertyTree.getChildren();
        List<Object> list = new ArrayList<>(children.size());
        for (PropertyTree child : children) {
            list.add(context.getObject(child));
        }
        return list;
    }

    @Override
    public void bind(PropertyTree propertyTree, Object object, ReadContext context) throws Exception {}

    @Override
    public void serialize(PropertyPath path, Object object, WriteContext context) {
        @SuppressWarnings("rawtypes")
        Collection list = (Collection) object;
        context.put(path, CONSTANT);
        int i=0;
        for (Object element : list) {
            context.serialize(path.index(i++), element);
        }
    }

    @Override
    public boolean isReference() {
        return false;
    }

}
