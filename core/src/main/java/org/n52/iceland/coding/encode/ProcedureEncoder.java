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

import java.util.Set;
import org.n52.iceland.coding.ProcedureCoder;

/**
 * @since 1.0.0
 *
 * @param <S>
 * @param <T>
 */
@Deprecated // SOS-specific
public interface ProcedureEncoder<S, T> extends Encoder<S, T>, ProcedureCoder {

    /**
     * Get the supported procedure description formats for this
     * {@linkplain ProcedureEncoder} and the specified service and version.
     *
     * @param service
     *            the service
     * @param version
     *            the version
     *
     * @return the procedure description formats
     */
    @Override
    Set<String> getSupportedProcedureDescriptionFormats(String service, String version);

}
