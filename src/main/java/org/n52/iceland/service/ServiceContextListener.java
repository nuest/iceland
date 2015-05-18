/**
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

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.n52.iceland.config.SettingsManager;
import org.n52.iceland.config.annotation.Instantiatable;
import org.n52.iceland.config.annotation.Instantiate;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.util.Cleanupable;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * @since 4.0.0
 *
 */
public class ServiceContextListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceContextListener.class);

    private static String path = null;
    private static final List<Runnable> hooks = new LinkedList<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        setPath(sce.getServletContext().getRealPath("/"));
        if (Configurator.getInstance() == null) {
            instantiateConfigurator(sce.getServletContext());
//            instantiateGeometryHandler();
        } else {
            LOG.error("Configurator already instantiated.");
        }
        initInstantiable();
    }

    private void initInstantiable() {
        Reflections reflections = new Reflections("org.n52");
        Set<Class<?>> annotated = 
                  reflections.getTypesAnnotatedWith(Instantiate.class);
        for (Class<?> clazz : annotated) {
            if (clazz.getAnnotatedSuperclass() instanceof Instantiatable) {
                ((Instantiatable)clazz.getAnnotatedSuperclass()).createInstance();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Set<String> providedJdbcDriver = null;
        if (Configurator.getInstance() != null) {
            providedJdbcDriver = Sets.newHashSet(
                    Configurator.getInstance().getProvidedJdbcDriver());
        }

        synchronized(hooks) {
            for (Runnable hook :hooks) {
                try {
                    hook.run();
                } catch (Throwable t) {
                    LOG.error("Error running shutdown hook", t);
                }
            }
        }

        cleanupConfigurator();
        cleanupSettingsManager();
//        cleanupGeometryHandler();
        if (providedJdbcDriver != null) {
            cleanupDrivers(providedJdbcDriver);
        }

    }

    protected void cleanupDrivers(Set<String> providedJdbcDriver) {
        if (ServiceConfiguration.getInstance().isDeregisterJdbcDriver()) {
            LOG.debug("Deregistering JDBC driver is enabled!");
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                if (!providedJdbcDriver.contains(driver.getClass().getName())) {
                    try {
                        DriverManager.deregisterDriver(driver);
                        LOG.info("Deregistering JDBC driver: {}", driver);
                    } catch (SQLException e) {
                        LOG.error("Error deregistering driver " + driver, e);
                    }
                } else {
                    LOG.debug("JDBC driver {} is marked to do not deregister", driver);
                }
            }
        } else {
            LOG.debug("Deregistering of JDBC driver(s) is disabled!");
        }
    }

    protected void cleanupSettingsManager() {
        try {
            if (SettingsManager.getInstance() != null) {
                SettingsManager.getInstance().cleanup();
            }
        } catch (Throwable ex) {
            LOG.error("Error while SettingsManager clean up", ex);
        }
    }

    protected void cleanupConfigurator() {
        try {
            if (Configurator.getInstance() != null) {
                Configurator.getInstance().cleanup();
            }
        } catch (Throwable ex) {
            LOG.error("Error while Configurator clean up", ex);
        }
    }
    
//    protected void cleanupGeometryHandler() {
//        try {
//            if (GeometryHandler.getInstance() != null) {
//                GeometryHandler.getInstance().cleanup();
//            }
//        } catch (Throwable ex) {
//            LOG.error("Error while GeometryHandler clean up", ex);
//        }
//    }
    
    protected void instantiateConfigurator(ServletContext context) {
        DatabaseSettingsHandler dbsh = DatabaseSettingsHandler.getInstance(context);
        if (dbsh.exists()) {
            LOG.debug("Initialising Configurator ({},{})", dbsh.getPath(), getPath());
            try {
                instantiateConfigurator(dbsh.getAll());
            } catch (ConfigurationException ex) {
                LOG.error("Error reading database properties", ex);
            }
        } else {
            LOG.warn("Can not initialize Configurator; config file is not present: {}", dbsh.getPath());
        }
    }

    protected void instantiateConfigurator(Properties p) {
        try {
            Configurator.createInstance(p, getPath());
        } catch (ConfigurationException ce) {
            String message = "Configurator initialization failed!";
            LOG.error(message, ce);
            throw new RuntimeException(message, ce);
        }
    }
    
//    /**
//     * Instantiate the {@link GeometryHandler} to avoid exceptions during the
//     * shutdown process.
//     */
//    protected void instantiateGeometryHandler() {
//        GeometryHandler.getInstance();
//    }

    public static String getPath() {
        return ServiceContextListener.path;
    }

    public static void setPath(String path) {
        ServiceContextListener.path = path;
    }

    public static boolean hasPath() {
        return ServiceContextListener.path != null;
    }

    public static void registerShutdownHook(Runnable runnable) {
        if (runnable != null) {
            synchronized(hooks) {
                hooks.add(runnable);
            }
        }
    }

    public static void registerShutdownHook(final Cleanupable cleanupable) {
        if (cleanupable != null) {
            registerShutdownHook(new Runnable() {
                @Override
                public void run() {
                    cleanupable.cleanup();
                }
            });
        }
    }
}
