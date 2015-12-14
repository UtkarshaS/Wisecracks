/**
 * Created by utkarshashetye on 12/13/15.
 */

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.LazyLogging

object Config extends LazyLogging {

  val env = System.getProperty("ENVIRONMENT", "dev")

  logger.debug(s"Using environment [$env]")
  val conf = ConfigFactory.load

  def apply() = conf.getConfig(env)
  def getConfig(env: String) = conf.getConfig(env)
}