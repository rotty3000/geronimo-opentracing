/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.geronimo.microprofile.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.inject.Vetoed;

@Vetoed
class DefaultOpenTracingConfig implements GeronimoOpenTracingConfig {
    private final Map<String, String> configuration = new HashMap<>();

    DefaultOpenTracingConfig() {
        System.getProperties().stringPropertyNames()
              .forEach(k -> configuration.put(k, System.getProperty(k)));
        try (final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/geronimo/microprofile/opentracing.properties")) {
            if (stream != null) {
                final Properties properties = new Properties();
                properties.load(stream);
                properties.stringPropertyNames().forEach(k -> configuration.put(k, properties.getProperty(k)));
            }
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String read(final String value, final String def) {
        return configuration.getOrDefault(value, def);
    }
}
