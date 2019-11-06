/*
 * Copyright 2016 The CHOReVOLUTION project.
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

public enum EventType {

    SENDING_REQUEST,
    RECEIVING_REQUEST,
    FORWARDING_REQUEST,
    REPLY_RESPONSE,
    RECEIVING_RESPONSE,
    FORWARDING_RESPONSE,
    SEND_UPDATE_BEFORE,
    SEND_UPDATE_AFTER,
    SEND_NOTIFY_BEFORE,
    SEND_NOTIFY_AFTER,
    WAIT_UPDATE_STATE_BEFORE,
    WAIT_UPDATE_STATE_AFTER,
    WAIT_ALL_NOTIFY_BEFORE,
    WAIT_ALL_NOTIFY_AFTER
}
