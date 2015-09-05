'use strict';
angular
		.module('score-block', [])
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
		.directive(
				'scoreBlock',
				function() {
					return {
						restrict : 'E',
						templateUrl : 'app/shared-components/score-block/score-block.html',
						controller : 'scoreBlockController'
					};
				})
		.controller(
				'scoreBlockController',
				function($scope, $state, ScoreFactory, Competition, RestFactory) {
					Competition.getActive().$promise.then(function(result) {
						$scope.competition = result;
						ScoreFactory.setCompetition(result);

					}, function(error) {
						console.log(error);
					});

					RestFactory.scores.query().$promise.then(
							function(result) {
								ScoreFactory.setScores(result);
								$scope.getScorePerRound = function(_team,
										_round) {
									return ScoreFactory.getScorePerRound(_team, _round);
								}
								$scope.getScoreTotal = function(_team) {
									return ScoreFactory.getScoreTotal(_team);
								}
								
							}, function(error) {
								console.log(error);
							});
					
					
					
							
				})

		.factory(
				'ScoreFactory',
				function(RestFactory) {
					var scores = [];
					var competition = {};

					return {
						getScorePerRound : function(_team, _round) {							
							for (var i = 0; i < scores.length; i++) {
								if (scores[i].user.id === _team.id && scores[i].round.id === _round.id) {
									console.log("Total score for " + _team.id + " in round " +_round.id +  ": "+ scores[i].score);
									return scores[i].score;
								}
							}
						},
						getScoreTotal : function(_team) {
							var sum = 0;
							for (var i = 0; i < competition.rounds.length; i++) {
								for (var u = 0; u < scores.length; u++) {
									if (scores[u].user.id === _team.id) {
										sum += scores[u].score;	
									}
								}
							}
						
							
							
							var max = 0;
							$(".teamrow").each( function(index) {
								
								var scores = new Array();
								var rounds = 0;
								var total = 0;
								
								$(this).find(".score-bar").each( function() {
									
									var score = $(this).attr('id');
									if(score != '') {
										total += parseInt(score);
										scores.push(score);
									}
									rounds++;
								});
								
								$(this).find(".score-bar").each( function(index) {
									
									var score = $(this).attr('id');
									if(score != '') {
										
										var percentage = 0;
										if(max > 0) percentage = 100 * ( score / max );
										else {
											percentage = 100 * ( score / total );
											max = total;
										}
										
										if(percentage > 0 && $(this).width() < percentage){
											$(this).stop().animate({ width: percentage+"%" }, "fast"); 	
										}
										
									}
									else $(this).remove();	
								});
								
							});
						
							
							return sum;
						},
						setCompetition : function(_comp) {
							competition = _comp;
						},
						setScores : function(_scores) {
							scores = _scores;
						},
						getCompetition : function() {
							return competition;
						},
						getScores : function() {
							return scores;
						}
					}

				});