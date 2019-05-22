package courses.config

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait DatabaseConfig {
  protected val dbProfile = DatabaseConfig.forConfig[JdbcProfile]("database")
  val db = dbProfile.db
}
