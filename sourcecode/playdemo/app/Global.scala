import play.api.{Logger, Application, GlobalSettings}

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("The application is starting now!")
    super.onStart(app)
  }

}
