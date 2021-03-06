/*

   Derby - Class com.pivotal.gemfirexd.internal.iapi.sql.dictionary.ColumnDescriptorList

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

/*
 * Changes for GemFireXD distributed data platform (some marked by "GemStone changes")
 *
 * Portions Copyright (c) 2010-2015 Pivotal Software, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */

package com.pivotal.gemfirexd.internal.iapi.sql.dictionary;



import com.pivotal.gemfirexd.internal.catalog.UUID;
import com.pivotal.gemfirexd.internal.iapi.error.StandardException;
import com.pivotal.gemfirexd.internal.iapi.services.sanity.SanityManager;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This represents a list of column descriptors. 
 */

public class ColumnDescriptorList extends ArrayList
{
	/**
	 * Add the column.  Currently, the table id is ignored.
	 *
	 * @param tableID the table id (ignored)
	 * @param column the column to add
	 */	
	public void add(UUID tableID, ColumnDescriptor column)
	{
		/*
		** RESOLVE: The interface includes tableID because presumably
		** the primary key for the columns table will be tableID +
		** columnID (or possibly tableID + column name - both column
		** name and ID must be unique within a table).  However, the
		** ColumnDescriptor contains a reference to a tableID, so it
		** seems like we don't need the parameter here.  I am going
		** to leave it here just in case we decide we need it later.
		*/
		add(column);
	}

	/**
	 * Get the column descriptor
	 *
	 * @param tableID the table id (ignored)
	 * @param columnName the column get
	 *
	 * @return the column descriptor if found
	 */	
	public ColumnDescriptor getColumnDescriptor(UUID tableID,
							String columnName)
	{
		ColumnDescriptor	returnValue = null;

		for (Iterator iterator = iterator(); iterator.hasNext(); )
		{
			ColumnDescriptor columnDescriptor = (ColumnDescriptor) iterator.next();

			if ( columnName.equals( columnDescriptor.getColumnName() ) &&
			    tableID.equals( columnDescriptor.getReferencingUUID() ) )
			{
				returnValue = columnDescriptor;
				break;
			}
		}

		return returnValue;
	}

	/**
	 * Get the column descriptor
	 *
	 * @param tableID the table id (ignored)
	 * @param columnID the column id
	 *
	 * @return the column descriptor if found
	 */	
	public ColumnDescriptor getColumnDescriptor(UUID tableID, int columnID)
	{
		ColumnDescriptor	returnValue = null;

		for (Iterator iterator = iterator(); iterator.hasNext(); )
		{
			ColumnDescriptor columnDescriptor = (ColumnDescriptor) iterator.next();
			if ( ( columnID == columnDescriptor.getPosition() ) &&
				tableID.equals( columnDescriptor.getReferencingUUID() ) )
			{
				returnValue = columnDescriptor;
				break;
			}
		}

		return returnValue;
	}

	/**
	 * Return the nth (0-based) element in the list.
	 *
	 * @param n	Which element to return.
	 *
	 * @return The nth element in the list.
	 */
	public ColumnDescriptor elementAt(int n)
	{
		return (ColumnDescriptor) get(n);
	}

	/**
	 * Get an array of strings for all the columns
	 * in this CDL.
	 *
	 * @return the array of strings
	 */
	public String[] getColumnNames()
	{
		String strings[] = new String[size()];

		int size = size();

		for (int index = 0; index < size; index++)
		{
			ColumnDescriptor columnDescriptor = elementAt(index);
			strings[index] = columnDescriptor.getColumnName();
		}
		return strings;
	}

// GemStone changes BEGIN
  public boolean hasAutoIncrementAlways() {
    int size = size();

    for (int index = 0; index < size; index++)
    {
            ColumnDescriptor columnDescriptor = elementAt(index);
            if (columnDescriptor.isAutoincAlways()) {
              return true;
            }
    }
    return false;
  }
//GemStone changes END
}
