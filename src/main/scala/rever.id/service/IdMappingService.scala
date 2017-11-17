package rever.id.service

import java.util.UUID

import com.google.inject.Inject
import com.twitter.util.Future
import org.nutz.ssdb4j.SSDBs
import rever.id.domain.{IdAddResp, IdGetResp, IdStatus, IdUpdateResp}
import rever.id.util.ZConfig

/**
  * Created by tiennt4 on 24/10/2016.
  */
trait IdMappingService {
  def checkId(prettyId: String): Future[IdStatus]

  def delete(prettyId: String): Future[Boolean]

  def add(prettyId: String): Future[IdAddResp]

  def add(prettyId: String, id: String): Future[IdAddResp]

  def update(prettyId: String, newId: String): Future[IdUpdateResp]

  def addMulti(prettyIds: Seq[String]): Future[IdAddResp]

  def checkMulti(ids: Seq[String]): Future[Map[String, IdStatus]]

  def get(prettyId: String): Future[IdGetResp]
}

case class IdMappingServiceImpl @Inject()() extends IdMappingService {

  val ssdbHost = ZConfig.getString("ssdb.host")
  val ssdbPort = ZConfig.getInt("ssdb.port")
  val ssdbTimeout = ZConfig.getInt("ssdb.timeout", 2000)
  val ssdbAuth = Option(ZConfig.getString("ssdb.auth", null))

  val ssdbClient = SSDBs.pool(ssdbHost, ssdbPort, ssdbTimeout, null, ssdbAuth.map(s => s.getBytes).orNull)

  override def checkId(id: String): Future[IdStatus] = futurePool {
    val resp = ssdbClient.exists(id)
    (resp.ok(), resp.asInt()) match {
      case (true, 1) => IdStatus(exist = true, Option(s"${id}_${System.currentTimeMillis()}"))
      case (true, 0) => IdStatus(exist = false, None)
      case _ => throw new Exception("SSDB Client response failure")
    }
  }

  override def delete(id: String): Future[Boolean] = futurePool {
    val resp = ssdbClient.del(id)
    resp.ok()
  }

  override def add(prettyId: String): Future[IdAddResp] = futurePool {
    val id = genUniqueId
    val resp = ssdbClient.setnx(prettyId, id)
    resp.ok() match {
      case true => resp.asInt() match {
        case 1 => IdAddResp(isOk = true, Option(id))
        case _ => IdAddResp(isOk = false, None)
      }
      case false => throw new Exception("SSDB Client response failure")
    }
  }

  override def add(prettyId: String, id: String): Future[IdAddResp] = futurePool {
    val resp = ssdbClient.setnx(prettyId, id)
    resp.ok() match {
      case true => resp.asInt() match {
        case 1 => IdAddResp(isOk = true, Option(id))
        case _ => IdAddResp(isOk = false, None)
      }
      case false => throw new Exception("SSDB Client response failure")
    }
  }

  override def update(prettyId: String, id: String): Future[IdUpdateResp] = futurePool {
    ssdbClient.get(prettyId)
  }.map(resp =>
    if (resp.notFound()) {
      IdUpdateResp(isOk = false, None, None)
    } else {
      val addResp = ssdbClient.set(prettyId, id)
      if (addResp.ok()) IdUpdateResp(isOk = true, Option(id), Option(resp.asString()))
      else IdUpdateResp(isOk = false, None, Option(resp.asString()))
    })

  override def checkMulti(ids: Seq[String]): Future[Map[String, IdStatus]] = futurePool {
    ids.map(id => {
      val resp = ssdbClient.exists(id)
      val status = (resp.ok(), resp.asInt()) match {
        case (true, 1) => IdStatus(exist = true, Option(s"${
          id
        }_${
          System.currentTimeMillis()
        }"))
        case (true, 0) => IdStatus(exist = false, None)
        case _ => throw new Exception("SSDB Client response failure")
      }
      (id, status)
    }).toMap
  }

  private[this] def genUniqueId: String = UUID.randomUUID().toString

  override def get(prettyId: String): Future[IdGetResp] = futurePool {
    val resp = ssdbClient.get(prettyId)
    resp.stat match {
      case "not_found" => IdGetResp(exist = false, None)
      case "ok" => IdGetResp(exist = true, Option(resp.asString()))
    }
  }

  // Risk on failure
  override def addMulti(prettyIds: Seq[String]): Future[IdAddResp] = ???

  //    checkMulti(prettyIds).map(exists => {
  //      exists.foldLeft(false)((r, e) => r || e._2.exist) match {
  //        case false => {
  //          //All keys does not exist
  //          val id = genUniqueId
  //          IdAddResp(true, None)
  //        }
  //        case true => IdAddResp(false, None) //Some key exist
  //      }
  //    })

}
