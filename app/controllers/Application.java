package controllers;

import static java.util.concurrent.TimeUnit.SECONDS;
import models.Pinger;
import play.libs.Akka;
import play.libs.F.Callback0;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.concurrent.duration.Duration;
import views.html.index;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;

public class Application extends Controller {
    public static WebSocket<String> pingWs() {
        return new WebSocket<String>() {
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
                final ActorRef pingActor = Akka.system().actorOf(Props.create(Pinger.class, in, out));
                final Cancellable cancellable = Akka.system().scheduler().schedule(Duration.create(1, SECONDS),
                                                   Duration.create(1, SECONDS),
                                                   pingActor,
                                                   "Tick",
                                                   Akka.system().dispatcher(),
                                                   null
                                                   );
                
                in.onClose(new Callback0() {
                	@Override
                	public void invoke() throws Throwable {
                		cancellable.cancel();
                	}
                });
            }

        };
    }

    public static Result pingJs() {
        return ok(views.js.ping.render());
    }

    public static Result index() {
        return ok(index.render());
    }
}
