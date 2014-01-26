tété.out
========

A Google App Engine web app to record and see the last breastfeeds.

Introduction
------------

I'm a father since 2013-12-31 and my wife breastfeed our baby.
To ease her task I developed this web app.
It was the occasion for me to play with AngularJS, Bootstrap, Yeoman and Google App Engine.

Features
--------

  * See the last 10 breastfeeds
  * Easily see info on the last breastfeed (which breast, since how long, duration)
  * Record new breastfeeds

Technical information
---------------------

This app is design to work on Google App Engine only.<br>
To use it, you must have a registered (free) account at [Google App Engine](https://appengine.google.com/) and an app id ready to be used.<br>
Data is stored using the Google Datastore (BigTable) through the "low level" [Java API](https://developers.google.com/appengine/docs/java/datastore/).<br>
Web frontend is developed with AngularJS.<br>
Server communication is done through REST services. They are supported by Spring MVC Controllers and Jackson JSON serialization.<br>
User authentication uses Google (gmail) accounts. Accounts must be registered developers of the app id. See 'admin' configuration [here](https://developers.google.com/appengine/docs/java/config/webxml#Security_and_Authentication).

Installation
------------

### Prerequisites

You must have a working recent installation of :

  * Maven
  * node.js/npm
  * Yeoman/Bower/Grunt

### Steps :

  1. Clone the repo
  ```
  git clone https://github.com/superbob/tetedotout.git
  ```
  2. Step into web-app subfolder
  ```
  cd tetedotout/web-app
  ```
  3. Build the web front end
  ```
  bower install
  grunt
  ```
  4. Update the application id in `src/main/webapp/WEB-INF/appengine-web.xml` with *<your-own-application-id>*
  5. Build the services and the final package
  ```
  mvn package
  ```
  6. Upload the app to the Google Cloud !
  ```
  mvn appengine:update
  ```
  7. Follow the command line instructions
  8. You are done !
    * You can browse to *<your-own-application-id>*.appspot.com and see the result

Hack
----

  1. Follow the above steps 1 to 5, then :
  2. Run the devserver
  ```
  mvn appengine:devserver
  ```
  3. (Optional) Run the grunt server
  ```
  grunt serve
  ```

If you want to play with REST services you only need the gae devserver (step 2) and browse to [localhost:8080](localhost:8080) to see the app.<br>
The gae devserver is built in with hot deploy, it will update its content each time you 'mvn package'.

If you want to play the web front end (AngularJS), you should use the grunt server and browse to [localhost:9000](localhost:9000) to see the app.
The grunt server is configured with livereload and proxy to gae devserver for REST and user authentication calls.
To ensure authentication is done right when using the grunt server, you should first browse to [localhost:9000/api/tete-log/](localhost:9000/api/tete-log/), make sure your authenticated, and then browse to [localhost:9000](localhost:9000).

TODO
----

Tests, I had to code fast, so there are no test which is really bad.

License
-------

My code is licensed under the Simplified BSD "2-Clause" License.

Credits/Tools used
------------------

Apache Maven
Spring MVC
Google App Engine (Web container + Datastore)
Google AngularJS
node.js
Yeoman
Bower
Grunt
yeoman angular generator
grunt-connect-proxy task
yeoman jangular generator (sample configuration of a Yeoman+Maven project and grunt-connect-proxy sample)
yeoman jhipster generator (same as above)
