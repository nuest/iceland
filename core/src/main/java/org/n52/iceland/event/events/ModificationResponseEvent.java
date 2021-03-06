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
package org.n52.iceland.event.events;

import org.n52.iceland.event.ServiceEvent;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.request.operator.RequestOperator;
import org.n52.iceland.response.AbstractServiceResponse;

/**
 * Abstract event that should be fired if a successful request changed the
 * contents of this service, e.g in the implemented {@link RequestOperator}
 *
 * @param <I>
 *            the request type
 * @param <O>
 *            the response type
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public abstract class ModificationResponseEvent<I extends AbstractServiceRequest<?>, O extends AbstractServiceResponse>
        extends ResponseEvent
        implements ServiceEvent {
    private final I request;

    public ModificationResponseEvent(I request, O response) {
        super(response);
        this.request = request;
    }

    public I getRequest() {
        return request;
    }

    @SuppressWarnings("unchecked")
    public O getResponse() {
        return (O) super.getResponse();
    }

    @Override
    public String toString() {
        return String.format("%s[request=%s, response=%s]", getClass()
                             .getSimpleName(), getRequest(), getResponse());
    }
}
