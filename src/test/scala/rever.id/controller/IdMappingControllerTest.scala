package rever.id.controller

import java.net.InetSocketAddress

import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.thrift.ThriftClientFramedCodec
import com.twitter.finatra.thrift.EmbeddedThriftServer
import com.twitter.inject.server.FeatureTest
import rever.id.Server
import rever.id.service.TIdMappingService

/**
  * Created by tiennt4 on 25/10/2016.
  */
class IdMappingControllerTest extends FeatureTest {
  override protected def server = new EmbeddedThriftServer(twitterServer = new Server)

  lazy val clientService = ClientBuilder()
    .hosts(Seq(new InetSocketAddress("localhost", server.thriftExternalPort)))
    .codec(ThriftClientFramedCodec())
    .hostConnectionLimit(1)
    .build()
  val client = new TIdMappingService.FinagledClient(clientService)

  "[Thrift] Check on exist key" should {
    "return true" in {
      client.addId("exist_id").value._1 should equal(true)
      client.check("exist_id").value._1 should equal(true)
    }
  }

  "[Thrift] Check on not exist key" should {
    "return false" in {
      client.deleteId("exist_id").value should equal(true)
      client.check("exist_id").value._1 should equal(false)
    }
  }

  "[Thrift] Add new pair id" should {
    "successful" in {
      client.deleteId("prettyId").value should equal(true)
      val add = client.addPrettyIdWithUid("prettyId", "Test Value").value
      add._1 should equal(true)
      add._2 should equal(Option("Test Value"))
    }
  }

  "[Thrift] Add an exist pair id" should {
    "return false" in {
      client.addId("prettyId").value
      val add = client.addPrettyIdWithUid("prettyId", "Test Value").value
      add._1 should equal(false)
      add._2 should equal(None)
    }
  }

  "[Thrift] Add an exist id" should {
    "return false" in {
      client.deleteId("exist_id").value should equal(true)
      client.addId("exist_id").value
      val add = client.addId("exist_id").value
      add._1 should equal(false)
      add._2 should equal(None)
    }
  }

  "[Thrift] Add Id" should {
    "successful" in {
      client.deleteId("add_id").value should equal(true)
      val add = client.addId("add_id").value
      add._1 should equal(true)
      val get = client.getId("add_id").value
      get._1 should equal(true)
      add._2.get should equal(get._2.get)
    }
  }

  "[Thrift] Get return value" should {
    "equal with pushed value " in {
      client.deleteId("sample_key").value should equal(true)
      val addId = client.addId("sample_key").value
      addId._1 should equal(true)
      addId._2.get should equal(
        client.getId("sample_key").value._2.get
      )
    }
  }

  "[Thrift] Check multiple" should {
    "return true when at least one key exist" in {
      client.deleteId("1").value should equal(true)
      val add1 = client.addPrettyIdWithUid("1", "val").value
      add1._1 should equal(true)
      add1._2.get should equal("val")
      client.deleteId("2").value should equal(true)
      val add2 = client.addPrettyIdWithUid("2", "val").value
      add2._1 should equal(true)
      add2._2.get should equal("val")
      client.deleteId("3").value should equal(true)
      val add3 = client.addPrettyIdWithUid("3", "val").value
      add3._1 should equal(true)
      add3._2.get should equal("val")
      client.deleteId("4").value should equal(true)
      val add4 = client.addPrettyIdWithUid("4", "val").value
      add4._1 should equal(true)
      add4._2.get should equal("val")
      client.deleteId("5").value should equal(true)
      client.mcheck(Seq("1", "2", "3", "4", "5")).value.map(e => (e._1, e._2._1)) should equal(
        Map("1" -> true, "2" -> true, "3" -> true, "4" -> true, "5" -> false)
      )
    }
  }

}
