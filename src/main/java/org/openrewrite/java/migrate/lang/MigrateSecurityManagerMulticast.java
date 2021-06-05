/*
 * Copyright 2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.java.migrate.lang;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.search.UsesMethod;
import org.openrewrite.java.tree.J;

import java.util.Collections;

public class MigrateSecurityManagerMulticast extends Recipe {
    private static final MethodMatcher MULTICAST_METHOD = new MethodMatcher("java.lang.SecurityManager checkMulticast(java.net.InetAddress, byte)");

    @Override
    public String getDisplayName() {
        return "Migrates deprecated method java.lang.SecurityManager.checkMulticast.";
    }

    @Override
    public String getDescription() {
        return "Replaces the java.lang.SecurityManager deprecated method checkMulticast(InetAddress, byte) with checkMulticast(InetAddress).";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getSingleSourceApplicableTest() {
        return new UsesMethod<>(MULTICAST_METHOD);
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<ExecutionContext>() {
            @Override
            public J.MethodInvocation visitMethodInvocation(J.MethodInvocation method, ExecutionContext ctx) {
                if (MULTICAST_METHOD.matches(method)) {
                    return method.withArguments(Collections.singletonList(method.getArguments().get(0)));
                }
                return super.visitMethodInvocation(method, ctx);
            }
        };
    }

}
