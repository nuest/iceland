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
package org.n52.iceland.config;

import java.io.File;

import org.n52.iceland.config.SettingType;
import org.n52.iceland.config.SettingValue;

/**
 * @since 1.0.0
 *
 */
public class FileSettingValueForTesting implements SettingValue<File> {

    private String key;

    private File value;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public File getValue() {
        return value;
    }

    @Override
    public SettingValue<File> setKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public SettingValue<File> setValue(File value) {
        this.value = value;
        return this;
    }

    @Override
    public SettingType getType() {
        // TODO Auto-generated method stub
        return null;
    }

}
