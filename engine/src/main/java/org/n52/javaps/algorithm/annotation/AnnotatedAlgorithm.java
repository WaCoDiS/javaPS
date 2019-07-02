/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.algorithm.annotation;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.n52.javaps.description.TypedProcessDescription;
import org.n52.javaps.engine.ProcessExecutionContext;
import org.n52.javaps.algorithm.AbstractAlgorithm;
import org.n52.javaps.algorithm.ExecutionException;
import org.n52.javaps.description.TypedGroupInputDescription;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.GroupInputData;
import org.n52.javaps.io.InputHandlerRepository;
import org.n52.javaps.io.OutputHandlerRepository;
import org.n52.javaps.io.literal.LiteralTypeRepository;

public class AnnotatedAlgorithm extends AbstractAlgorithm {
    private static final Map<Class<?>, AnnotatedAlgorithmMetadata> CACHE = new ConcurrentHashMap<>();

    private final AnnotatedAlgorithmMetadata metadata;

    private final Object algorithmInstance;

    @Inject
    public AnnotatedAlgorithm(InputHandlerRepository parserRepository, OutputHandlerRepository generatorRepository,
            LiteralTypeRepository literalTypeRepository) {
        this(parserRepository, generatorRepository, literalTypeRepository, null);
    }

    public AnnotatedAlgorithm(InputHandlerRepository parserRepository, OutputHandlerRepository generatorRepository,
            LiteralTypeRepository literalDataManager, Object algorithmInstance) {
        this.algorithmInstance = algorithmInstance == null ? this : algorithmInstance;
        this.metadata = CACHE.computeIfAbsent(this.algorithmInstance.getClass(), c -> new AnnotatedAlgorithmMetadata(c,
                parserRepository, generatorRepository, literalDataManager));
    }

    @Override
    protected TypedProcessDescription createDescription() {
        return this.metadata.getDescription();
    }

    @Override
    public void execute(ProcessExecutionContext context) throws ExecutionException {
        /* a map that holds a reference to combined group bindings:
        * this map ensure that an algorithm receives annotated group bindings
        * in the correct order. The List<?> bindings for a group input
        * are represented as individiual flat fields in the algorithm instance. Therefore
        * two inputs of the same group receive inputs in different fields, but
        * with the same count and in the correct order
        */
        Map<String, List<Data<?>>> combinedBindings = new HashMap<>();

        // helper map to store the binding for later reflection calls
        Map<String, AbstractInputBinding> inputIdentifierToBinding = new HashMap<>();

        // go through all inputs and check if it belongs to a group
        // if so, store it in the helper map
        this.metadata.getInputBindings().forEach((id, binding) -> {
            String groupName = binding.getDescription().getGroup();
            if (!(binding.getDescription() instanceof TypedGroupInputDescription) &&
                    (groupName != null && !groupName.isEmpty())) {
                inputIdentifierToBinding.put(binding.getDescription().getId().getValue(), binding);
            }
        });

        // go through all bindings and check if it is an actual instance
        // of a group input (GroupInput annotation)
        this.metadata.getInputBindings().forEach((id, binding) -> {
            String groupName = binding.getDescription().getGroup();

            /* check if we have group input wrapper */
            if (binding.getDescription().isGroup() && binding.getDescription() instanceof TypedGroupInputDescription) {
                // the GroupInput inputs hold the actual child inputs
                List<Data<?>> theGroupInputs = context.getInputs().get(id);
                theGroupInputs.forEach(gi -> {
                    // currently, only GroupInputData is supported
                    if (gi instanceof GroupInputData) {
                        GroupInputData gid = (GroupInputData) gi;

                        // go through all inputs and attach them to the combined bindings
                        // which are later used to set the input via reflections
                        gid.getPayload().forEach((code, data) -> {
                            if (!combinedBindings.containsKey(code.getValue())) {
                                combinedBindings.put(code.getValue(), Lists.newArrayList());
                            }
                            combinedBindings.get(code.getValue()).addAll(data);
                        });
                    }
                });

            }

            /* check if we have an input not belonging to any group */
            if (!binding.getDescription().isGroup() && (groupName == null || groupName.isEmpty())) {
                // just set the input via reflections --> the behavior as before groups were supported
                binding.set(this.algorithmInstance, context.getInputs().get(id));
            }
        });

        // iterate over the bindings and set the values via reflections
        inputIdentifierToBinding.forEach((value, binding) -> {
            binding.set(this.algorithmInstance, combinedBindings.get(value));
        });

        this.metadata.getExecuteBinding().execute(this.algorithmInstance);
        this.metadata.getOutputBindings().forEach((id,
                binding) -> context.getOutputs().put(id, binding.get(this.algorithmInstance)));
    }
}
