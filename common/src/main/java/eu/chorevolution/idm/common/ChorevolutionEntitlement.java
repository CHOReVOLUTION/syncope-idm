/*
 * Copyright 2015 The CHOReVOLUTION project
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
package eu.chorevolution.idm.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public final class ChorevolutionEntitlement {

    public static final String CHOREOGRAPHY_LIST = "CHOREOGRAPHY_LIST";

    public static final String CHOREOGRAPHY_READ = "CHOREOGRAPHY_READ";

    public static final String CHOREOGRAPHY_CREATE = "CHOREOGRAPHY_CREATE";

    public static final String CHOREOGRAPHY_UPDATE = "CHOREOGRAPHY_UPDATE";

    public static final String CHOREOGRAPHY_DELETE = "CHOREOGRAPHY_DELETE";

    public static final String CHOREOGRAPHY_ENACT = "CHOREOGRAPHY_ENACT";

    public static final String NOTIFY_COMPLETION = "NOTIFY_COMPLETION";

    public static final String ON_CHOREOGRAPHY = "ON_CHOREOGRAPHY";

    public static final String ON_CHOREOGRAPHY_SERVICE = "ON_CHOREOGRAPHY_SERVICE";

    private static final Set<String> VALUES;

    static {
        Set<String> values = new TreeSet<>();
        for (Field field : ChorevolutionEntitlement.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && String.class.equals(field.getType())) {
                values.add(field.getName());
            }
        }
        VALUES = Collections.unmodifiableSet(values);
    }

    public static Set<String> values() {
        return VALUES;
    }

    private ChorevolutionEntitlement() {
        // private constructor for static utility class
    }
}
