/**
 * Licensed to Apereo under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. Apereo
 * licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at the
 * following location:
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apereo.portal.layout.dao;

import java.util.List;
import org.apereo.portal.layout.om.IStylesheetDescriptor;

/** Defines CRUD operations on {@link IStylesheetDescriptor} objects */
public interface IStylesheetDescriptorDao {
    /** Create and persist a new stylesheet descriptor, name and resource are required */
    IStylesheetDescriptor createStylesheetDescriptor(String name, String stylesheetResource);

    /** @return all of the persisted stylesheet descriptors */
    List<? extends IStylesheetDescriptor> getStylesheetDescriptors();

    /** Lookup a descriptor by id */
    IStylesheetDescriptor getStylesheetDescriptor(long id);

    /** Lookup a descriptor by name */
    IStylesheetDescriptor getStylesheetDescriptorByName(String name);

    /** Update a descriptor */
    void updateStylesheetDescriptor(IStylesheetDescriptor stylesheetDescriptor);

    /** Delete a descriptor */
    void deleteStylesheetDescriptor(IStylesheetDescriptor stylesheetDescriptor);
}
