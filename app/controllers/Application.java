package controllers;

import play.*;
import play.mvc.*;
import play.libs.*;

import views.html.*;

import akka.actor.*;
import akka.japi.Creator;
import scala.concurrent.duration.Duration;
import static java.util.concurrent.TimeUnit.*;

import models.*;

public class Application extends Controller {
    public static WebSocket<String> pingWs() {
        return new WebSocket<String>() {
            Cancellable cancellable;

            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
                ActorRef pingActor = Akka.system().actorOf(Props.create(Pinger.class, in, out));
                cancellable =
                    Akka.system().scheduler().schedule(Duration.create(1, SECONDS),
                                                       Duration.create(1, SECONDS),
                                                       pingActor,
                                                       "Tick",
                                                       Akka.system().dispatcher(),
                                                       null
                                                       );
            }

            public void onClose() {
                cancellable.cancel();
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
