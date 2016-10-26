package rever.id

import com.google.inject.Stage
import com.twitter.finatra.thrift.EmbeddedThriftServer
import com.twitter.inject.server.FeatureTest

/**
  * Created by tiennt4 on 25/10/2016.
  */
class IdMappingServiceStartupTest extends FeatureTest{
  val server = new EmbeddedThriftServer(
    stage = Stage.PRODUCTION,
    twitterServer = new Server
  )
  "IdMappingServer" should {
    "startup" in {
      server.assertHealthy()
    }
  }
}
