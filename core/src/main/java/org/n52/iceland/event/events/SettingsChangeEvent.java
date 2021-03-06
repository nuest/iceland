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

import org.n52.iceland.config.SettingDefinition;
import org.n52.iceland.config.SettingValue;
import org.n52.iceland.event.ServiceEvent;

/**
 * This event is fired if the {@link SettingValue} of a
 * {@link SettingDefinition} has been changed or a {@link SettingDefinition} was
 * deleted.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class SettingsChangeEvent implements ServiceEvent {

    private SettingDefinition<?, ?> setting;

    private SettingValue<?> oldValue;

    private SettingValue<?> newValue;

    public SettingsChangeEvent(SettingDefinition<?, ?> setting, SettingValue<?> oldValue, SettingValue<?> newValue) {
        this.setting = setting;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public SettingDefinition<?, ?> getSetting() {
        return setting;
    }

    public SettingValue<?> getOldValue() {
        return oldValue;
    }

    public SettingValue<?> getNewValue() {
        return newValue;
    }

    public boolean hasNewValue() {
        return getNewValue() != null;
    }

    public boolean hasOldValue() {
        return getOldValue() != null;
    }

    @Override
    public String toString() {
        return String.format("SettingsChangeEvent[setting=%s, oldValue=%s, newValue=%s", getSetting(), getOldValue(),
                getNewValue());
    }
}
