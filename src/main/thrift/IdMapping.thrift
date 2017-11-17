#@namespace scala rever.id.service

include "IdMappingDT.thrift"

service TIdMappingService {
    IdMappingDT.TIdStatus check(1: string id)
    map<string, IdMappingDT.TIdStatus> mcheck(1: list<string> prettyIds)
    IdMappingDT.TIdAddResp addId(1: string prettyId)
    IdMappingDT.TIdAddResp addPrettyIdWithUid(1: string prettyId, 2: string id)
    IdMappingDT.TIdUpdateResp updatePrettyIdWithUid(1: string prettyId, 2: string newId)
    bool deleteId(1: string prettyId)
    IdMappingDT.TIdGetResp getId(1: string prettyId)
}