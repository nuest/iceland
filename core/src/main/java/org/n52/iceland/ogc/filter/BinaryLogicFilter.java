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
package org.n52.iceland.ogc.filter;

import com.google.common.base.MoreObjects;
import java.util.Set;

import org.n52.iceland.ogc.filter.FilterConstants.BinaryLogicOperator;

import com.google.common.collect.Sets;
import java.util.Arrays;

/**
 * OGC Filter class for binary logic filters "AND" and "OR"
 *
 * @since 1.0.0
 *
 */
public class BinaryLogicFilter extends Filter<BinaryLogicOperator> implements LogicFilter {

    private BinaryLogicOperator operator;

    private final Set<Filter<?>> filterPredicates = Sets.newLinkedHashSet(); // keep insertion order

    /**
     * constructor
     *
     * @param operator
     *            Binary logic filter operator
     */
    public BinaryLogicFilter(BinaryLogicOperator operator) {
        this.operator = operator;
    }

    /**
     * constructor
     *
     * @param operator
     *            Binary logic filter operator
     * @param filterOne
     *            First filter
     * @param filterTwo
     *            Second filter
     */
    public BinaryLogicFilter(BinaryLogicOperator operator, Filter<?> filterOne, Filter<?> filterTwo) {
        this.operator = operator;
        filterPredicates.add(filterOne);
        filterPredicates.add(filterTwo);
    }

    @Override
    public BinaryLogicOperator getOperator() {
        return operator;
    }

    @Override
    public Filter<BinaryLogicOperator> setOperator(BinaryLogicOperator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * @return the filterPredicates
     */
    public Set<Filter<?>> getFilterPredicates() {
        return filterPredicates;
    }

    /**
     * @param filterPredicate
     *            the filterPredicate to add
     */
    public BinaryLogicFilter addFilterPredicates(Filter<?> filterPredicate) {
        this.filterPredicates.add(filterPredicate);
        return this;
    }

    /**
     * @param filterPredicates
     *            the filterPredicates to add
     */
    public BinaryLogicFilter addFilterPredicates(Set<Filter<?>> filterPredicates) {
        this.filterPredicates.addAll(filterPredicates);
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("children", this.filterPredicates.size())
                .add("predicates", Arrays.toString(this.filterPredicates.toArray()))
                .toString();
    }



}
