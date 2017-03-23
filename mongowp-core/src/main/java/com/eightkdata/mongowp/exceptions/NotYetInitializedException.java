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

/**
 *
 */
public class NotYetInitializedException extends MongoException {

  private static final long serialVersionUID = 832426920912175586L;

  public NotYetInitializedException() {
    super(ErrorCode.NOT_YET_INITIALIZED);
  }

  public NotYetInitializedException(String customMessage) {
    super(customMessage, ErrorCode.NOT_YET_INITIALIZED);
  }

}
