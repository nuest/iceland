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
package org.n52.iceland.service;

import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.request.Request;

import com.google.common.base.Strings;

/**
 * @since 1.0.0
 *
 */
public abstract class AbstractServiceCommunicationObject implements Request {

    /** service parameter */
    private String service;
    private String version;

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service
     *            the service to set
     * @return this
     */
    public AbstractServiceCommunicationObject setService(String service) {
        this.service = service;
        return this;
    }

    /**
     * @param version
     *            the version to set
     * @return this
     */
    public AbstractServiceCommunicationObject setVersion(String version) {
        this.version = version;
        return this;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return <code>true</code>, if service is not null or empty
     */
    public boolean isSetService() {
        return !Strings.isNullOrEmpty(getService());
    }

    /**
     * @return <code>true</code>, if version is not null or empty
     */
    public boolean isSetVersion() {
        return !Strings.isNullOrEmpty(getVersion());
    }

    /**
     * Set service and version from another
     * {@link AbstractServiceCommunicationObject}
     *
     * @param object
     *            The {@link AbstractServiceCommunicationObject} to get values
     *            from
     * @return this.
     */
    public AbstractServiceCommunicationObject set(AbstractServiceCommunicationObject object) {
        setService(object.getService());
        setVersion(object.getVersion());
        return this;
    }

    /**
     * @return the operationName
     */
    public abstract String getOperationName();

    /**
     * @return The {@link OperationKey} of this
     *         {@link AbstractServiceCommunicationObject}
     */
    public OperationKey getOperationKey() {
        return new OperationKey(getService(), getVersion(), getOperationName());
    }

    @Override
    public String toString() {
        return String.format("%s[service=%s, version=%s]", getClass().getName(), getService(), getVersion());
    }
}
