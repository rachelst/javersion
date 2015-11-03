/*
 * Copyright 2015 Samppa Saarela
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
package org.javersion.store.jdbc;

import org.javersion.core.Revision;
import org.javersion.core.VersionType;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.EnumPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;

public class JVersion<Id> extends com.mysema.query.sql.RelationalPathBase<JVersion>  {

    public final Path<Id> docId;

    public final StringPath branch = createString("branch");

    public final SimplePath<Revision> revision = createSimple("revision", org.javersion.core.Revision.class);

    public final EnumPath<VersionType> type = createEnum("type", org.javersion.core.VersionType.class);

    public final NumberPath<Long> ordinal = createNumber("ordinal", Long.class);

    public final NumberPath<Long> localOrdinal = createNumber("localOrdinal", Long.class);

    public JVersion(RelationalPathBase<?> table, Path<Id> docId) {
        super(JVersion.class, table.getMetadata(), table.getSchemaName(), table.getTableName());
        this.docId = docId;
        table.getColumns().forEach(path -> addMetadata(path, table.getMetadata(path)));
    }

}
