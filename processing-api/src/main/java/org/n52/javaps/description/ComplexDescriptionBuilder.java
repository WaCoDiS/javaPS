/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.description;

import java.util.Arrays;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <T>
 * @param <B>
 */
public interface ComplexDescriptionBuilder<T extends ComplexDescription, B extends ComplexDescriptionBuilder<T, B>> {

    B withDefaultFormat(Format format);

    B withSupportedFormat(Format format);

    @SuppressWarnings("unchecked")
    default B withSupportedFormat(Iterable<Format> formats){
        if (formats != null) {
            for (Format format : formats) {
                withSupportedFormat(format);
            }
        }
        return (B) this;
    }

    default B withSupportedFormat(Format... formats){
        return withSupportedFormat(Arrays.asList(formats));
    }

}