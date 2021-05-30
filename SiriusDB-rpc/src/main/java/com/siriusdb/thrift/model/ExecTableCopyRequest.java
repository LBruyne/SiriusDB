/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.siriusdb.thrift.model;

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
/**
 * master告知region表格复制请求
 */
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2021-05-26")
public class ExecTableCopyRequest implements org.apache.thrift.TBase<ExecTableCopyRequest, ExecTableCopyRequest._Fields>, java.io.Serializable, Cloneable, Comparable<ExecTableCopyRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ExecTableCopyRequest");

  private static final org.apache.thrift.protocol.TField TARGET_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("targetName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField TARGET_URL_FIELD_DESC = new org.apache.thrift.protocol.TField("targetUrl", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField TABLE_NAMES_FIELD_DESC = new org.apache.thrift.protocol.TField("tableNames", org.apache.thrift.protocol.TType.LIST, (short)3);
  private static final org.apache.thrift.protocol.TField BASE_FIELD_DESC = new org.apache.thrift.protocol.TField("base", org.apache.thrift.protocol.TType.STRUCT, (short)255);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ExecTableCopyRequestStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ExecTableCopyRequestTupleSchemeFactory());
  }

  public String targetName; // required
  public String targetUrl; // required
  public List<String> tableNames; // required
  public com.siriusdb.thrift.model.Base base; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TARGET_NAME((short)1, "targetName"),
    TARGET_URL((short)2, "targetUrl"),
    TABLE_NAMES((short)3, "tableNames"),
    BASE((short)255, "base");

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
        case 1: // TARGET_NAME
          return TARGET_NAME;
        case 2: // TARGET_URL
          return TARGET_URL;
        case 3: // TABLE_NAMES
          return TABLE_NAMES;
        case 255: // BASE
          return BASE;
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
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TARGET_NAME, new org.apache.thrift.meta_data.FieldMetaData("targetName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.TARGET_URL, new org.apache.thrift.meta_data.FieldMetaData("targetUrl", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.TABLE_NAMES, new org.apache.thrift.meta_data.FieldMetaData("tableNames", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.BASE, new org.apache.thrift.meta_data.FieldMetaData("base", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.siriusdb.thrift.model.Base.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ExecTableCopyRequest.class, metaDataMap);
  }

  public ExecTableCopyRequest() {
  }

  public ExecTableCopyRequest(
    String targetName,
    String targetUrl,
    List<String> tableNames,
    com.siriusdb.thrift.model.Base base)
  {
    this();
    this.targetName = targetName;
    this.targetUrl = targetUrl;
    this.tableNames = tableNames;
    this.base = base;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ExecTableCopyRequest(ExecTableCopyRequest other) {
    if (other.isSetTargetName()) {
      this.targetName = other.targetName;
    }
    if (other.isSetTargetUrl()) {
      this.targetUrl = other.targetUrl;
    }
    if (other.isSetTableNames()) {
      List<String> __this__tableNames = new ArrayList<String>(other.tableNames);
      this.tableNames = __this__tableNames;
    }
    if (other.isSetBase()) {
      this.base = new com.siriusdb.thrift.model.Base(other.base);
    }
  }

  public ExecTableCopyRequest deepCopy() {
    return new ExecTableCopyRequest(this);
  }

  @Override
  public void clear() {
    this.targetName = null;
    this.targetUrl = null;
    this.tableNames = null;
    this.base = null;
  }

  public String getTargetName() {
    return this.targetName;
  }

  public ExecTableCopyRequest setTargetName(String targetName) {
    this.targetName = targetName;
    return this;
  }

  public void unsetTargetName() {
    this.targetName = null;
  }

  /** Returns true if field targetName is set (has been assigned a value) and false otherwise */
  public boolean isSetTargetName() {
    return this.targetName != null;
  }

  public void setTargetNameIsSet(boolean value) {
    if (!value) {
      this.targetName = null;
    }
  }

  public String getTargetUrl() {
    return this.targetUrl;
  }

  public ExecTableCopyRequest setTargetUrl(String targetUrl) {
    this.targetUrl = targetUrl;
    return this;
  }

  public void unsetTargetUrl() {
    this.targetUrl = null;
  }

  /** Returns true if field targetUrl is set (has been assigned a value) and false otherwise */
  public boolean isSetTargetUrl() {
    return this.targetUrl != null;
  }

  public void setTargetUrlIsSet(boolean value) {
    if (!value) {
      this.targetUrl = null;
    }
  }

  public int getTableNamesSize() {
    return (this.tableNames == null) ? 0 : this.tableNames.size();
  }

  public java.util.Iterator<String> getTableNamesIterator() {
    return (this.tableNames == null) ? null : this.tableNames.iterator();
  }

  public void addToTableNames(String elem) {
    if (this.tableNames == null) {
      this.tableNames = new ArrayList<String>();
    }
    this.tableNames.add(elem);
  }

  public List<String> getTableNames() {
    return this.tableNames;
  }

  public ExecTableCopyRequest setTableNames(List<String> tableNames) {
    this.tableNames = tableNames;
    return this;
  }

  public void unsetTableNames() {
    this.tableNames = null;
  }

  /** Returns true if field tableNames is set (has been assigned a value) and false otherwise */
  public boolean isSetTableNames() {
    return this.tableNames != null;
  }

  public void setTableNamesIsSet(boolean value) {
    if (!value) {
      this.tableNames = null;
    }
  }

  public com.siriusdb.thrift.model.Base getBase() {
    return this.base;
  }

  public ExecTableCopyRequest setBase(com.siriusdb.thrift.model.Base base) {
    this.base = base;
    return this;
  }

  public void unsetBase() {
    this.base = null;
  }

  /** Returns true if field base is set (has been assigned a value) and false otherwise */
  public boolean isSetBase() {
    return this.base != null;
  }

  public void setBaseIsSet(boolean value) {
    if (!value) {
      this.base = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case TARGET_NAME:
      if (value == null) {
        unsetTargetName();
      } else {
        setTargetName((String)value);
      }
      break;

    case TARGET_URL:
      if (value == null) {
        unsetTargetUrl();
      } else {
        setTargetUrl((String)value);
      }
      break;

    case TABLE_NAMES:
      if (value == null) {
        unsetTableNames();
      } else {
        setTableNames((List<String>)value);
      }
      break;

    case BASE:
      if (value == null) {
        unsetBase();
      } else {
        setBase((com.siriusdb.thrift.model.Base)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case TARGET_NAME:
      return getTargetName();

    case TARGET_URL:
      return getTargetUrl();

    case TABLE_NAMES:
      return getTableNames();

    case BASE:
      return getBase();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case TARGET_NAME:
      return isSetTargetName();
    case TARGET_URL:
      return isSetTargetUrl();
    case TABLE_NAMES:
      return isSetTableNames();
    case BASE:
      return isSetBase();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ExecTableCopyRequest)
      return this.equals((ExecTableCopyRequest)that);
    return false;
  }

  public boolean equals(ExecTableCopyRequest that) {
    if (that == null)
      return false;

    boolean this_present_targetName = true && this.isSetTargetName();
    boolean that_present_targetName = true && that.isSetTargetName();
    if (this_present_targetName || that_present_targetName) {
      if (!(this_present_targetName && that_present_targetName))
        return false;
      if (!this.targetName.equals(that.targetName))
        return false;
    }

    boolean this_present_targetUrl = true && this.isSetTargetUrl();
    boolean that_present_targetUrl = true && that.isSetTargetUrl();
    if (this_present_targetUrl || that_present_targetUrl) {
      if (!(this_present_targetUrl && that_present_targetUrl))
        return false;
      if (!this.targetUrl.equals(that.targetUrl))
        return false;
    }

    boolean this_present_tableNames = true && this.isSetTableNames();
    boolean that_present_tableNames = true && that.isSetTableNames();
    if (this_present_tableNames || that_present_tableNames) {
      if (!(this_present_tableNames && that_present_tableNames))
        return false;
      if (!this.tableNames.equals(that.tableNames))
        return false;
    }

    boolean this_present_base = true && this.isSetBase();
    boolean that_present_base = true && that.isSetBase();
    if (this_present_base || that_present_base) {
      if (!(this_present_base && that_present_base))
        return false;
      if (!this.base.equals(that.base))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_targetName = true && (isSetTargetName());
    list.add(present_targetName);
    if (present_targetName)
      list.add(targetName);

    boolean present_targetUrl = true && (isSetTargetUrl());
    list.add(present_targetUrl);
    if (present_targetUrl)
      list.add(targetUrl);

    boolean present_tableNames = true && (isSetTableNames());
    list.add(present_tableNames);
    if (present_tableNames)
      list.add(tableNames);

    boolean present_base = true && (isSetBase());
    list.add(present_base);
    if (present_base)
      list.add(base);

    return list.hashCode();
  }

  @Override
  public int compareTo(ExecTableCopyRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetTargetName()).compareTo(other.isSetTargetName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTargetName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.targetName, other.targetName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTargetUrl()).compareTo(other.isSetTargetUrl());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTargetUrl()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.targetUrl, other.targetUrl);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTableNames()).compareTo(other.isSetTableNames());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTableNames()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tableNames, other.tableNames);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBase()).compareTo(other.isSetBase());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBase()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.base, other.base);
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
    StringBuilder sb = new StringBuilder("ExecTableCopyRequest(");
    boolean first = true;

    sb.append("targetName:");
    if (this.targetName == null) {
      sb.append("null");
    } else {
      sb.append(this.targetName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("targetUrl:");
    if (this.targetUrl == null) {
      sb.append("null");
    } else {
      sb.append(this.targetUrl);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("tableNames:");
    if (this.tableNames == null) {
      sb.append("null");
    } else {
      sb.append(this.tableNames);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("base:");
    if (this.base == null) {
      sb.append("null");
    } else {
      sb.append(this.base);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (targetName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'targetName' was not present! Struct: " + toString());
    }
    if (targetUrl == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'targetUrl' was not present! Struct: " + toString());
    }
    if (tableNames == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'tableNames' was not present! Struct: " + toString());
    }
    if (base == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'base' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (base != null) {
      base.validate();
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ExecTableCopyRequestStandardSchemeFactory implements SchemeFactory {
    public ExecTableCopyRequestStandardScheme getScheme() {
      return new ExecTableCopyRequestStandardScheme();
    }
  }

  private static class ExecTableCopyRequestStandardScheme extends StandardScheme<ExecTableCopyRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ExecTableCopyRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TARGET_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.targetName = iprot.readString();
              struct.setTargetNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // TARGET_URL
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.targetUrl = iprot.readString();
              struct.setTargetUrlIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // TABLE_NAMES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list48 = iprot.readListBegin();
                struct.tableNames = new ArrayList<String>(_list48.size);
                String _elem49;
                for (int _i50 = 0; _i50 < _list48.size; ++_i50)
                {
                  _elem49 = iprot.readString();
                  struct.tableNames.add(_elem49);
                }
                iprot.readListEnd();
              }
              struct.setTableNamesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 255: // BASE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.base = new com.siriusdb.thrift.model.Base();
              struct.base.read(iprot);
              struct.setBaseIsSet(true);
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
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ExecTableCopyRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.targetName != null) {
        oprot.writeFieldBegin(TARGET_NAME_FIELD_DESC);
        oprot.writeString(struct.targetName);
        oprot.writeFieldEnd();
      }
      if (struct.targetUrl != null) {
        oprot.writeFieldBegin(TARGET_URL_FIELD_DESC);
        oprot.writeString(struct.targetUrl);
        oprot.writeFieldEnd();
      }
      if (struct.tableNames != null) {
        oprot.writeFieldBegin(TABLE_NAMES_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.tableNames.size()));
          for (String _iter51 : struct.tableNames)
          {
            oprot.writeString(_iter51);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.base != null) {
        oprot.writeFieldBegin(BASE_FIELD_DESC);
        struct.base.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ExecTableCopyRequestTupleSchemeFactory implements SchemeFactory {
    public ExecTableCopyRequestTupleScheme getScheme() {
      return new ExecTableCopyRequestTupleScheme();
    }
  }

  private static class ExecTableCopyRequestTupleScheme extends TupleScheme<ExecTableCopyRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ExecTableCopyRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.targetName);
      oprot.writeString(struct.targetUrl);
      {
        oprot.writeI32(struct.tableNames.size());
        for (String _iter52 : struct.tableNames)
        {
          oprot.writeString(_iter52);
        }
      }
      struct.base.write(oprot);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ExecTableCopyRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.targetName = iprot.readString();
      struct.setTargetNameIsSet(true);
      struct.targetUrl = iprot.readString();
      struct.setTargetUrlIsSet(true);
      {
        org.apache.thrift.protocol.TList _list53 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
        struct.tableNames = new ArrayList<String>(_list53.size);
        String _elem54;
        for (int _i55 = 0; _i55 < _list53.size; ++_i55)
        {
          _elem54 = iprot.readString();
          struct.tableNames.add(_elem54);
        }
      }
      struct.setTableNamesIsSet(true);
      struct.base = new com.siriusdb.thrift.model.Base();
      struct.base.read(iprot);
      struct.setBaseIsSet(true);
    }
  }

}

