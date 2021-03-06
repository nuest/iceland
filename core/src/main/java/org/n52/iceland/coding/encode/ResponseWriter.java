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
package org.n52.iceland.coding.encode;

import java.io.IOException;
import java.io.OutputStream;

import org.n52.iceland.component.Component;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.ResponseFormat;
import org.n52.iceland.util.http.MediaType;
import org.n52.iceland.util.http.MediaTypes;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author CarstenHollmann <c.hollmann@52north.org>
 *
 * @since 1.0.0
 */
public interface ResponseWriter<T> extends Component<ResponseWriterKey> {

    /**
     * Get the current contentType
     *
     * @return the contenType
     */
    MediaType getContentType();

    /**
     * Set the contentType
     *
     * @param contentType
     *            to set
     */
    void setContentType(MediaType contentType);

     /**
     * Check if contentType is set
     * @return <code>true</code>, if contentType is set
     */
    default boolean isSetContentType() {
        return getContentType() != null;
    }


    /**
     * Write object t to {@link OutputStream} out
     *
     * @param t
     *            Object to write
     * @param out
     *            {@link OutputStream} to be written to
     * @param responseProxy
     *            {@link ResponseProxy} giving access to header and content length setters
     * @throws IOException
     *             If an error occurs during writing
     */
    void write(T t, OutputStream out, ResponseProxy responseProxy) throws IOException, OwsExceptionReport;

    /**
     * Check if GZip is supported by this writer
     *
     * @param t
     *            Object to write
     * @return <code>true</code>, if GZip is supported
     */
    boolean supportsGZip(T t);

    default MediaType getEncodedContentType(ResponseFormat responseFormat) {
        MediaType defaultContentType = getContentType();

        if (responseFormat.isSetResponseFormat()) {
            try {
                String rf = responseFormat.getResponseFormat();
                MediaType mt = MediaType.parse(rf).withoutParameters();
                if (MediaTypes.COMPATIBLE_TYPES.containsEntry(mt, defaultContentType)) {
                    return defaultContentType;
                }
                return mt;
            } catch (IllegalArgumentException iae) {
                // ignore
            }

        }
        return defaultContentType;
    }
}
