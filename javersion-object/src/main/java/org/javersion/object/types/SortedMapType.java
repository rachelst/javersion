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

import java.util.Map;
import java.util.TreeMap;

public class SortedMapType extends MapType {

    public SortedMapType(ScalarType keyType) {
        super(keyType);
    }

    @Override
    protected Map<Object, Object> newMap(int size) {
        return new TreeMap<>();
    }
}
