# Play! Websocket Test

[Play!](http://www.playframework.com/) demo for Heroku websockets support.

# Running

``` bash
$ play run
```

# Running on Heroku

``` bash
$ heroku create --buildpack https://github.com/jamesward/heroku-buildpack-play.git
$ heroku labs:enable websockets
$ git push heroku master
$ heroku open
```
