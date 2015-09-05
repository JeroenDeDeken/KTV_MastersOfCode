'use strict';
angular
		.module('newsfeed', [])

		.directive(
				'newsfeedBlock',
				function() {
					return {
						restrict : 'E',
						templateUrl : 'app/shared-components/newsfeed/newsfeed-block.html',
						controller : 'newsfeedBlockController'//,
						//link: function($scope, element, attrs) {
						//	$scope.$on('SocketUpdate', function(event, message){
						//		console.log(message);
						//		//TODO: add item to newsItems
						//	});
						//}
					};
				}).controller('newsfeedBlockController', function ($scope, NewsFactory) {	
					$scope.newsItems = NewsFactory.query();
				})

		.directive(
				'newsfeedItem',
				function() {
					return {
						restrict : 'E',
						templateUrl : 'app/shared-components/newsfeed/newsfeed-item.html'
					};
				})
				
		.factory('NewsFactory', function($rootScope, $websocket) {
				var newsItems = []; 
			    
				function getRootUri() {
					return "ws://"
					+ (document.location.hostname == "" ? "localhost"
							: document.location.hostname) + ":"
					+ (document.location.port == "" ? "8080" : document.location.port);
				}
			
				//Open a websocket connection
			    var websocket = $websocket(getRootUri() + "/newsfeed");
				//var websocket = new WebSocket(getRootUri() + "/newsfeed");
			    
			    websocket.onOpen(function () {
			        console.log("Newsfeed Websocket connected");
			    });
			    
			    websocket.onClose(function (evt) {
			        console.log("Newsfeed Websocket disconnected");
			        websocket.close();
			    });
			    websocket.onMessage(function (evt) {
			        //convert json to javascript object
			        var message = JSON.parse(evt.data);
			        
			        //write message.text to screen
			        var newsItem = {text:message.text, time:message.time};
			        console.log("RECIEVED NEWSFEED MESSAGE: " + newsItem.text + newsItem.time);
			        newsItems.unshift(newsItem);
				    
			        // reverse order and animate
			        var list = $('ul.newsfeed');
			        $(list).find("li").first().hide().slideDown();
			        
			        //$rootScope.$broadcast('SocketUpdate', text);
			    });
			    websocket.onError(function (evt) {
			    	console.log("WEBSOCKET ERROR: " + evt.data);
			    });
			    
			    return {
			        query: function () {
			        	console.log("returning " + newsItems.length + " news items");
			        	return newsItems;
			        }
			    };
		});