namespace java com.siriusdb.thrift

struct Base {
    1: string caller = "",
    2: string hostUrl = "",
    3: string hostName = "",
}

struct BaseResp {
    1: string message = "",
    2: i32 code = 0,
}
