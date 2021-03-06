/*
 * Copyright 2013 Samppa Saarela
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
package org.javersion.core;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Maps.filterEntries;
import static com.google.common.collect.Maps.filterKeys;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Maps.transformValues;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static org.javersion.core.VersionType.NORMAL;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

@Immutable
public class Version<K, V, M> {

    private static final Set<Revision> EMPTY_PARENTS = ImmutableSet.of();

    public static final String DEFAULT_BRANCH = "default";

    private final Function<V, VersionProperty<V>> toVersionProperties = new Function<V, VersionProperty<V>>() {

        @Override
        public VersionProperty<V> apply(V input) {
            return new VersionProperty<V>(revision, input);
        }

    };

    public final Revision revision;

    public final String branch;

    public final Set<Revision> parentRevisions;

    public final Map<K, V> changeset;

    public final VersionType type;

    public final M meta;

    protected Version(BuilderBase<K, V, M, ?> builder) {
        this.revision = builder.revision != null ? builder.revision : new Revision();
        this.branch = builder.branch != null ? builder.branch : DEFAULT_BRANCH;
        this.type = builder.type != null ? builder.type : NORMAL;
        this.parentRevisions = builder.parentRevisions != null ? copyOf(builder.parentRevisions) : EMPTY_PARENTS;
        this.changeset = builder.changeset != null ? unmodifiableMap(newLinkedHashMap(builder.changeset)) : ImmutableMap.of();
        this.meta = builder.meta;

        if (parentRevisions.contains(revision)) {
            throw new IllegalArgumentException("parentRevisions should not contain own revision");
        }
    }

    public Map<K, VersionProperty<V>> getVersionProperties() {
        return transformValues(changeset, toVersionProperties);
    }

    public int hashCode() {
        int hashCode = revision.hashCode();
        hashCode = 31*hashCode + branch.hashCode();
        hashCode = 31*hashCode + parentRevisions.hashCode();
        hashCode = 31*hashCode + changeset.hashCode();
        hashCode = 31*hashCode + type.hashCode();
        hashCode = 31*hashCode + (meta==null ? 0 : meta.hashCode());
        return hashCode;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Version) {
            Version<?, ?, ?> other = (Version<?, ?, ?>) obj;
            return this.revision.equals(other.revision)
                    && this.branch.equals(other.branch)
                    && this.parentRevisions.equals(other.parentRevisions)
                    && this.changeset.equals(other.changeset)
                    && this.type.equals(other.type)
                    && Objects.equals(this.meta, other.meta);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("revision", revision)
                .add("branch", branch)
                .add("type", type)
                .add("meta", meta)
                .add("parentRevisions", parentRevisions)
                .add("changeset", changeset)
                .toString();
    }

    public static class Builder<K, V, M> extends BuilderBase<K, V, M, Builder<K, V, M>> {
        public Builder() {
        }
        public Builder(Revision revision) {
            super(revision);
        }
    }

    @NotThreadSafe
    public abstract static class BuilderBase<K, V, M, This extends BuilderBase<K, V, M, This>> {

        public Revision revision;

        public VersionType type;

        public String branch;

        public Iterable<Revision> parentRevisions;

        public Map<K, V> changeset;

        public M meta;

        public BuilderBase() {
        }

        public BuilderBase(Revision revision) {
            this.revision = revision;
        }

        public This revision(Revision revision) {
            this.revision = revision;
            return self();
        }

        public This type(VersionType versionType) {
            this.type = versionType;
            return self();
        }

        public This branch(String branch) {
            this.branch = branch;
            return self();
        }

        public This parents(Revision... parentRevisions) {
            this.parentRevisions = (parentRevisions != null ? asList(parentRevisions) : null);
            return self();
        }

        public This parents(Iterable<Revision> parentRevisions) {
            this.parentRevisions = parentRevisions;
            return self();
        }

        public This changeset(Map<K, V> changeset) {
            this.changeset = changeset;
            return self();
        }

        public This meta(M meta) {
            this.meta = meta;
            return self();
        }

        public This changeset(Map<K, V> newProperties, VersionGraph<K, V, M> versionGraph) {
            return changeset(newProperties, versionGraph, p -> true);
        }

        public This changeset(Map<K, V> newProperties, VersionGraph<K, V, M> versionGraph, Predicate<K> filter) {
            if (parentRevisions != null) {
                Merge<K, V, M> merge = versionGraph.mergeRevisions(parentRevisions);
                if (newProperties == null || newProperties.isEmpty()) {
                    Map<K, V> oldProperties = filterKeys(merge.getProperties(), filter);
                    changeset(transformValues(oldProperties, v -> null));
                } else {
                    changeset(merge.diff(newProperties, filter));
                }
            } else if (newProperties != null) {
                changeset(filterEntries(newProperties, entry -> entry.getValue() != null && filter.apply(entry.getKey())));
            } else {
                changeset(null);
            }
            return self();
        }

        @SuppressWarnings("unchecked")
        protected This self() {
            return (This) this;
        }

        public Version<K, V, M> build() {
            return new Version<>(this);
        }

    }

}

