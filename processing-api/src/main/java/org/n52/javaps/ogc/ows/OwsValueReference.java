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
package org.n52.javaps.ogc.ows;

import java.net.URI;
import java.util.Objects;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsValueReference implements OwsValueDescription {

    private final URI reference;
    private final String value;

    public OwsValueReference(URI reference, String value) {
        this.reference = Objects.requireNonNull(reference);
        this.value = Objects.requireNonNull(Strings.emptyToNull(value));
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public OwsValueReference asReference() {
        return this;
    }

    public URI getReference() {
        return this.reference;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.reference);
        hash = 89 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OwsValueReference other = (OwsValueReference) obj;
        return Objects.equals(this.value, other.value) &&
               Objects.equals(this.reference, other.reference);
    }



}
