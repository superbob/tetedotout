'use strict';

/* TODO use a service instead */
/* global moment */
/* global $ */

angular.module('yoApp')
  .controller('MainCtrl', function ($scope, $resource) {
    var TeteLog = $resource('/api/tete-log/:logId', {logId: '@id'});

    var refreshTeteLogList = function () {
      var teteLogs = TeteLog.query({'q': 'last10'}, function () {
        $scope.tetelogs = teteLogs;

        if (typeof teteLogs !== undefined) {
          if (typeof teteLogs[0] !== undefined) {
            $scope.lastTeteLog = teteLogs[0];
          }
        }
      });
    };

    var refreshRecordLogDuration = function () {
      var formData = $scope.recordLog;
      if (typeof formData !== undefined) {
        if (formData.beginDate && formData.beginTime && formData.endDate && formData.endTime) {
          var beginMoment = moment(formData.beginDate + 'T' + formData.beginTime);
          var endMoment = moment(formData.endDate + 'T' + formData.endTime);
          var durationMinutes = endMoment.diff(beginMoment, 'minutes');
          $scope.recordLog.durationMinutes = durationMinutes;
        }
      }
    };

    $scope.$watch('recordLog.beginDate', refreshRecordLogDuration);
    $scope.$watch('recordLog.beginTime', refreshRecordLogDuration);
    $scope.$watch('recordLog.endDate', refreshRecordLogDuration);
    $scope.$watch('recordLog.endTime', refreshRecordLogDuration);

    refreshTeteLogList();

    $scope.breastInfo = function (mainLeft, someOther) {
      var message;
      if (mainLeft) {
        message = 'Gauche';
      } else {
        message = 'Droit';
      }

      if (someOther) {
        if (mainLeft) {
          message += ' (+ Droit)';
        } else {
          message += ' (+ Gauche)';
        }
      }

      return message;
    };

    $scope.submit = function () {
      var modelToStore = prepareModel($scope.recordLog);
      var teteLog = new TeteLog(modelToStore);
      teteLog.$save({}, function () {
        $('#recordLogModal').modal('hide');
        refreshTeteLogList();
      });
    };

    var prepareModel = function (formData) {
      var beginMoment = moment(formData.beginDate + 'T' + formData.beginTime);
      var endMoment = moment(formData.endDate + 'T' + formData.endTime);

      var targetModel = {
        'begin': beginMoment.toISOString(),
        'end': endMoment.toISOString(),
        'durationSeconds': formData.durationMinutes * 60,
        'mainLeft': (formData.mainLeft === 'true'),
        'someOther': formData.someOther
      };

      return targetModel;
    };

    $scope.moment = moment;
  });
