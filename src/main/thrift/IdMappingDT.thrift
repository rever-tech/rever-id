#@namespace scala rever.id.domain.thrift

struct TIdStatus {
    1: required bool exist,
    2: optional string suggest
}

struct TIdAddResp {
    1: required bool isOk,
    2: optional string id
}

struct TIdGetResp {
    1: required bool exist,
    2: optional string id
}

struct TIdUpdateResp {
    1: required bool isOk,
    2: optional string id,
    3: optional string oldId
}