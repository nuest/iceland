/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.iceland.ogc.ows;

import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.n52.iceland.util.CollectionHelper;

import com.google.common.collect.Sets;

/**
 * @since 1.0.0
 *
 */
public class OwsParameterValuePossibleValues implements OwsParameterValue {

    private SortedSet<String> values = Sets.newTreeSet();

    public OwsParameterValuePossibleValues(Collection<String> values) {
        this.values = values == null ? new TreeSet<>() : new TreeSet<>(values);
    }

    public OwsParameterValuePossibleValues(String value) {
        this(Collections.singleton(value));
    }

    public OwsParameterValuePossibleValues(Enum<?> value) {
        this(value.name());
    }

    public OwsParameterValuePossibleValues() {
        this(new TreeSet<String>());
    }

    public SortedSet<String> getValues() {
        return Collections.unmodifiableSortedSet(values);
    }

    public void addValue(String value) {
        addValues(Collections.singleton(value));
    }

    public void addValue(Enum<?> value) {
        addValues(Collections.singleton(value.name()));
    }

    public void addValues(Collection<String> values) {
        if (isSetValues()) {
            if (values != null) {
                this.values.addAll(values);
            }
        } else {
            this.values = values == null ? new TreeSet<>() : new TreeSet<>(values);
        }
    }

    public void setValues(Collection<String> values) {
        this.values.clear();
        if (values != null) {
            this.values.addAll(values);
        }
    }

    public boolean isSetValues() {
        return CollectionHelper.isNotEmpty(getValues());
    }

}
