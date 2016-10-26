package rever.id


import com.twitter.finatra.thrift.ThriftServer
import com.twitter.finatra.thrift.routing.ThriftRouter
import rever.id.controller.thrift.IdMappingController
import rever.id.module.IdGenModule
import rever.id.util.ZConfig

/**
  * Created by SangDang on 9/8/
  **/
object MainApp extends Server
class Server extends ThriftServer {

  override protected def defaultFinatraThriftPort: String = ZConfig.getString("server.thrift.port",":8082")

  override protected def disableAdminHttpServer: Boolean = ZConfig.getBoolean("server.admin.disable",true)

  override val modules = Seq(IdGenModule)

  override protected def configureThrift(router: ThriftRouter): Unit = {
    router
      .add[IdMappingController]
  }
}
