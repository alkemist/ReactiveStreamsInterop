package com.rolandkuhn.rsinterop

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import ratpack.func.Action
import ratpack.handling.{Context, Handler}
import ratpack.http.ResponseChunks
import ratpack.test.embed.EmbeddedApp
import ratpack.test.http.TestHttpClient
import reactor.rx.Streams
import rx.{Observable, RxReactiveStreams}

import scala.collection.JavaConverters._

object ScalaMain extends App {
  val system = ActorSystem("InteropTest")
  implicit val mat = FlowMaterializer()(system)

  EmbeddedApp.fromHandler(new Handler {
    override def handle(ctx: Context): Unit = {
      // RxJava Observable
      val intObs = Observable.from((1 to 10).asJava)
      // Reactive Streams Publisher
      val intPub = RxReactiveStreams.toPublisher(intObs)
      // Akka Streams Source
      val stringSource = Source(intPub).map(_.toString)
      // Reactive Streams Publisher
      val stringPub = stringSource.runWith(Sink.fanoutPublisher(1, 1))
      // Reactor Stream
      val linesStream = Streams.create(stringPub).map[String](new reactor.function.Function[String, String] {
        override def apply(in: String) = in + "\n"
      })
      // and now render the HTTP response
      ctx.render(ResponseChunks.stringChunks(linesStream))
    }
  }).test(new Action[TestHttpClient] {
    override def execute(client: TestHttpClient): Unit = {
      println(client.getText())
    }
  })
}