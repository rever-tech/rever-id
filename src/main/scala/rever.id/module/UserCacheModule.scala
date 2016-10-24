package rever.id.module

import javax.inject.Singleton

import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import rever.id.domain.{UserID, UserInfo}
import rever.id.repository.{CacheRepository, OnMemoryCacheRepository}
import rever.id.service.{UserCacheService, UserCacheServiceImpl}

/**
  * Created by SangDang on 9/16/16.
  */
object UserCacheModule extends TwitterModule {
  override def configure: Unit = {
    bind[UserCacheService].to[UserCacheServiceImpl]
  }

  @Singleton
  @Provides
  def providesUserCacheRepository(): CacheRepository[UserID, UserInfo] = {
    new OnMemoryCacheRepository[UserID, UserInfo]()
  }
}
