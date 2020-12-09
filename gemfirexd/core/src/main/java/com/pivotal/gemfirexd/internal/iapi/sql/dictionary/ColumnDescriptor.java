/*

   Derby - Class com.pivotal.gemfirexd.internal.iapi.sql.dictionary.ColumnDescriptor

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




//GemStone changes BEGIN
import com.pivotal.gemfirexd.internal.catalog.DefaultInfo;
import com.pivotal.gemfirexd.internal.catalog.UUID;
import com.pivotal.gemfirexd.internal.engine.jdbc.GemFireXDRuntimeException;
import com.pivotal.gemfirexd.internal.engine.store.RowFormatter;
import com.pivotal.gemfirexd.internal.iapi.error.StandardException;
import com.pivotal.gemfirexd.internal.iapi.reference.SQLState;
import com.pivotal.gemfirexd.internal.iapi.services.sanity.SanityManager;
import com.pivotal.gemfirexd.internal.iapi.sql.StatementType;
import com.pivotal.gemfirexd.internal.iapi.types.DataTypeDescriptor;
import com.pivotal.gemfirexd.internal.iapi.types.DataValueDescriptor;
import com.pivotal.gemfirexd.internal.impl.sql.compile.ColumnDefinitionNode;

/**
 * This class represents a column descriptor.
 *
 * public methods in this class are:
 * <ol>
 * <li>long getAutoincStart()</li>
 * <li>java.lang.String getColumnName()</li>
 * <li>DefaultDescriptor getDefaultDescriptor(DataDictionary dd)</li>
 * <li>DefaultInfo getDefaultInfo</li>
 * <li>UUID getDefaultUUID</li>
 * <li>DataValueDescriptor getDefaultValue</li>
 * <li>int getPosition()</li>
 * <li>UUID getReferencingUUID()</li>
 * <li>TableDescriptor getTableDescriptor</li>
 * <li>DTD getType()</li>
 * <li>hasNonNullDefault</li>
 * <li>isAutoincrement</li>
 * <li>setColumnName</li>
 * <li>setPosition</li>
 *</ol>
 */
// GemStone changes BEGIN
public final class ColumnDescriptor extends TupleDescriptor implements java.io.Serializable
{
	private static final long serialVersionUID = -7687054450976667166L;
// GemStone changes END

	// implementation
	private DefaultInfo			columnDefaultInfo;
	private transient TableDescriptor		table;
	private String			columnName;
	private int			columnPosition;
// GemStone changes BEGIN
	// made public to avoid function calls from RowFormatter; also
	// cache a few more flags here
	public final DataTypeDescriptor columnType;
	public DataValueDescriptor columnDefault;
	/** the serialized form of the default value */
	public byte[] columnDefaultBytes;
	/** true if this column can be null */
	public final boolean isNullable;
	/** the width for fixed width columns else -1 */
	public final int fixedWidth;
	/** true if this column is a BLOB/CLOB/XML type */
	public final boolean isLob;
	/* (original code)
	private DataTypeDescriptor	columnType;
	private DataValueDescriptor	columnDefault;
	*/
// GemStone changes END
	private UUID				uuid;
	private UUID				defaultUUID;
	private long				autoincStart;
	private long				autoincInc;
	private long				autoincValue;
	// GemStone changes BEGIN
	private boolean                         isGeneratedByDefault;
	// GemStone changes END
	//Following variable is used to see if the user is adding an autoincrement 
	//column, or if user is altering the existing autoincrement column to change 
	//the increment value or to change the start value. If none of the above,
	//then it will be set to -1
	long				autoinc_create_or_modify_Start_Increment = -1; 

	/**
	 * Constructor for a ColumnDescriptor when the column involved
	 * is an autoincrement column. The last parameter to this method
	 * indicates if an autoincrement column is getting added or if
	 * the autoincrement column is being modified to change the
	 * increment value or to change the start value
	 *
	 * @param columnName		The name of the column
	 * @param columnPosition	The ordinal position of the column
	 * @param columnType		A DataTypeDescriptor for the type of
	 *				the column
	 * @param columnDefault		A DataValueDescriptor representing the
	 *							default value of the column, if any
	 *							(null if no default)
	 * @param columnDefaultInfo		The default info for the column.
	 * @param table			A TableDescriptor for the table the
	 *						column is in
	 * @param defaultUUID			The UUID for the default, if any.
	 * @param autoincStart	Start value for an autoincrement column.
	 * @param autoincInc	Increment for autoincrement column
	 * @param userChangedWhat		Adding an autoincrement column OR
	 *						changing increment value or start value of
	 *						the autoincrement column.
	 */

	public ColumnDescriptor(String columnName, int columnPosition,
					 DataTypeDescriptor columnType, DataValueDescriptor columnDefault,
					 DefaultInfo columnDefaultInfo,
					 TableDescriptor table,
					 UUID defaultUUID, long autoincStart, long autoincInc, 
					 long userChangedWhat, boolean isGeneratedByDefault)
	{
		this(columnName, columnPosition, columnType, columnDefault,
				columnDefaultInfo, table, defaultUUID, autoincStart,
				autoincInc, isGeneratedByDefault);				
		autoinc_create_or_modify_Start_Increment = userChangedWhat;
	}

		/**
		 * Constructor for a ColumnDescriptor
		 *
		 * @param columnName		The name of the column
		 * @param columnPosition	The ordinal position of the column
		 * @param columnType		A DataTypeDescriptor for the type of
		 *				the column
		 * @param columnDefault		A DataValueDescriptor representing the
		 *							default value of the column, if any
		 *							(null if no default)
		 * @param columnDefaultInfo		The default info for the column.
		 * @param table			A TableDescriptor for the table the
		 *						column is in
		 * @param defaultUUID			The UUID for the default, if any.
		 * @param autoincStart	Start value for an autoincrement column.
		 * @param autoincInc	Increment for autoincrement column
		 */

		public ColumnDescriptor(String columnName, int columnPosition,
						 DataTypeDescriptor columnType, DataValueDescriptor columnDefault,
						 DefaultInfo columnDefaultInfo,
						 TableDescriptor table,
						 UUID defaultUUID, long autoincStart, long autoincInc, boolean isGeneratedByDefault)
		{
		this.columnName = columnName;
		this.columnPosition = columnPosition;
		this.columnType = columnType;
// GemStone changes BEGIN
		setColumnDefault(columnDefault);
		/* (original code)
		this.columnDefault = columnDefault;
		*/
// GemStone changes END
		this.columnDefaultInfo = columnDefaultInfo;
		this.defaultUUID = defaultUUID;
		if (table != null)
		{
			this.table = table;
			this.uuid = table.getUUID();
		}

		assertAutoinc(autoincInc != 0,
			      autoincInc,
			      columnDefaultInfo);

		this.autoincStart = autoincStart;
		this.autoincValue = autoincStart;
		this.autoincInc = autoincInc;
// GemStone changes BEGIN
		this.isGeneratedByDefault = isGeneratedByDefault;
		this.isNullable = columnType.isNullable();
		this.fixedWidth = RowFormatter.getFixedWidthInBytes(
		    columnType, this);
		this.isLob = RowFormatter.isLob(columnType);
// GemStone changes END
	}

	/**
	 * Constructor for a ColumnDescriptor.  Used when
	 * columnDescriptor doesn't know/care about a table
	 * descriptor.
	 *
	 * @param columnName		The name of the column
	 * @param columnPosition	The ordinal position of the column
	 * @param columnType		A DataTypeDescriptor for the type of
	 *				the column
	 * @param columnDefault		A DataValueDescriptor representing the
	 *							default value of the column, if any
	 *							(null if no default)
	 * @param columnDefaultInfo		The default info for the column.
	 * @param uuid			A uuid for the object that this column
	 *						is in.
	 * @param defaultUUID			The UUID for the default, if any.
	 * @param autoincStart	Start value for an autoincrement column.
	 * @param autoincInc	Increment for autoincrement column
	 * @param autoincValue	Current value of the autoincrement column
	 */
	public ColumnDescriptor(String columnName, int columnPosition,
		DataTypeDescriptor columnType, DataValueDescriptor columnDefault,
		DefaultInfo columnDefaultInfo,
		UUID uuid,
		UUID defaultUUID,
        long autoincStart, long autoincInc, long autoincValue, boolean isGeneratedByDefault)

	{
		this.columnName = columnName;
		this.columnPosition = columnPosition;
		this.columnType = columnType;
// GemStone changes BEGIN
		setColumnDefault(columnDefault);
		/* (original code)
		this.columnDefault = columnDefault;
		*/
// GemStone changes END
		this.columnDefaultInfo = columnDefaultInfo;
		this.uuid = uuid;
		this.defaultUUID = defaultUUID;

		assertAutoinc(autoincInc!=0,
			      autoincInc,
			      columnDefaultInfo);
		
		this.autoincStart = autoincStart;
		this.autoincValue = autoincValue;
		this.autoincInc = autoincInc;
// GemStone changes BEGIN
		this.isGeneratedByDefault = isGeneratedByDefault;
		this.isNullable = columnType.isNullable();
		this.fixedWidth = RowFormatter.getFixedWidthInBytes(
		    columnType, this);
		this.isLob = RowFormatter.isLob(columnType);
	}

	public void setColumnDefault(DataValueDescriptor columnDefault) {
	  if (columnDefault != null && !columnDefault.isNull()) {
	    try {
	      final int len = columnDefault.getLengthInBytes(columnType);
	      final byte[] bytes = new byte[len];
	      columnDefault.writeBytes(bytes, 0, columnType);
	      this.columnDefaultBytes = bytes;
	    } catch (StandardException se) {
	      throw GemFireXDRuntimeException.newRuntimeException(
	          "unexpected exception", se);
	    }
	    this.columnDefault = columnDefault;
	  }
	  else {
	    this.columnDefaultBytes = null;
	    this.columnDefault = null;
	  }
	}

	public ColumnDescriptor cloneObject() {
	  final ColumnDescriptor cd = new ColumnDescriptor(this.columnName,
	      this.columnPosition, this.columnType, this.columnDefault,
	      this.columnDefaultInfo, this.table, this.defaultUUID,
	      this.autoincStart, this.autoincInc,
	      this.autoinc_create_or_modify_Start_Increment,
	      this.isGeneratedByDefault);
	  // table can be null, in which case uuid should be assigned explicitly
	  cd.uuid = this.uuid;
	  return cd;
	}
// GemStone changes END

	/**
	 * Get the UUID of the object the column is a part of.
	 *
	 * @return	The UUID of the table the column is a part of.
	 */
	public UUID	getReferencingUUID()
	{
		return uuid;
	}

	/**
	 * Get the TableDescriptor of the column's table.
	 *
	 * @return	The TableDescriptor of the column's table.
	 */
 	public TableDescriptor	getTableDescriptor()
	{
		return table;
	}

	/**
	 * Get the name of the column.
	 *
	 * @return	A String containing the name of the column.
	 */
	public String	getColumnName()
	{
		return columnName;
	}

	/**
	 * Sets the column name in case of rename column.
	 *
	 * @param newColumnName	The new column name.
	 */
	public void	setColumnName(String newColumnName)
	{
		this.columnName = newColumnName;
	}

	/**
	 * Sets the table descriptor for the column.
	 *
	 * @param tableDescriptor	The table descriptor for this column
	 */
	public void	setTableDescriptor(TableDescriptor tableDescriptor)
	{
		this.table = tableDescriptor;
	}

	/**
	 * Get the ordinal position of the column (1 based)
	 *
	 * @return	The ordinal position of the column.
	 */
	public int	getPosition()
	{
		return columnPosition;
	}

	/**
	 * Get the TypeDescriptor of the column's datatype.
	 *
	 * @return	The TypeDescriptor of the column's datatype.
	 */
	public DataTypeDescriptor getType()
	{
		return columnType;
	}

	/**
	 * Return whether or not there is a non-null default on this column.
	 *
	 * @return Whether or not there is a non-null default on this column.
	 */
	public boolean hasNonNullDefault()
	{
		if (columnDefault != null && ! columnDefault.isNull())
		{
			return true;
		}

		return columnDefaultInfo != null;
	}

	/**
	 * Get the default value for the column. For columns with primitive
	 * types, the object returned will be of the corresponding object type.
	 * For example, for a float column, getDefaultValue() will return
	 * a Float.
	 *
	 * @return	An object with the value and type of the default value
	 *		for the column. Returns NULL if there is no default.
	 */
	public DataValueDescriptor getDefaultValue()
	{
		return columnDefault;
	}

	/**
	 * Get the DefaultInfo for this ColumnDescriptor.
	 *
	 * @return The DefaultInfo for this ColumnDescriptor.
	 */
	public DefaultInfo getDefaultInfo()
	{
		return columnDefaultInfo;
	}

	/**
	 * Get the UUID for the column default, if any.
	 *
	 * @return The UUID for the column default, if any.
	 */
	public UUID getDefaultUUID()
	{
		return defaultUUID;
	}

	/**
	 * Get a DefaultDescriptor for the default, if any, associated with this column.
	 *
	 * @param	dd	The DataDictionary.
	 *
	 * @return	A DefaultDescriptor if this column has a column default.
	 */
	public DefaultDescriptor getDefaultDescriptor(DataDictionary dd)
	{
		DefaultDescriptor defaultDescriptor = null;

		if (defaultUUID != null)
		{
			defaultDescriptor = new DefaultDescriptor(dd, defaultUUID, uuid, columnPosition);
		}

		return defaultDescriptor;
	}

	/**
	 * Is this column an autoincrement column?
	 *
	 * @return Whether or not this is an autoincrement column
	 */
	public boolean isAutoincrement()
	{
		return (autoincInc != 0);
	}
	public boolean updatableByCursor()
	{
		return false;
	}

	/**
	 * Is this column to have autoincremented value always ?
	 */
	public boolean isAutoincAlways(){
		return (columnDefaultInfo == null) && isAutoincrement();
	}
	
	// GemStone changes BEGIN
        public boolean isGeneratedByDefault(){
                return isGeneratedByDefault;
        }
        // GemStone changes END
	/**
	 * Get the start value of an autoincrement column
	 * 
	 * @return Get the start value of an autoincrement column
	 */
	public long getAutoincStart()
	{
		return autoincStart;
	}
	
	/**
	 * Get the Increment value given by the user for an autoincrement column
	 *
	 * @return the Increment value for an autoincrement column
	 */
	public long getAutoincInc()
	{
		return autoincInc;
	}

	/**
	 * Get the current value for an autoincrement column.
	 *
	 * One case in which this is used involves dropping a column
	 * from a table. When ALTER TABLE DROP COLUMN runs, it drops
	 * the column from SYSCOLUMNS, and then must adjust the
	 * column positions of the other subsequent columns in the table
	 * to account for the removal of the dropped columns. This
	 * involves deleting and re-adding the column descriptors to
	 * SYSCOLUMNS, but during that process we must be careful to
	 * preserve the current value of any autoincrement column.
	 *
	 * @return the current value for an autoincrement column
	 */
	public long getAutoincValue()
	{
		return autoincValue;
	}

	public long getAutoinc_create_or_modify_Start_Increment()
	{
		return autoinc_create_or_modify_Start_Increment;
	}
	public void setAutoinc_create_or_modify_Start_Increment(int c_or_m)
	{
		autoinc_create_or_modify_Start_Increment = c_or_m;
	}
	
	       public void setGeneratedByDefault(boolean value)
	        {
	                isGeneratedByDefault = value;
	        }

	/**
	 * Set the ordinal position of the column.
	 */
	public void	setPosition(int columnPosition)
	{
		this.columnPosition = columnPosition;
	}

	/**
	 * Convert the ColumnDescriptor to a String.
	 *
	 * @return	A String representation of this ColumnDescriptor
	 */

	public String	toString()
	{
		if (SanityManager.DEBUG)
		{
			/*
			** NOTE: This does not format table, because table.toString()
			** formats columns, leading to infinite recursion.
			*/
			return "columnName: " + columnName + "\n" +
				"columnPosition: " + columnPosition + "\n" +
				"columnType: " + columnType + "\n" +
				"columnDefault: " + columnDefault + "\n" +
// GemStone changes BEGIN
				(isLob ? "isLob: true\n" : "") +
				"maximumWidth: " + columnType.getMaximumWidth() + "\n" +
// GemStone changes END
				"uuid: " + uuid + "\n" +
				"defaultUUID: " + defaultUUID + "\n";
		}
		else
		{
		  return "columnName: " + columnName + "\n" +
      "columnPosition: " + columnPosition + "\n" +
      "columnType: " + columnType + "\n" +
      "columnDefault: " + columnDefault + "\n" +
      "uuid: " + uuid + "\n" +
      "defaultUUID: " + defaultUUID + "\n";
			//return "";
		}
	}
	
	/** @see TupleDescriptor#getDescriptorName */
	public String getDescriptorName()
	{
		// try and get rid of getColumnName!
		return columnName;
	}

	/** @see TupleDescriptor#getDescriptorType */
	public String getDescriptorType()
	{
		return "Column";
	}

	
	private static void assertAutoinc(boolean autoinc,
					  long autoincInc,
					  DefaultInfo defaultInfo){

		if (SanityManager.DEBUG) {
			if (autoinc){
				SanityManager.ASSERT((autoincInc != 0),
					"increment is zero for  autoincrement column");
				SanityManager.ASSERT((defaultInfo == null ||
					      defaultInfo.isDefaultValueAutoinc()),
					     "If column is autoinc and have defaultInfo, " + 
					     "isDefaultValueAutoinc must be true.");
			}
			else{
				SanityManager.ASSERT((autoincInc == 0),
					"increment is non-zero for non-autoincrement column");
				SanityManager.ASSERT((defaultInfo == null ||
					      ! defaultInfo.isDefaultValueAutoinc()),
					     "If column is not autoinc and have defaultInfo, " + 
					     "isDefaultValueAutoinc can not be true");
			}
		}
	}

// GemStone changes BEGIN
	@Override
	public int hashCode() {
	  final TableDescriptor td = this.table;
	  final String name = this.columnName;
	  if (td != null) {
	    return td.hashCode() ^ name.hashCode();
	  }
	  else {
	    return name.hashCode();
	  }
	}

	@Override
	public boolean equals(Object other) {
	  if (other == this) {
	    return true;
	  }
	  if (other instanceof ColumnDescriptor) {
	    ColumnDescriptor otherCD = (ColumnDescriptor)other;
	    final TableDescriptor td = this.table;
	    final TableDescriptor otherTD = otherCD.table;
	    if (td != null && otherTD != null) {
	      return this.columnName.equals(otherCD.columnName)
	          && td.equals(otherTD);
	    }
	  }
	  return false;
	}
// GemStone changes END
}
