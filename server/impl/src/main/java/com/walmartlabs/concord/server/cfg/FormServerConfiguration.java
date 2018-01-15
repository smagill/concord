package com.walmartlabs.concord.server.cfg;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 Wal-Mart Store, Inc.
 * -----
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =====
 */

import com.walmartlabs.concord.common.IOUtils;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Named
@Singleton
public class FormServerConfiguration {

    public static final String FORM_SERVER_DIR_KEY = "FORM_SERVER_DIR";

    private final Path baseDir;

    public FormServerConfiguration() throws IOException {
        String s = System.getenv(FORM_SERVER_DIR_KEY);
        this.baseDir = s != null ? Paths.get(s) : IOUtils.createTempDir("formserv");
    }

    public Path getBaseDir() {
        return baseDir;
    }
}
