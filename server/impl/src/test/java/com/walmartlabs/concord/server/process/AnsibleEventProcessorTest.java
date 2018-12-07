package com.walmartlabs.concord.server.process;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

import com.walmartlabs.concord.server.AbstractDaoTest;
import com.walmartlabs.concord.server.cfg.AnsibleEventsConfiguration;
import com.walmartlabs.concord.server.process.event.AnsibleEventProcessor;
import com.walmartlabs.concord.server.process.event.AnsibleEventProcessor.AnsibleEventDao;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class AnsibleEventProcessorTest extends AbstractDaoTest {

    private AnsibleEventProcessor processor;

    @Before
    public void setUp() {
        AnsibleEventsConfiguration cfg = new AnsibleEventsConfiguration(1, 1000);
        AnsibleEventDao ansibleEventDao = new AnsibleEventDao(getConfiguration());
        processor = new AnsibleEventProcessor(cfg, ansibleEventDao);
    }

    @Test
    public void a() {
        for (int i = 0; i < 7; i++) {
            processor.performTask();
        }
    }
}
