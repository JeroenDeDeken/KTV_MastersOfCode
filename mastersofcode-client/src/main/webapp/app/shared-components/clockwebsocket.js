angular.module('clock', function() {

})

.factory(
		'Clock',
		function($rootScope, RestFactory) {
			var wsUri = getRootUri() + "/time";
			var status;
			var lasttick = {
				remaining : 0,
				total : 0
			};
			
			var websocket = new WebSocket(wsUri);
			websocket.onmessage = function(evt) {
				onMessage(evt)
			};
		
			function getRootUri() {
				return "ws://"
						+ (document.location.hostname == "" ? "localhost"
								: document.location.hostname)
						+ ":"
						+ (document.location.port == "" ? "8080"
								: document.location.port);
			}

			function onMessage(evt) {
				try {

					var tick = JSON.parse(evt.data);
					var remaining = tick.remaining;
					var total = tick.total;

					// round finished
					if (remaining <= 0 && total == 0) {
						if (status !== 'stopped') {
							status = 'stopped';
							$rootScope.$broadcast('statuschanged', status);
						}
					}
					// round is started
					else if (remaining > 0 && lasttick.remaining > remaining) {
						if (status !== 'inprogress') {
							status = 'inprogress';
							$rootScope.$broadcast('statuschanged', status);
						}
						// round is paused
					} else if (remaining > 0
							&& lasttick.remaining === remaining) {
						if (status !== 'paused') {
							status = 'paused';
							$rootScope.$broadcast('statuschanged', status);
						}
					}
					lasttick = tick;
					
					
				} catch (err) {
					console.log(err);
				}
			}

			return {
				stop : function() {
					return RestFactory.clock("stop");
				},
				pause : function() {
					return RestFactory.clock("pause");
				},
				freeze: function() {
					return RestFactory.clock("freeze");
				},
				setStatus : function(status) {
					status = status;
				},
				getStatus : function() {
					return status;
				}
			}

		});