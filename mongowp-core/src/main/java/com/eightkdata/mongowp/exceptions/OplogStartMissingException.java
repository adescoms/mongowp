/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;
import com.google.common.net.HostAndPort;

/**
 *
 */
public class OplogStartMissingException extends MongoException {

  private static final long serialVersionUID = 1L;

  private final HostAndPort syncSource;

  public OplogStartMissingException(HostAndPort syncSource) {
    super(ErrorCode.OPLOG_START_MISSING, syncSource);
    this.syncSource = syncSource;
  }

  public HostAndPort getSyncSource() {
    return syncSource;
  }

}
