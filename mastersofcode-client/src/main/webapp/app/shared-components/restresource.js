angular.module('restresource', [])

.factory('RestFactory', function($resource, $location, $http) {
	var path = "http://" + $location.host() + ":" + $location.port() + "/api/";

	return {
		security : $resource(path + "security", {

		}, {
			'logout' : {
				method : 'GET'
			}
		}),
		users : $resource(path + "users", {
			
		}, {
			'query' : {
				method : 'GET',
				isArray : true
			},
			'get' : {
				method : 'GET'
			},
			'set' : {
				method : 'POST'
			},
			'update' : {
				method : 'PUT'
			}
		}),
		deleteuser : $resource(path + "users/:id", {
			id: '@id'
		}, {
			'remove' : {
				method : 'DELETE',
			}
		}),
		currentuser : $resource(path + "users/current", {
		}, {
			'get' : {
				method : 'GET'
			}
		}),
		assignment : $resource(path + "assignments", {}, {
			'query' : {
				method : 'GET',
				isArray : true
			},
			'get' : {
				method : 'GET'
			},
			'set' : {
				method : 'POST',
				headers : {
					'Content-Type' : 'application/json'
				}
			},
			'update' : {
				method : 'PUT',
				headers : {
					'Content-Type' : 'application/json'
				}
			},
		}),
		competition : $resource(path + "competitions", {
			
		}, {
			'query' : {
				method : 'GET',
				isArray : true
			},
			'get' : {
				method : 'GET'
			},
			'set' : {
				method : 'POST'
			},
			'update' : {
				method : 'PUT'
			}
		}),
		deletecompetition : $resource(path + "competitions/:id", {
			id: '@id'
		}, {
			'remove' : {
				method : 'DELETE',
			}
		}),
		activecompetition : $resource(path + "competitions/current", {
			id : '@id'
		}, {
			'get' : {
				method : 'GET'
			},
			'set' : {
				method : 'POST',
				headers : {
					'Content-Type' : 'text/plain'
				}
			}
		}),
		stopcompetition : $resource(path + "competitions/current/stop", {}, {
			'get' : {
				method : 'GET',
			}
		}),
		rounds : $resource(path + "rounds", {
		}, {
			'query' : {
				method : 'GET',
				isArray : true
			},
			'get' : {
				method : 'GET'
			},
			'set' : {
				method : 'POST'
			},
			'update' : {
				method : 'PUT'
			}
		}),
		deleteround : $resource(path + "rounds/:id", {
			id: '@id'
		}, {
			'remove' : {
				method : 'DELETE',
			}
		}),
		activeround : $resource(path + "rounds/current", {}, {
			'get' : {
				method : 'GET'
			},
			'set' : {
				method : 'POST',
				headers : {
					'Content-Type' : 'text/plain'
				}
			}
		}),
		node : $resource(path + "nodes/:id", {
			id : '@id'
		}, {
			'query' : {
				method : 'GET',
				isArray : true
			},
			'get' : {
				method : 'GET'
			},
			'set' : {
				method : 'POST'
			},
			'update' : {
				method : 'PUT'
			},
			'remove' : {
				method : 'DELETE'
			},
		}),
		scores : $resource(path + "scores", {
			id : '@id'
		}, {
			'query' : {
				method : 'GET',
				isArray : true
			}
		}),
		clock : function(command) {
			return $http.get(path + "time/" + command);
		}
	}
});
