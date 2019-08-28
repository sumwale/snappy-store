/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

#ifndef SNAPPYDATA_STRUCT_TRANSACTIONXID_H
#define SNAPPYDATA_STRUCT_TRANSACTIONXID_H


#include "snappydata_struct_Decimal.h"
#include "snappydata_struct_BlobChunk.h"
#include "snappydata_struct_ClobChunk.h"

#include "snappydata_types.h"

namespace io { namespace snappydata { namespace thrift {


class TransactionXid {
 public:

  TransactionXid(const TransactionXid&);
  TransactionXid(TransactionXid&&) noexcept;
  TransactionXid& operator=(const TransactionXid&);
  TransactionXid& operator=(TransactionXid&&) noexcept;
  TransactionXid() : formatId(0), globalId(), branchQualifier() {
  }

  virtual ~TransactionXid() noexcept;
  int32_t formatId;
  std::string globalId;
  std::string branchQualifier;

  void __set_formatId(const int32_t val);

  void __set_globalId(const std::string& val);

  void __set_branchQualifier(const std::string& val);

  bool operator == (const TransactionXid & rhs) const
  {
    if (!(formatId == rhs.formatId))
      return false;
    if (!(globalId == rhs.globalId))
      return false;
    if (!(branchQualifier == rhs.branchQualifier))
      return false;
    return true;
  }
  bool operator != (const TransactionXid &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const TransactionXid & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

  virtual void printTo(std::ostream& out) const;
};

void swap(TransactionXid &a, TransactionXid &b);

inline std::ostream& operator<<(std::ostream& out, const TransactionXid& obj)
{
  obj.printTo(out);
  return out;
}

}}} // namespace

#endif
