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
 * 查询表格元数据响应
 */
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2021-05-30")
public class QueryTableMetaInfoResponse implements org.apache.thrift.TBase<QueryTableMetaInfoResponse, QueryTableMetaInfoResponse._Fields>, java.io.Serializable, Cloneable, Comparable<QueryTableMetaInfoResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("QueryTableMetaInfoResponse");

  private static final org.apache.thrift.protocol.TField META_FIELD_DESC = new org.apache.thrift.protocol.TField("meta", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField BASE_RESP_FIELD_DESC = new org.apache.thrift.protocol.TField("baseResp", org.apache.thrift.protocol.TType.STRUCT, (short)255);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new QueryTableMetaInfoResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new QueryTableMetaInfoResponseTupleSchemeFactory());
  }

  public List<com.siriusdb.thrift.model.VTableMeta> meta; // optional
  public com.siriusdb.thrift.model.BaseResp baseResp; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    META((short)1, "meta"),
    BASE_RESP((short)255, "baseResp");

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
        case 1: // META
          return META;
        case 255: // BASE_RESP
          return BASE_RESP;
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
  private static final _Fields optionals[] = {_Fields.META};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.META, new org.apache.thrift.meta_data.FieldMetaData("meta", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.siriusdb.thrift.model.VTableMeta.class))));
    tmpMap.put(_Fields.BASE_RESP, new org.apache.thrift.meta_data.FieldMetaData("baseResp", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.siriusdb.thrift.model.BaseResp.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(QueryTableMetaInfoResponse.class, metaDataMap);
  }

  public QueryTableMetaInfoResponse() {
  }

  public QueryTableMetaInfoResponse(
    com.siriusdb.thrift.model.BaseResp baseResp)
  {
    this();
    this.baseResp = baseResp;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public QueryTableMetaInfoResponse(QueryTableMetaInfoResponse other) {
    if (other.isSetMeta()) {
      List<com.siriusdb.thrift.model.VTableMeta> __this__meta = new ArrayList<com.siriusdb.thrift.model.VTableMeta>(other.meta.size());
      for (com.siriusdb.thrift.model.VTableMeta other_element : other.meta) {
        __this__meta.add(new com.siriusdb.thrift.model.VTableMeta(other_element));
      }
      this.meta = __this__meta;
    }
    if (other.isSetBaseResp()) {
      this.baseResp = new com.siriusdb.thrift.model.BaseResp(other.baseResp);
    }
  }

  public QueryTableMetaInfoResponse deepCopy() {
    return new QueryTableMetaInfoResponse(this);
  }

  @Override
  public void clear() {
    this.meta = null;
    this.baseResp = null;
  }

  public int getMetaSize() {
    return (this.meta == null) ? 0 : this.meta.size();
  }

  public java.util.Iterator<com.siriusdb.thrift.model.VTableMeta> getMetaIterator() {
    return (this.meta == null) ? null : this.meta.iterator();
  }

  public void addToMeta(com.siriusdb.thrift.model.VTableMeta elem) {
    if (this.meta == null) {
      this.meta = new ArrayList<com.siriusdb.thrift.model.VTableMeta>();
    }
    this.meta.add(elem);
  }

  public List<com.siriusdb.thrift.model.VTableMeta> getMeta() {
    return this.meta;
  }

  public QueryTableMetaInfoResponse setMeta(List<com.siriusdb.thrift.model.VTableMeta> meta) {
    this.meta = meta;
    return this;
  }

  public void unsetMeta() {
    this.meta = null;
  }

  /** Returns true if field meta is set (has been assigned a value) and false otherwise */
  public boolean isSetMeta() {
    return this.meta != null;
  }

  public void setMetaIsSet(boolean value) {
    if (!value) {
      this.meta = null;
    }
  }

  public com.siriusdb.thrift.model.BaseResp getBaseResp() {
    return this.baseResp;
  }

  public QueryTableMetaInfoResponse setBaseResp(com.siriusdb.thrift.model.BaseResp baseResp) {
    this.baseResp = baseResp;
    return this;
  }

  public void unsetBaseResp() {
    this.baseResp = null;
  }

  /** Returns true if field baseResp is set (has been assigned a value) and false otherwise */
  public boolean isSetBaseResp() {
    return this.baseResp != null;
  }

  public void setBaseRespIsSet(boolean value) {
    if (!value) {
      this.baseResp = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case META:
      if (value == null) {
        unsetMeta();
      } else {
        setMeta((List<com.siriusdb.thrift.model.VTableMeta>)value);
      }
      break;

    case BASE_RESP:
      if (value == null) {
        unsetBaseResp();
      } else {
        setBaseResp((com.siriusdb.thrift.model.BaseResp)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case META:
      return getMeta();

    case BASE_RESP:
      return getBaseResp();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case META:
      return isSetMeta();
    case BASE_RESP:
      return isSetBaseResp();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof QueryTableMetaInfoResponse)
      return this.equals((QueryTableMetaInfoResponse)that);
    return false;
  }

  public boolean equals(QueryTableMetaInfoResponse that) {
    if (that == null)
      return false;

    boolean this_present_meta = true && this.isSetMeta();
    boolean that_present_meta = true && that.isSetMeta();
    if (this_present_meta || that_present_meta) {
      if (!(this_present_meta && that_present_meta))
        return false;
      if (!this.meta.equals(that.meta))
        return false;
    }

    boolean this_present_baseResp = true && this.isSetBaseResp();
    boolean that_present_baseResp = true && that.isSetBaseResp();
    if (this_present_baseResp || that_present_baseResp) {
      if (!(this_present_baseResp && that_present_baseResp))
        return false;
      if (!this.baseResp.equals(that.baseResp))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_meta = true && (isSetMeta());
    list.add(present_meta);
    if (present_meta)
      list.add(meta);

    boolean present_baseResp = true && (isSetBaseResp());
    list.add(present_baseResp);
    if (present_baseResp)
      list.add(baseResp);

    return list.hashCode();
  }

  @Override
  public int compareTo(QueryTableMetaInfoResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetMeta()).compareTo(other.isSetMeta());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMeta()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.meta, other.meta);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBaseResp()).compareTo(other.isSetBaseResp());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBaseResp()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.baseResp, other.baseResp);
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
    StringBuilder sb = new StringBuilder("QueryTableMetaInfoResponse(");
    boolean first = true;

    if (isSetMeta()) {
      sb.append("meta:");
      if (this.meta == null) {
        sb.append("null");
      } else {
        sb.append(this.meta);
      }
      first = false;
    }
    if (!first) sb.append(", ");
    sb.append("baseResp:");
    if (this.baseResp == null) {
      sb.append("null");
    } else {
      sb.append(this.baseResp);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (baseResp == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'baseResp' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (baseResp != null) {
      baseResp.validate();
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

  private static class QueryTableMetaInfoResponseStandardSchemeFactory implements SchemeFactory {
    public QueryTableMetaInfoResponseStandardScheme getScheme() {
      return new QueryTableMetaInfoResponseStandardScheme();
    }
  }

  private static class QueryTableMetaInfoResponseStandardScheme extends StandardScheme<QueryTableMetaInfoResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, QueryTableMetaInfoResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // META
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list24 = iprot.readListBegin();
                struct.meta = new ArrayList<com.siriusdb.thrift.model.VTableMeta>(_list24.size);
                com.siriusdb.thrift.model.VTableMeta _elem25;
                for (int _i26 = 0; _i26 < _list24.size; ++_i26)
                {
                  _elem25 = new com.siriusdb.thrift.model.VTableMeta();
                  _elem25.read(iprot);
                  struct.meta.add(_elem25);
                }
                iprot.readListEnd();
              }
              struct.setMetaIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 255: // BASE_RESP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.baseResp = new com.siriusdb.thrift.model.BaseResp();
              struct.baseResp.read(iprot);
              struct.setBaseRespIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, QueryTableMetaInfoResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.meta != null) {
        if (struct.isSetMeta()) {
          oprot.writeFieldBegin(META_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.meta.size()));
            for (com.siriusdb.thrift.model.VTableMeta _iter27 : struct.meta)
            {
              _iter27.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.baseResp != null) {
        oprot.writeFieldBegin(BASE_RESP_FIELD_DESC);
        struct.baseResp.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class QueryTableMetaInfoResponseTupleSchemeFactory implements SchemeFactory {
    public QueryTableMetaInfoResponseTupleScheme getScheme() {
      return new QueryTableMetaInfoResponseTupleScheme();
    }
  }

  private static class QueryTableMetaInfoResponseTupleScheme extends TupleScheme<QueryTableMetaInfoResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, QueryTableMetaInfoResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      struct.baseResp.write(oprot);
      BitSet optionals = new BitSet();
      if (struct.isSetMeta()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetMeta()) {
        {
          oprot.writeI32(struct.meta.size());
          for (com.siriusdb.thrift.model.VTableMeta _iter28 : struct.meta)
          {
            _iter28.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, QueryTableMetaInfoResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.baseResp = new com.siriusdb.thrift.model.BaseResp();
      struct.baseResp.read(iprot);
      struct.setBaseRespIsSet(true);
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list29 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.meta = new ArrayList<com.siriusdb.thrift.model.VTableMeta>(_list29.size);
          com.siriusdb.thrift.model.VTableMeta _elem30;
          for (int _i31 = 0; _i31 < _list29.size; ++_i31)
          {
            _elem30 = new com.siriusdb.thrift.model.VTableMeta();
            _elem30.read(iprot);
            struct.meta.add(_elem30);
          }
        }
        struct.setMetaIsSet(true);
      }
    }
  }

}

