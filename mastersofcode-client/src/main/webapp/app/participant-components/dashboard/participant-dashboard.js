'use strict';


angular.module('participant-dashboard', [])
.filter('orderObjectBy', function() {
		  return function(items, field, reverse) {
		    var filtered = [];
		    angular.forEach(items, function(item) {
		      filtered.push(item);
		    });
		    filtered.sort(function (a, b) {
		      return (a[field] > b[field] ? 1 : -1);
		    });
		    if(reverse) filtered.reverse();
		    return filtered;
		  };
		})
.controller('DashboardController', function ($scope, $state, Competition, ngDialog, Clock) {
	//get active round
	Competition.getActiveRound().$promise.then(function(result){
		$scope.round = result;
		Competition.setActiveRound(result);
	}, function(error) {
		console.log(error);
	});
	
	//get active competition
	Competition.getActive().$promise.then(function(result){
		$scope.competition = result;
		Competition.setActiveCompetition(result);
	}, function(error) {
		console.log(error);
	});
	
	//listener for broadcast message, received when the websocket 'clock' status is changed.
	$scope.$on('statuschanged', function(event, data) {
		console.log('dashboard status changed to ' + data)
		$scope.$apply(function() {
			$scope.competition.status = data;
		})	
		
		//get active round
		Competition.getActiveRound().$promise.then(function(result){
			$scope.round = result;
			Competition.setActiveRound(result);
		}, function(error) {
			console.log(error);
		});
	});		
	
	//called when the user clicks on a round
	$scope.clickRound = function(_round) {	
		if (Competition.canUserParticipate()) {
			if(Competition.isActiveRound(_round)) {
				$state.go('editor');
			} else {
				ngDialog.open({template: '\<p>Round ' + $scope.competition.rounds.indexOf(_round) + ' has not yet started.'})
			}
		} else {
			ngDialog.open({template: '\<p>Your team has not registered for this competition.'})
		}
		
	};
	
	//check if a round is active
	$scope.isActiveRound = function(_round) {
		return Competition.isActiveRound(_round);
	}
		
	function isCompetitionActive() {
		return angular.equals({}, Competition.getActive());
	}
	

})

//Factory to manage the current competition
.factory('Competition', function(RestFactory, Clock, User) {
	var competition = {};
	var round = {};
	
	return {
		getActive: function() {
			competition = RestFactory.activecompetition.get();
			return competition;
		},		
		getActiveRound: function() {
			round = RestFactory.activeround.get();
			return round;
		},		
		setActiveRound: function(_round) {
			round = _round;
		},
		setActiveCompetition: function(_competition) {
			competition = _competition;
		},
		isActiveRound : function(_round) {
			if (Clock.getStatus() === 'inprogress' || Clock.getStatus() === 'paused') {
				return angular.equals(_round.id, round.id);
			} else {
				return false;
			}	
		},
		canUserParticipate: function() {
			var user = User.get();
			var can = false;	
			angular.forEach(competition.teams, function(value, key) {
				
				if (value.id === user.id) {
					console.log('yes' + value.id + ':' +  user.id);
					can = true;
				}
				
			});
			return can;
		}
	}
});


