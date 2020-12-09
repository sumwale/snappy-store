/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package io.snappydata.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2020-12-07")
public class PrepareResult implements org.apache.thrift.TBase<PrepareResult, PrepareResult._Fields>, java.io.Serializable, Cloneable, Comparable<PrepareResult> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PrepareResult");

  private static final org.apache.thrift.protocol.TField STATEMENT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("statementId", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField STATEMENT_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("statementType", org.apache.thrift.protocol.TType.BYTE, (short)2);
  private static final org.apache.thrift.protocol.TField PARAMETER_META_DATA_FIELD_DESC = new org.apache.thrift.protocol.TField("parameterMetaData", org.apache.thrift.protocol.TType.LIST, (short)3);
  private static final org.apache.thrift.protocol.TField RESULT_SET_META_DATA_FIELD_DESC = new org.apache.thrift.protocol.TField("resultSetMetaData", org.apache.thrift.protocol.TType.LIST, (short)4);
  private static final org.apache.thrift.protocol.TField WARNINGS_FIELD_DESC = new org.apache.thrift.protocol.TField("warnings", org.apache.thrift.protocol.TType.STRUCT, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new PrepareResultStandardSchemeFactory());
    schemes.put(TupleScheme.class, new PrepareResultTupleSchemeFactory());
  }

  public long statementId; // required
  public byte statementType; // required
  public List<ColumnDescriptor> parameterMetaData; // required
  public List<ColumnDescriptor> resultSetMetaData; // optional
  public SnappyExceptionData warnings; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STATEMENT_ID((short)1, "statementId"),
    STATEMENT_TYPE((short)2, "statementType"),
    PARAMETER_META_DATA((short)3, "parameterMetaData"),
    RESULT_SET_META_DATA((short)4, "resultSetMetaData"),
    WARNINGS((short)5, "warnings");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // STATEMENT_ID
          return STATEMENT_ID;
        case 2: // STATEMENT_TYPE
          return STATEMENT_TYPE;
        case 3: // PARAMETER_META_DATA
          return PARAMETER_META_DATA;
        case 4: // RESULT_SET_META_DATA
          return RESULT_SET_META_DATA;
        case 5: // WARNINGS
          return WARNINGS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __STATEMENTID_ISSET_ID = 0;
  private static final int __STATEMENTTYPE_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.RESULT_SET_META_DATA,_Fields.WARNINGS};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STATEMENT_ID, new org.apache.thrift.meta_data.FieldMetaData("statementId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.STATEMENT_TYPE, new org.apache.thrift.meta_data.FieldMetaData("statementType", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)));
    tmpMap.put(_Fields.PARAMETER_META_DATA, new org.apache.thrift.meta_data.FieldMetaData("parameterMetaData", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ColumnDescriptor.class))));
    tmpMap.put(_Fields.RESULT_SET_META_DATA, new org.apache.thrift.meta_data.FieldMetaData("resultSetMetaData", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ColumnDescriptor.class))));
    tmpMap.put(_Fields.WARNINGS, new org.apache.thrift.meta_data.FieldMetaData("warnings", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, SnappyExceptionData.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PrepareResult.class, metaDataMap);
  }

  public PrepareResult() {
  }

  public PrepareResult(
    long statementId,
    byte statementType,
    List<ColumnDescriptor> parameterMetaData)
  {
    this();
    this.statementId = statementId;
    setStatementIdIsSet(true);
    this.statementType = statementType;
    setStatementTypeIsSet(true);
    this.parameterMetaData = parameterMetaData;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PrepareResult(PrepareResult other) {
    __isset_bitfield = other.__isset_bitfield;
    this.statementId = other.statementId;
    this.statementType = other.statementType;
    if (other.isSetParameterMetaData()) {
      List<ColumnDescriptor> __this__parameterMetaData = new ArrayList<ColumnDescriptor>(other.parameterMetaData.size());
      for (ColumnDescriptor other_element : other.parameterMetaData) {
        __this__parameterMetaData.add(new ColumnDescriptor(other_element));
      }
      this.parameterMetaData = __this__parameterMetaData;
    }
    if (other.isSetResultSetMetaData()) {
      List<ColumnDescriptor> __this__resultSetMetaData = new ArrayList<ColumnDescriptor>(other.resultSetMetaData.size());
      for (ColumnDescriptor other_element : other.resultSetMetaData) {
        __this__resultSetMetaData.add(new ColumnDescriptor(other_element));
      }
      this.resultSetMetaData = __this__resultSetMetaData;
    }
    if (other.isSetWarnings()) {
      this.warnings = new SnappyExceptionData(other.warnings);
    }
  }

  public PrepareResult deepCopy() {
    return new PrepareResult(this);
  }

  @Override
  public void clear() {
    setStatementIdIsSet(false);
    this.statementId = 0;
    setStatementTypeIsSet(false);
    this.statementType = 0;
    this.parameterMetaData = null;
    this.resultSetMetaData = null;
    this.warnings = null;
  }

  public long getStatementId() {
    return this.statementId;
  }

  public PrepareResult setStatementId(long statementId) {
    this.statementId = statementId;
    setStatementIdIsSet(true);
    return this;
  }

  public void unsetStatementId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __STATEMENTID_ISSET_ID);
  }

  /** Returns true if field statementId is set (has been assigned a value) and false otherwise */
  public boolean isSetStatementId() {
    return EncodingUtils.testBit(__isset_bitfield, __STATEMENTID_ISSET_ID);
  }

  public void setStatementIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __STATEMENTID_ISSET_ID, value);
  }

  public byte getStatementType() {
    return this.statementType;
  }

  public PrepareResult setStatementType(byte statementType) {
    this.statementType = statementType;
    setStatementTypeIsSet(true);
    return this;
  }

  public void unsetStatementType() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __STATEMENTTYPE_ISSET_ID);
  }

  /** Returns true if field statementType is set (has been assigned a value) and false otherwise */
  public boolean isSetStatementType() {
    return EncodingUtils.testBit(__isset_bitfield, __STATEMENTTYPE_ISSET_ID);
  }

  public void setStatementTypeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __STATEMENTTYPE_ISSET_ID, value);
  }

  public int getParameterMetaDataSize() {
    return (this.parameterMetaData == null) ? 0 : this.parameterMetaData.size();
  }

  public java.util.Iterator<ColumnDescriptor> getParameterMetaDataIterator() {
    return (this.parameterMetaData == null) ? null : this.parameterMetaData.iterator();
  }

  public void addToParameterMetaData(ColumnDescriptor elem) {
    if (this.parameterMetaData == null) {
      this.parameterMetaData = new ArrayList<ColumnDescriptor>();
    }
    this.parameterMetaData.add(elem);
  }

  public List<ColumnDescriptor> getParameterMetaData() {
    return this.parameterMetaData;
  }

  public PrepareResult setParameterMetaData(List<ColumnDescriptor> parameterMetaData) {
    this.parameterMetaData = parameterMetaData;
    return this;
  }

  public void unsetParameterMetaData() {
    this.parameterMetaData = null;
  }

  /** Returns true if field parameterMetaData is set (has been assigned a value) and false otherwise */
  public boolean isSetParameterMetaData() {
    return this.parameterMetaData != null;
  }

  public void setParameterMetaDataIsSet(boolean value) {
    if (!value) {
      this.parameterMetaData = null;
    }
  }

  public int getResultSetMetaDataSize() {
    return (this.resultSetMetaData == null) ? 0 : this.resultSetMetaData.size();
  }

  public java.util.Iterator<ColumnDescriptor> getResultSetMetaDataIterator() {
    return (this.resultSetMetaData == null) ? null : this.resultSetMetaData.iterator();
  }

  public void addToResultSetMetaData(ColumnDescriptor elem) {
    if (this.resultSetMetaData == null) {
      this.resultSetMetaData = new ArrayList<ColumnDescriptor>();
    }
    this.resultSetMetaData.add(elem);
  }

  public List<ColumnDescriptor> getResultSetMetaData() {
    return this.resultSetMetaData;
  }

  public PrepareResult setResultSetMetaData(List<ColumnDescriptor> resultSetMetaData) {
    this.resultSetMetaData = resultSetMetaData;
    return this;
  }

  public void unsetResultSetMetaData() {
    this.resultSetMetaData = null;
  }

  /** Returns true if field resultSetMetaData is set (has been assigned a value) and false otherwise */
  public boolean isSetResultSetMetaData() {
    return this.resultSetMetaData != null;
  }

  public void setResultSetMetaDataIsSet(boolean value) {
    if (!value) {
      this.resultSetMetaData = null;
    }
  }

  public SnappyExceptionData getWarnings() {
    return this.warnings;
  }

  public PrepareResult setWarnings(SnappyExceptionData warnings) {
    this.warnings = warnings;
    return this;
  }

  public void unsetWarnings() {
    this.warnings = null;
  }

  /** Returns true if field warnings is set (has been assigned a value) and false otherwise */
  public boolean isSetWarnings() {
    return this.warnings != null;
  }

  public void setWarningsIsSet(boolean value) {
    if (!value) {
      this.warnings = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case STATEMENT_ID:
      if (value == null) {
        unsetStatementId();
      } else {
        setStatementId((Long)value);
      }
      break;

    case STATEMENT_TYPE:
      if (value == null) {
        unsetStatementType();
      } else {
        setStatementType((Byte)value);
      }
      break;

    case PARAMETER_META_DATA:
      if (value == null) {
        unsetParameterMetaData();
      } else {
        setParameterMetaData((List<ColumnDescriptor>)value);
      }
      break;

    case RESULT_SET_META_DATA:
      if (value == null) {
        unsetResultSetMetaData();
      } else {
        setResultSetMetaData((List<ColumnDescriptor>)value);
      }
      break;

    case WARNINGS:
      if (value == null) {
        unsetWarnings();
      } else {
        setWarnings((SnappyExceptionData)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case STATEMENT_ID:
      return getStatementId();

    case STATEMENT_TYPE:
      return getStatementType();

    case PARAMETER_META_DATA:
      return getParameterMetaData();

    case RESULT_SET_META_DATA:
      return getResultSetMetaData();

    case WARNINGS:
      return getWarnings();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case STATEMENT_ID:
      return isSetStatementId();
    case STATEMENT_TYPE:
      return isSetStatementType();
    case PARAMETER_META_DATA:
      return isSetParameterMetaData();
    case RESULT_SET_META_DATA:
      return isSetResultSetMetaData();
    case WARNINGS:
      return isSetWarnings();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof PrepareResult)
      return this.equals((PrepareResult)that);
    return false;
  }

  public boolean equals(PrepareResult that) {
    if (that == null)
      return false;

    boolean this_present_statementId = true;
    boolean that_present_statementId = true;
    if (this_present_statementId || that_present_statementId) {
      if (!(this_present_statementId && that_present_statementId))
        return false;
      if (this.statementId != that.statementId)
        return false;
    }

    boolean this_present_statementType = true;
    boolean that_present_statementType = true;
    if (this_present_statementType || that_present_statementType) {
      if (!(this_present_statementType && that_present_statementType))
        return false;
      if (this.statementType != that.statementType)
        return false;
    }

    boolean this_present_parameterMetaData = true && this.isSetParameterMetaData();
    boolean that_present_parameterMetaData = true && that.isSetParameterMetaData();
    if (this_present_parameterMetaData || that_present_parameterMetaData) {
      if (!(this_present_parameterMetaData && that_present_parameterMetaData))
        return false;
      if (!this.parameterMetaData.equals(that.parameterMetaData))
        return false;
    }

    boolean this_present_resultSetMetaData = true && this.isSetResultSetMetaData();
    boolean that_present_resultSetMetaData = true && that.isSetResultSetMetaData();
    if (this_present_resultSetMetaData || that_present_resultSetMetaData) {
      if (!(this_present_resultSetMetaData && that_present_resultSetMetaData))
        return false;
      if (!this.resultSetMetaData.equals(that.resultSetMetaData))
        return false;
    }

    boolean this_present_warnings = true && this.isSetWarnings();
    boolean that_present_warnings = true && that.isSetWarnings();
    if (this_present_warnings || that_present_warnings) {
      if (!(this_present_warnings && that_present_warnings))
        return false;
      if (!this.warnings.equals(that.warnings))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_statementId = true;
    list.add(present_statementId);
    if (present_statementId)
      list.add(statementId);

    boolean present_statementType = true;
    list.add(present_statementType);
    if (present_statementType)
      list.add(statementType);

    boolean present_parameterMetaData = true && (isSetParameterMetaData());
    list.add(present_parameterMetaData);
    if (present_parameterMetaData)
      list.add(parameterMetaData);

    boolean present_resultSetMetaData = true && (isSetResultSetMetaData());
    list.add(present_resultSetMetaData);
    if (present_resultSetMetaData)
      list.add(resultSetMetaData);

    boolean present_warnings = true && (isSetWarnings());
    list.add(present_warnings);
    if (present_warnings)
      list.add(warnings);

    return list.hashCode();
  }

  @Override
  public int compareTo(PrepareResult other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetStatementId()).compareTo(other.isSetStatementId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStatementId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.statementId, other.statementId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetStatementType()).compareTo(other.isSetStatementType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStatementType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.statementType, other.statementType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetParameterMetaData()).compareTo(other.isSetParameterMetaData());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetParameterMetaData()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.parameterMetaData, other.parameterMetaData);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetResultSetMetaData()).compareTo(other.isSetResultSetMetaData());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetResultSetMetaData()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.resultSetMetaData, other.resultSetMetaData);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetWarnings()).compareTo(other.isSetWarnings());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetWarnings()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.warnings, other.warnings);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("PrepareResult(");
    boolean first = true;

    sb.append("statementId:");
    sb.append(this.statementId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("statementType:");
    sb.append(this.statementType);
    first = false;
    if (!first) sb.append(", ");
    sb.append("parameterMetaData:");
    if (this.parameterMetaData == null) {
      sb.append("null");
    } else {
      sb.append(this.parameterMetaData);
    }
    first = false;
    if (isSetResultSetMetaData()) {
      if (!first) sb.append(", ");
      sb.append("resultSetMetaData:");
      if (this.resultSetMetaData == null) {
        sb.append("null");
      } else {
        sb.append(this.resultSetMetaData);
      }
      first = false;
    }
    if (isSetWarnings()) {
      if (!first) sb.append(", ");
      sb.append("warnings:");
      if (this.warnings == null) {
        sb.append("null");
      } else {
        sb.append(this.warnings);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'statementId' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'statementType' because it's a primitive and you chose the non-beans generator.
    if (parameterMetaData == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'parameterMetaData' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (warnings != null) {
      warnings.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class PrepareResultStandardSchemeFactory implements SchemeFactory {
    public PrepareResultStandardScheme getScheme() {
      return new PrepareResultStandardScheme();
    }
  }

  private static class PrepareResultStandardScheme extends StandardScheme<PrepareResult> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PrepareResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STATEMENT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.statementId = iprot.readI64();
              struct.setStatementIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // STATEMENT_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.BYTE) {
              struct.statementType = iprot.readByte();
              struct.setStatementTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PARAMETER_META_DATA
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list228 = iprot.readListBegin();
                struct.parameterMetaData = new ArrayList<ColumnDescriptor>(_list228.size);
                ColumnDescriptor _elem229;
                for (int _i230 = 0; _i230 < _list228.size; ++_i230)
                {
                  _elem229 = new ColumnDescriptor();
                  _elem229.read(iprot);
                  struct.parameterMetaData.add(_elem229);
                }
                iprot.readListEnd();
              }
              struct.setParameterMetaDataIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // RESULT_SET_META_DATA
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list231 = iprot.readListBegin();
                struct.resultSetMetaData = new ArrayList<ColumnDescriptor>(_list231.size);
                ColumnDescriptor _elem232;
                for (int _i233 = 0; _i233 < _list231.size; ++_i233)
                {
                  _elem232 = new ColumnDescriptor();
                  _elem232.read(iprot);
                  struct.resultSetMetaData.add(_elem232);
                }
                iprot.readListEnd();
              }
              struct.setResultSetMetaDataIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // WARNINGS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.warnings = new SnappyExceptionData();
              struct.warnings.read(iprot);
              struct.setWarningsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      if (!struct.isSetStatementId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'statementId' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetStatementType()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'statementType' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, PrepareResult struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(STATEMENT_ID_FIELD_DESC);
      oprot.writeI64(struct.statementId);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(STATEMENT_TYPE_FIELD_DESC);
      oprot.writeByte(struct.statementType);
      oprot.writeFieldEnd();
      if (struct.parameterMetaData != null) {
        oprot.writeFieldBegin(PARAMETER_META_DATA_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.parameterMetaData.size()));
          for (ColumnDescriptor _iter234 : struct.parameterMetaData)
          {
            _iter234.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.resultSetMetaData != null) {
        if (struct.isSetResultSetMetaData()) {
          oprot.writeFieldBegin(RESULT_SET_META_DATA_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.resultSetMetaData.size()));
            for (ColumnDescriptor _iter235 : struct.resultSetMetaData)
            {
              _iter235.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.warnings != null) {
        if (struct.isSetWarnings()) {
          oprot.writeFieldBegin(WARNINGS_FIELD_DESC);
          struct.warnings.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class PrepareResultTupleSchemeFactory implements SchemeFactory {
    public PrepareResultTupleScheme getScheme() {
      return new PrepareResultTupleScheme();
    }
  }

  private static class PrepareResultTupleScheme extends TupleScheme<PrepareResult> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PrepareResult struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI64(struct.statementId);
      oprot.writeByte(struct.statementType);
      {
        oprot.writeI32(struct.parameterMetaData.size());
        for (ColumnDescriptor _iter236 : struct.parameterMetaData)
        {
          _iter236.write(oprot);
        }
      }
      BitSet optionals = new BitSet();
      if (struct.isSetResultSetMetaData()) {
        optionals.set(0);
      }
      if (struct.isSetWarnings()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetResultSetMetaData()) {
        {
          oprot.writeI32(struct.resultSetMetaData.size());
          for (ColumnDescriptor _iter237 : struct.resultSetMetaData)
          {
            _iter237.write(oprot);
          }
        }
      }
      if (struct.isSetWarnings()) {
        struct.warnings.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PrepareResult struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.statementId = iprot.readI64();
      struct.setStatementIdIsSet(true);
      struct.statementType = iprot.readByte();
      struct.setStatementTypeIsSet(true);
      {
        org.apache.thrift.protocol.TList _list238 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
        struct.parameterMetaData = new ArrayList<ColumnDescriptor>(_list238.size);
        ColumnDescriptor _elem239;
        for (int _i240 = 0; _i240 < _list238.size; ++_i240)
        {
          _elem239 = new ColumnDescriptor();
          _elem239.read(iprot);
          struct.parameterMetaData.add(_elem239);
        }
      }
      struct.setParameterMetaDataIsSet(true);
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list241 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.resultSetMetaData = new ArrayList<ColumnDescriptor>(_list241.size);
          ColumnDescriptor _elem242;
          for (int _i243 = 0; _i243 < _list241.size; ++_i243)
          {
            _elem242 = new ColumnDescriptor();
            _elem242.read(iprot);
            struct.resultSetMetaData.add(_elem242);
          }
        }
        struct.setResultSetMetaDataIsSet(true);
      }
      if (incoming.get(1)) {
        struct.warnings = new SnappyExceptionData();
        struct.warnings.read(iprot);
        struct.setWarningsIsSet(true);
      }
    }
  }

}

