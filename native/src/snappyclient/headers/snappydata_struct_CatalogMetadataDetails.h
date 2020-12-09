/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

#ifndef SNAPPYDATA_STRUCT_CATALOGMETADATADETAILS_H
#define SNAPPYDATA_STRUCT_CATALOGMETADATADETAILS_H


#include "snappydata_struct_Decimal.h"
#include "snappydata_struct_BlobChunk.h"
#include "snappydata_struct_ClobChunk.h"
#include "snappydata_struct_TransactionXid.h"
#include "snappydata_struct_ServiceMetaData.h"
#include "snappydata_struct_ServiceMetaDataArgs.h"
#include "snappydata_struct_OpenConnectionArgs.h"
#include "snappydata_struct_ConnectionProperties.h"
#include "snappydata_struct_HostAddress.h"
#include "snappydata_struct_SnappyExceptionData.h"
#include "snappydata_struct_StatementAttrs.h"
#include "snappydata_struct_ColumnValue.h"
#include "snappydata_struct_ColumnDescriptor.h"
#include "snappydata_struct_Row.h"
#include "snappydata_struct_OutputParameter.h"
#include "snappydata_struct_RowSet.h"
#include "snappydata_struct_PrepareResult.h"
#include "snappydata_struct_UpdateResult.h"
#include "snappydata_struct_StatementResult.h"
#include "snappydata_struct_BucketOwners.h"
#include "snappydata_struct_CatalogStorage.h"
#include "snappydata_struct_CatalogSchemaObject.h"
#include "snappydata_struct_CatalogStats.h"
#include "snappydata_struct_CatalogTableObject.h"
#include "snappydata_struct_CatalogFunctionObject.h"
#include "snappydata_struct_CatalogPartitionObject.h"
#include "snappydata_struct_CatalogMetadataRequest.h"

#include "snappydata_types.h"

namespace io { namespace snappydata { namespace thrift {

typedef struct _CatalogMetadataDetails__isset {
  _CatalogMetadataDetails__isset() : names(false), properties(false), newProperties(false), catalogSchemaVersion(false), exists(false), otherFlags(false), catalogDatabase(false), catalogTable(false), catalogFunction(false), catalogPartitions(false), catalogStats(false), newSchema(false) {}
  bool names :1;
  bool properties :1;
  bool newProperties :1;
  bool catalogSchemaVersion :1;
  bool exists :1;
  bool otherFlags :1;
  bool catalogDatabase :1;
  bool catalogTable :1;
  bool catalogFunction :1;
  bool catalogPartitions :1;
  bool catalogStats :1;
  bool newSchema :1;
} _CatalogMetadataDetails__isset;

class CatalogMetadataDetails {
 public:

  CatalogMetadataDetails(const CatalogMetadataDetails&);
  CatalogMetadataDetails(CatalogMetadataDetails&&) noexcept;
  CatalogMetadataDetails& operator=(const CatalogMetadataDetails&);
  CatalogMetadataDetails& operator=(CatalogMetadataDetails&&) noexcept;
  CatalogMetadataDetails() : catalogSchemaVersion(0), exists(0), newSchema() {
  }

  virtual ~CatalogMetadataDetails() noexcept;
  std::vector<std::string>  names;
  std::vector<std::map<std::string, std::string> >  properties;
  std::vector<std::map<std::string, std::string> >  newProperties;
  int64_t catalogSchemaVersion;
  bool exists;
  std::vector<int32_t>  otherFlags;
  CatalogSchemaObject catalogDatabase;
  CatalogTableObject catalogTable;
  CatalogFunctionObject catalogFunction;
  std::vector<CatalogPartitionObject>  catalogPartitions;
  CatalogStats catalogStats;
  std::string newSchema;

  _CatalogMetadataDetails__isset __isset;

  void __set_names(const std::vector<std::string> & val);

  void __set_properties(const std::vector<std::map<std::string, std::string> > & val);

  void __set_newProperties(const std::vector<std::map<std::string, std::string> > & val);

  void __set_catalogSchemaVersion(const int64_t val);

  void __set_exists(const bool val);

  void __set_otherFlags(const std::vector<int32_t> & val);

  void __set_catalogDatabase(const CatalogSchemaObject& val);

  void __set_catalogTable(const CatalogTableObject& val);

  void __set_catalogFunction(const CatalogFunctionObject& val);

  void __set_catalogPartitions(const std::vector<CatalogPartitionObject> & val);

  void __set_catalogStats(const CatalogStats& val);

  void __set_newSchema(const std::string& val);

  bool operator == (const CatalogMetadataDetails & rhs) const
  {
    if (__isset.names != rhs.__isset.names)
      return false;
    else if (__isset.names && !(names == rhs.names))
      return false;
    if (__isset.properties != rhs.__isset.properties)
      return false;
    else if (__isset.properties && !(properties == rhs.properties))
      return false;
    if (__isset.newProperties != rhs.__isset.newProperties)
      return false;
    else if (__isset.newProperties && !(newProperties == rhs.newProperties))
      return false;
    if (__isset.catalogSchemaVersion != rhs.__isset.catalogSchemaVersion)
      return false;
    else if (__isset.catalogSchemaVersion && !(catalogSchemaVersion == rhs.catalogSchemaVersion))
      return false;
    if (__isset.exists != rhs.__isset.exists)
      return false;
    else if (__isset.exists && !(exists == rhs.exists))
      return false;
    if (__isset.otherFlags != rhs.__isset.otherFlags)
      return false;
    else if (__isset.otherFlags && !(otherFlags == rhs.otherFlags))
      return false;
    if (__isset.catalogDatabase != rhs.__isset.catalogDatabase)
      return false;
    else if (__isset.catalogDatabase && !(catalogDatabase == rhs.catalogDatabase))
      return false;
    if (__isset.catalogTable != rhs.__isset.catalogTable)
      return false;
    else if (__isset.catalogTable && !(catalogTable == rhs.catalogTable))
      return false;
    if (__isset.catalogFunction != rhs.__isset.catalogFunction)
      return false;
    else if (__isset.catalogFunction && !(catalogFunction == rhs.catalogFunction))
      return false;
    if (__isset.catalogPartitions != rhs.__isset.catalogPartitions)
      return false;
    else if (__isset.catalogPartitions && !(catalogPartitions == rhs.catalogPartitions))
      return false;
    if (__isset.catalogStats != rhs.__isset.catalogStats)
      return false;
    else if (__isset.catalogStats && !(catalogStats == rhs.catalogStats))
      return false;
    if (__isset.newSchema != rhs.__isset.newSchema)
      return false;
    else if (__isset.newSchema && !(newSchema == rhs.newSchema))
      return false;
    return true;
  }
  bool operator != (const CatalogMetadataDetails &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const CatalogMetadataDetails & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

  virtual void printTo(std::ostream& out) const;
};

void swap(CatalogMetadataDetails &a, CatalogMetadataDetails &b);

inline std::ostream& operator<<(std::ostream& out, const CatalogMetadataDetails& obj)
{
  obj.printTo(out);
  return out;
}

}}} // namespace

#endif
