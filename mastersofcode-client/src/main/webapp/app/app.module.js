'use strict';

// Declare app level module which depends on views, and components
var app = angular.module('MoC', [
	'ui.router',
	'ngResource',
	'ngWebSocket',
	'header',
	'participant-dashboard',
	'public-dashboard',
	'editor',
	'angucomplete', 
	'admin-dashboard', 	
	'competition',
	'competition-menu',
	'competition-assignments',
	'competition-hints',
	'competition-rounds',
	'competition-participants',
	'competition-teams',
	'newsfeed',
	'score-block',
	'restresource',
	'ngDialog',
	'translator',
	'clock',
	'node-view',
]); 

// App Config
app.config(function ($stateProvider, $httpProvider, ngDialogProvider) {
	
	ngDialogProvider.setDefaults({
        className: 'ngdialog-theme-default',
        plain: true,
        showClose: true,
        closeByDocument: true,
        closeByEscape: true
    });
	
	 $httpProvider.defaults.headers["delete"] = {
			    'Content-Type': 'application/json;charset=utf-8'
			  };
	
	// Stateprovider handles the routing information with states.
	$stateProvider	
    // Participant dashboard - root
    .state('dashboard', {
        url: '',
        templateUrl: 'app/participant-components/dashboard/participant-dashboard-view.html',
        controller: 'DashboardController',
        data: { restrict: "user" }
    })
    // Public client - root
    .state('public', {
        url: '/public',
        templateUrl: 'app/public-components/dashboard/public-dashboard-view.html',
        controller: 'DashboardController',
        data: { restrict: "guest" }
    })
    // Editor
    .state('editor', {
        url: '/editor',
        templateUrl: 'app/participant-components/editor/editor-view.html',
        controller: 'EditorController',
        data: { restrict: "user" }
    })
    // Scores
    .state('scores', {
        url: '/scores',
        templateUrl: 'app/participant-components/scores/scores-view.html',
        controller: 'scoreBlockController',
        data: { restrict: "user" }
    })
    // Admin
    .state('admin', {
		url: '/admin',
		templateUrl: 'app/admin-components/dashboard/admin-dashboard-view.html',
		controller: 'AdminController',
        data: { restrict: "admin" }
	})
	// Admin - Nodes
    .state('nodes', {
		url: '/admin/nodes',
		templateUrl: 'app/admin-components/node/node-view.html',
		controller: 'NodeController',
        data: { restrict: "admin" }
	})
	// Admin - Nodes - Add
    .state('addnode', {
		url: '/admin/nodes/add',
		templateUrl: 'app/admin-components/node/node-add-view.html',
		controller: 'NodeController',
        data: { restrict: "admin" }
	})
	// Admin - Nodes - Edit
    .state('editnode', {
		url: '/admin/nodes/:id',
		templateUrl: 'app/admin-components/node/node-add-view.html',
		controller: 'NodeController',
        data: { restrict: "admin" }
	})
	// Admin - Competition
	.state('competition', {
		url: '/admin/competition/:id',
		templateUrl: 'app/admin-components/competition/competition-view.html',
		controller: 'CompetitionController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Add
	.state('addcompetition', {
		url: '/admin/createcompetition',
		templateUrl: 'app/admin-components/competition/competition-add-view.html',
		controller: 'AdminController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Edit
	.state('editcompetition', {
		url: '/admin/competition/:id/edit',
		templateUrl: 'app/admin-components/competition/competition-add-view.html',
		controller: 'CompetitionController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Assignments
	.state('assignments', {
		url: '/admin/competition/:id/assignments',
		templateUrl: 'app/admin-components/assignment/assignment-view.html',
		controller: 'AssignmentController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Assignments - Add
	.state('addassignment', {
		url: '/admin/competition/:id/assignments/add',
		templateUrl: 'app/admin-components/assignment/assignment-add-view.html',
		controller: 'AssignmentController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Assignments - Edit
	.state('editassignment', {
		url: '/admin/competition/:id/assignments/:assid',
		templateUrl: 'app/admin-components/assignment/assignment-edit-view.html',
		controller: 'AssignmentController',
        data: { restrict: "admin" }
	})
	
	// Admin - Competition - Hints
	.state('hints', {
		url: '/admin/competition/:id/hints',
		templateUrl: 'app/admin-components/hint/hint-view.html',
		controller: 'HintController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Hints - Add
	.state('addhint', {
		url: '/admin/competition/:id/hints/add',
		templateUrl: 'app/admin-components/hint/hint-add-view.html',
		controller: 'HintController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Hints - Edit
	.state('edithint', {
		url: '/admin/competition/:id/hints/:hintid',
		templateUrl: 'app/admin-components/hint/hint-add-view.html',
		controller: 'HintController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Rounds
	.state('rounds', {
		url: '/admin/competition/:id/rounds',
		templateUrl: 'app/admin-components/round/round-view.html',
		controller: 'RoundController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Rounds - Add
	.state('addround', {
		url: '/admin/competition/:id/rounds/add',
		templateUrl: 'app/admin-components/round/round-add-view.html',
		controller: 'RoundController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Rounds - Edit
	.state('editround', {
		url: '/admin/competition/:id/rounds/:roundid',
		templateUrl: 'app/admin-components/round/round-add-view.html',
		controller: 'RoundController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Participants
	.state('participants', {
		url: '/admin/competition/:id/participants',
		templateUrl: 'app/admin-components/participant/participant-view.html',
		controller: 'ParticipantController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Teams
	.state('teams', {
		url: '/admin/competition/:id/teams',
		templateUrl: 'app/admin-components/team/team-view.html',
		controller: 'TeamController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Teams - Add
	.state('addteam', {
		url: '/admin/competition/:id/teams/add',
		templateUrl: 'app/admin-components/team/team-add-view.html',
		controller: 'TeamController',
        data: { restrict: "admin" }
	})
	// Admin - Competition - Teams - Edit
	.state('editteam', {
		url: '/admin/competition/:id/teams/:teamid',
		templateUrl: 'app/admin-components/team/team-edit-view.html',
		controller: 'TeamController',
        data: { restrict: "admin" }
	});
	
	
});
app.run(function($rootScope, $state, User){
	// When the state is changed, write the change to console for debugging
	// purposes.
    $rootScope.$on('$stateChangeSuccess',function(event, toState, toParams, fromState, fromParams){
        console.log('Transitioned from: ' + fromState.name + ' To: ' + toState.name);
        if (!angular.isUndefined(toState)) {
        	 window.sessionStorage.state = toState.name;   
        }          
    });
           
    // When a state change is requested, check a few things before moving on to
	// the next state.
    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){    	
    	// Check if the user has admin privileges when a restricted page is
		// requested.
        if ( toState.data.restrict === 'admin' && !User.isAdmin() ) {
            event.preventDefault();
            return false;
        }  else if ( toState.data.restrict === 'user' && User.isGuest()) {
        	event.preventDefault();
            return false;
        }
      }); 
    
    // function to redirect the user to the last state, or to the dashboard.
    function redirect() {
    	if (angular.isUndefined(window.sessionStorage.state)) {
    		if (User.isAdmin()) 	$state.go('admin');
    		else if(User.isGuest()) $state.go('public');
    		else 					$state.go('dashboard');
    	}
    	else {
    		if (User.isAdmin()) 	$state.go('admin');
    		else if(User.isGuest()) $state.go('public');
    		else 					$state.go(window.sessionStorage.state);
    		   		
    	}
    }
    
    // get the logged in user
    User.login().$promise.then(function (result) {
		User.set(result);
		console.log("logged in as: " + result.username);
		redirect();
		},    		
    function(error) {
    	console.log(error);
    }); 
    
    
});


// Factory to maintain the session user.
app.factory('User', function(RestFactory) {
	var user = RestFactory.currentuser.get();
	
	return {
		login: function() {
			return RestFactory.currentuser.get();
		},
		get: function() {
			return user;
		},
		set: function(_user) {
			user = _user;
		},
		logout: function() {
			RestFactory.security.logout();
		},
		isAdmin: function() {
			return user.role === "admin";
		},
		isGuest: function() {
			return user.role === "guest";
		},
		isLoggedIn: function() {
			return user === {};
		} 
	}
	
});

app.filter('secondsToDateTime', function() {
    return function(seconds) {
        var d = new Date(0,0,0,0,0,0,0);
        d.setSeconds(seconds);
        return d;
    };
});