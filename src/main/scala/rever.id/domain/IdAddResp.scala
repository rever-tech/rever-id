package rever.id.domain

/**
  * Created by tiennt4 on 25/10/2016.
  */
case class IdAddResp(isOk: Boolean, id: Option[String])

case class IdUpdateResp(isOk: Boolean, id: Option[String], oldId: Option[String])