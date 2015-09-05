'use strict';
angular
		.module('competition-menu', [])

		.directive(
				'competitionMenu',
				function() {
					return {
						restrict : 'E',
						templateUrl : 'app/shared-components/competition-menu/competition-menu.html',
						controller : 'competitionMenuController'
					};
				}).controller('competitionMenuController',
				function($scope, $rootScope, $state, CompetitionFactory) {
					var competition = CompetitionFactory.get();

					// this function is called when a tab is selected, and sets
					// it.
					$scope.selectItem = function(_item) {
						$scope.active = _item;
					};
					// checks if the given item is active.
					$scope.isActive = function(_item) {
						return $scope.active === _item;
					};

					$scope.checkActiveState = function() {
						switch ($state.current.name) {
						case "competition":
							return 0;
						case "assignments":
							return 1;
						case "hints":
							return 2;
						case "rounds":
							return 3;
						case "participants":
							return 4;
						case "teams":
							return 5;
						}
					}

					$scope.active = $scope.checkActiveState();

					$scope.$watch('active', function() {
						switch ($scope.active) {
						case 0:
							$state.go('competition', {
								id : competition.id
							});
							break;
						case 1:
							$state.go('assignments', {
								id : competition.id
							});
							break;
						case 2:
							$state.go('hints', {
								id : competition.id
							});
							break;
						case 3:
							$state.go('rounds', {
								id : competition.id
							});
							break;
						case 4:
							$state.go('participants', {
								id : competition.id
							});
							break;
						case 5:
							$state.go('teams', {
								id : competition.id
							});
							break;
						}
					});
				});