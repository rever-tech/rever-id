package rever.id

import com.twitter.util.FuturePool

/**
  * Created by tiennt4 on 26/10/2016.
  */
package object service {
  lazy val futurePool = FuturePool.unboundedPool
}
