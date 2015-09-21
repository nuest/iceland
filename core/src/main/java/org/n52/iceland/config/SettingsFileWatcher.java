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

import com.google.common.base.MoreObjects;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.exception.ConfigurationError;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.lifecycle.Destroyable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://andreinc.net/2013/12/06/java-7-nio-2-tutorial-writing-a-simple-filefolder-monitor-using-the-watch-service-api/
 * https://docs.oracle.com/javase/tutorial/essential/io/notification.html
 *
 *
 * Load bean:
 * <pre><bean id="fileWatcher" class="org.n52.iceland.config.SettingsFileWatcher" /></pre>
 *
 * Configure setting:
 * <pre><bean class="org.n52.iceland.config.settings.BooleanSettingDefinition">
 *       <property name="key" value="filewatcher.enabled" />
 *       <property name="title" value="Enable configuration file watcher" />
 *       <property name="description" value="If enabled, the configuration file will be reloaded when changed on the disk." />
 *       <property name="order" value="1.0" />
 *       <property name="group" ref="watcherSettingDefinitionGroup" />
 *       <property name="defaultValue" value="false" />
 * </bean></pre>
 *
 * @author Daniel Nüst <d.nuest@52north.org>
 */
@Configurable
public class SettingsFileWatcher implements Constructable, Destroyable {

    private static final String FILE_WATCHER_ENABLED = "filewatcher.enabled";

    private static final Logger log = LoggerFactory
            .getLogger(SettingsFileWatcher.class);

    private SettingsService settingsService;

    private FileSettingsConfiguration fileConfiguration;

    private boolean enabled;

    private WatchService watchService;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public SettingsFileWatcher() {
        log.debug("NEW {}", this);
    }

    @Inject
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Inject
    public void setFileConfiguration(FileSettingsConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    @Setting(FILE_WATCHER_ENABLED)
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void init() {
        if (!enabled) {
            log.info("File watcher loaded but not enabled: {}", this.toString());
            return;
        }

        Path configFilePath = this.fileConfiguration.getPath();

        log.info("Adding watch on '{}'.", configFilePath);

        try {
            this.watchService = FileSystems.getDefault().newWatchService();
            configFilePath.getParent().register(this.watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            throw new ConfigurationError("Error creating and registering watch service", e);
        }

        executor.submit(new Watcher(this.watchService, this.settingsService, this.fileConfiguration));
    }

    @Override
    public void destroy() {
        log.debug("Destroying... {}", this);
        if (this.watchService != null) {
            try {
                this.watchService.close();
            } catch (IOException e) {
                log.error("Error closing watch service on file {}", this.fileConfiguration.getPath(), e);
            }
        }

        executor.shutdown();

        log.debug("Destroyed {}", this);
    }

    private static class Watcher implements Runnable {

        private WatchService service;

        private final SettingsService settings;

        private final FileSettingsConfiguration fileConfiguration;

        private Watcher(WatchService watchService, SettingsService settingsService,
                FileSettingsConfiguration fileConfiguration) {
            this.service = watchService;
            this.settings = settingsService;
            this.fileConfiguration = fileConfiguration;
        }

        @Override
        public void run() {
            log.debug("Starting watcher thread.");

            for (;;) {

                // wait for key to be signaled
                WatchKey key;
                try {
                    key = service.take(); // instant
//                    key = service.poll(intervalSeconds, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    log.error("Could not take key from watcher", e);
                    return;
                }

                key.pollEvents().stream().forEach((event) -> {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind != OVERFLOW) {
                        log.trace("Received {} event for file: {}",
                                event.kind(), event.context());

                        // check if it is an event on the config file
                        Path eventPath = (Path) event.context();
                        if (this.fileConfiguration.getPath().endsWith(eventPath)) {
                            log.debug("File {} changed, updating settings...", eventPath);

                            fileConfiguration.refresh();
                            settings.reconfigure();
                        }
                    }
                });

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

            log.debug("Stopping watcher thread.");
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("enabled", this.enabled)
                .add("path", this.fileConfiguration)
                .toString();
    }

}