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
package org.n52.iceland.request;

import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.response.TestResponse;

public class TestRequest extends AbstractServiceRequest<TestResponse> {

    @Override
    public TestResponse getResponse()
            throws OwsExceptionReport {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getOperationName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isSetRequestedLanguage() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getRequestedLanguage() {
        // TODO Auto-generated method stub
        return null;
    }

}
