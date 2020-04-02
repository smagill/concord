package com.walmartlabs.concord.runtime.v2.runner.vm;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2019 Walmart Inc.
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

import com.walmartlabs.concord.runtime.v2.model.SwitchStep;
import com.walmartlabs.concord.runtime.v2.parser.KV;
import com.walmartlabs.concord.runtime.v2.runner.context.ContextFactory;
import com.walmartlabs.concord.runtime.v2.runner.el.ExpressionEvaluator;
import com.walmartlabs.concord.runtime.v2.runner.el.Interpolator;
import com.walmartlabs.concord.runtime.v2.sdk.Context;
import com.walmartlabs.concord.svm.Runtime;
import com.walmartlabs.concord.svm.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SwitchCommand extends StepCommand<SwitchStep> {

    private static final long serialVersionUID = 1L;

    private final List<KV<String, Command>> caseCommands;
    private final Command defaultCommand;

    public SwitchCommand(SwitchStep step, List<KV<String, Command>> caseCommands, Command defaultCommand) {
        super(step);
        this.caseCommands = caseCommands;
        this.defaultCommand = defaultCommand;
    }

    @Override
    public void eval(Runtime runtime, State state, ThreadId threadId) {
        Frame frame = state.peekFrame(threadId);
        frame.pop();

        ContextFactory contextFactory = runtime.getService(ContextFactory.class);
        ExpressionEvaluator ee = runtime.getService(ExpressionEvaluator.class);

        Context ctx = contextFactory.create(runtime, state, threadId, getStep(), UUID.randomUUID());

        SwitchStep step = getStep();
        String expr = step.getExpression();

        ThreadLocalContext.set(ctx);
        try {
            String switchResult = ee.eval(ctx, expr, String.class);
            boolean caseFound = false;
            for (KV<String, Command> kv : caseCommands) {
                String caseLabel = interpolate(ee, ctx, kv.getKey());
                if (Objects.equals(switchResult, caseLabel)) {
                    frame.push(kv.getValue());
                    caseFound = true;
                    break;
                }
            }

            if (!caseFound && defaultCommand != null) {
                frame.push(defaultCommand);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ThreadLocalContext.clear();
        }
    }

    private static String interpolate(ExpressionEvaluator ee, Context ctx, String caseLabel) {
        if (caseLabel == null) {
            return null;
        }

        if (Interpolator.hasExpression(caseLabel)) {
            return ee.eval(ctx, caseLabel, String.class);
        }

        return caseLabel;
    }
}