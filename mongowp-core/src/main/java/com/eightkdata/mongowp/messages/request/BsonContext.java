/*
 * MongoWP
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.messages.request;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * This class represents a callback to clean up all BSON data that could be offheap.
 *
 * Some implementations could decide to not store documents (or part of them) on the heap. In that
 * case, they have to supply an instance of this class.
 *
 * All documents referenced on a request must be <em>alive</em> at least until this callback is
 * called.
 */
@NotThreadSafe
public interface BsonContext extends AutoCloseable {

  /**
   * @return true if documents associated with this context are still valid.
   */
  boolean isValid();

}
