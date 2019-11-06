/*
 * Copyright 2016 The CHOReVOLUTION project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

angular.module('self')
        .directive('choreographies', function () {
          return {
            restrict: 'E',
            templateUrl: 'views/choreographies.html',
            scope: {
              dynamicForm: "=form",
              user: "="
            },
            controller: function ($scope, $filter) {
              $scope.init = function () {
                if (!$scope.user.memberships) {
                  $scope.user.memberships = new Array();
                }
                $scope.choreographyDisabled = false;
              };

              $scope.addChoreography = function (item, model) {
                var choreography = item;
                $scope.user.memberships.push({"rightKey": choreography.rightKey, "groupName": choreography.groupName});
                $scope.$emit("groupAdded", choreography.groupName);
              };

              $scope.removeChoreography = function (item, model) {
                var choreographyIndex = $scope.getIndex(item);
                var choreography = $scope.user.memberships[choreographyIndex];
                var choreographyName = choreography.groupName;
                $scope.user.memberships.splice(choreographyIndex, 1);
                $scope.$emit("groupRemoved", choreographyName);
              };

              $scope.getIndex = function (selectedChoreography) {
                var choreographyIndex = $scope.user.memberships.map(function (membership) {
                  return membership.groupName;
                }).indexOf(selectedChoreography.groupName);
                return choreographyIndex;
              };
            }
          };
        });
