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
package org.n52.javaps.ogc.wps.description;

import java.util.Set;

import org.n52.javaps.io.data.IData;
import org.n52.javaps.ogc.ows.OwsCode;
import org.n52.javaps.ogc.ows.OwsKeyword;
import org.n52.javaps.ogc.ows.OwsLanguageString;
import org.n52.javaps.ogc.ows.OwsMetadata;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractProcessOutputDescription
        extends AbstractDataDescription
        implements ProcessOutputDescription {

    protected AbstractProcessOutputDescription(
            AbstractBuilder<?, ?> builder) {
        super(builder);
    }

    public AbstractProcessOutputDescription(OwsCode id,
                                            OwsLanguageString title,
                                            OwsLanguageString abstrakt,
                                            Set<OwsKeyword> keywords,
                                            Set<OwsMetadata> metadata,
                                            Class<? extends IData> bindingClass) {
        super(id, title, abstrakt, keywords, metadata, bindingClass);
    }

    protected static abstract class AbstractBuilder<T extends ProcessOutputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractDataDescription.AbstractBuilder<T, B>
            implements ProcessOutputDescription.Builder<T, B> {
    }

}
