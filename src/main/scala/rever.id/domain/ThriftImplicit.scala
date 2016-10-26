package rever.id.domain

import rever.id.domain.thrift.{TIdAddResp, TIdGetResp, TIdStatus}

/**
  * Created by SangDang on 9/16/16.
  */
object ThriftImplicit {
  implicit def IdStatus2T (idStatus : IdStatus) : TIdStatus = TIdStatus(idStatus.exist, idStatus.suggest)
  implicit def IdAddResp2T(addResp : IdAddResp) : TIdAddResp = TIdAddResp(addResp.isOk, addResp.id)
  implicit def IdGetResp2T(getResp : IdGetResp) : TIdGetResp = TIdGetResp(getResp.exist, getResp.id)
}
