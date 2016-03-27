/*
 * Copyright 2015 the original author or authors.
 *
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
 */

package org.gradle.tooling.internal.consumer.connection;

import org.gradle.api.Action;
import org.gradle.tooling.internal.adapter.SourceObjectMapping;
import org.gradle.tooling.internal.consumer.converters.CompositeMappingAction;
import org.gradle.tooling.internal.consumer.converters.IdeaProjectCompatibilityMapper;
import org.gradle.tooling.internal.consumer.converters.TaskPropertyHandlerFactory;
import org.gradle.tooling.internal.consumer.versioning.VersionDetails;

public class HasCompatibilityMapperAction {

    private final Action<SourceObjectMapping> taskPropertyHandlerMapper;
    private final Action<SourceObjectMapping> ideaProjectCompatibilityMapper;
    private final Action<SourceObjectMapping> mapper;

    public HasCompatibilityMapperAction(VersionDetails versionDetails) {
        taskPropertyHandlerMapper = new TaskPropertyHandlerFactory().forVersion(versionDetails);
        ideaProjectCompatibilityMapper = new IdeaProjectCompatibilityMapper(versionDetails);
        this.mapper = CompositeMappingAction.builder()
            .add(taskPropertyHandlerMapper)
            .add(ideaProjectCompatibilityMapper)
            .build();
    }

    public Action<SourceObjectMapping> getCompatibilityMapperAction() {
        return mapper;
    }

    public Action<SourceObjectMapping> getCompatibilityMapperAction(Action<SourceObjectMapping> requestScopedMapping) {
        return CompositeMappingAction.builder()
            .add(taskPropertyHandlerMapper)
            .add(ideaProjectCompatibilityMapper)
            .add(requestScopedMapping)
            .build();
    }
}
