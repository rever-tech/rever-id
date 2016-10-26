package rever.id.controller.thrift

import com.google.inject.Inject
import com.twitter.finatra.thrift.Controller
import com.twitter.inject.Logging
import rever.id.domain.ThriftImplicit._
import rever.id.service.TIdMappingService.{AddId, AddPrettyIdWithUid, Check, DeleteId, GetId, Mcheck}
import rever.id.service.{IdMappingService, TIdMappingService}

/**
  * Created by tiennt4 on 24/10/2016.
  */
class IdMappingController @Inject()(service : IdMappingService) extends Controller with TIdMappingService.BaseServiceIface with Logging {
  override val check = handle(Check) { args: Check.Args =>
    service.checkId(args.id).map(resp => resp)
  }

  override val addId = handle(AddId) { args: AddId.Args =>
    service.add(args.prettyId).map(resp => resp)
  }

  override val deleteId = handle(DeleteId) { args: DeleteId.Args =>
    service.delete(args.prettyId)
  }

  override val mcheck = handle(Mcheck) { args: Mcheck.Args =>
    service.checkMulti(args.prettyIds).map(_.map(e => (e._1, IdStatus2T(e._2))))
  }

//  override val addIdsWithSameUId = handle(AddIdsWithSameUId) { args : AddIdsWithSameUId.Args => {
//    Future.value(null)
//  }}

  override val addPrettyIdWithUid = handle(AddPrettyIdWithUid) { args :AddPrettyIdWithUid.Args => {
    service.add(args.prettyId, args.id).map(resp => resp)
  }}

  override val getId = handle(GetId) { args :GetId.Args => {
    service.get(args.prettyId).map(resp => resp)
  }}
}
