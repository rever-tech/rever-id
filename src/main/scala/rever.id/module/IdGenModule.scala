package rever.id.module

import com.twitter.inject.TwitterModule
import rever.id.service.{IdMappingService, IdMappingServiceImpl}

/**
  * Created by tiennt4 on 24/10/2016.
  */
object IdGenModule extends TwitterModule{
  override def configure(): Unit = {
    bind[IdMappingService].to[IdMappingServiceImpl]
  }
}
