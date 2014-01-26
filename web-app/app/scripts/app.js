'use strict';

/* TODO use a service instead */
/* global moment */

angular.module('yoApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute'
])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });

var browserLanguage = window.navigator.userLanguage || window.navigator.language;
moment.lang(browserLanguage);
