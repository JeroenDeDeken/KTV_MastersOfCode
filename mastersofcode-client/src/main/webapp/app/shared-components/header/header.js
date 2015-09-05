'use strict';
angular.module('header', []).directive('headerView', function() {
	return {
		restrict : 'E',
		templateUrl : 'app/shared-components/header/header-view.html',
		controller : 'headerViewController'
	};
})

.controller('headerViewController', function($scope, $state, User) {
	$scope.user = User.get();

	$scope.logout = function() {
		$scope.user = User.logout();
		$state.go($state.current, {}, {
			reload : true
		});
		window.location.reload(true);
	};

	$scope.goToDashBoard = function() {
		if (User.isAdmin()) {
			$state.go('admin');
		} else {
			$state.go('dashboard');
		}
	}

	$scope.toggleLanguage = function(language) {
		if (language == "en_EN") {
			window.sessionStorage.language = "en_EN";
			$translate.uses('en_EN')
		} else if (language == "nl_NL") {
			window.sessionStorage.language = "nl_NL";
			$translate.uses('nl_NL')
		}
		window.location.reload(true);
	}

});