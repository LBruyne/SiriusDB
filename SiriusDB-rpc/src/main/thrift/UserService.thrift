namespace java com.siriusdb.common.thrift

service  UserService {
  string getName(1:i32 id)
  bool isExist(1:string name)
}