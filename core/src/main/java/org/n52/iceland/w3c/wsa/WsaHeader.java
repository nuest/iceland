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
package org.n52.iceland.w3c.wsa;

import org.n52.iceland.util.StringHelper;
import org.n52.iceland.w3c.soap.SoapHeader;

/**
 * Class represents a W3C WS-Addressing header
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class WsaHeader implements SoapHeader {

    private String value;

    /**
     * Constructor
     *
     * @param value
     *            Value to set
     */
    public WsaHeader(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return if the value is set
     */
    public boolean isSetValue() {
        return StringHelper.isNotEmpty(getValue());
    }

    @Override
    public String getNamespace() {
        return WsaConstants.NS_WSA;
    }

}
