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

import java.util.List;
import java.util.SortedSet;

import org.javersion.object.types.SetType.Key;
import org.javersion.object.types.SortedSetType;
import org.javersion.object.types.ValueType;

public class SortedSetMapping extends SetTypeMapping {

    public SortedSetMapping() {
        super(SortedSet.class);
    }

    @Override
    protected ValueType newSetType(List<Key> valueTypes) {
        return new SortedSetType(valueTypes);
    }
}
