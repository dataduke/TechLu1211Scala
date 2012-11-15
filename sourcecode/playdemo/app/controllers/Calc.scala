package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.ws.WS
import play.api.Logger

object Calc extends Controller {

  val calcUrl = "http://localhost:8123"

  def add(x: Int, y: Int) = Action {
    Logger.info("Add %d and %d".format(x, y))
    val result = WS.url(calcUrl).post(generateRequest(x, y)).value.get.xml
    Ok("Ergebnis: " + (result \\ "Result").text)
  }

  def generateRequest(x: Int, y: Int) =
    <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
      <soap:Body>
        <calc:add xmlns:calc="http://www.parasoft.com/wsdl/calculator/">
          <calc:x>{x}</calc:x>
          <calc:y>{y}</calc:y>
        </calc:add>
      </soap:Body>
    </soap:Envelope>

}
