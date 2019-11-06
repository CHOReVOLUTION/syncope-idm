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
package eu.chorevolution.idm.common.types;

public enum ChoreographyAction {
    /**
     * User requests start to be accepted.
     * Only available after given choreography was subjected to {@link #STOP}..
     */
    START,
    /**
     * No more user requests are accepted.
     * Only available after given choreography was subjected to {@link #START}..
     */
    STOP,
    /**
     * Freezes all choreography resources.
     * Each node is put in a frozen state.
     * Only available after given choreography was subjected to {@link #START}..
     */
    FREEZE,
    /**
     * Unfreezes all choreography resources.
     * Only available after given choreography was subjected to {@link #FREEZE}..
     * Each node is back available again.
     */
    UNFREEZE,
    /**
     * Sets the minimum number of nodes running each service group of the given choreography.
     */
    RESIZE;

}
