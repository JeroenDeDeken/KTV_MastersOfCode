'use strict';

// Module
var mod_editor = angular.module('editor', [ 'header', 'ngWebSocket' ]);

mod_editor.filter('secondsToDateTime', [ function() {
	return function(seconds) {
		return new Date(1970, 0, 1).setSeconds(seconds);
	};
} ])

mod_editor
		.factory(
				'ATP',
				function($websocket) {

					var wsUri = getRootUri() + "/editor";

					function getRootUri() {
						return "ws://"
								+ (document.location.hostname == "" ? "localhost"
										: document.location.hostname)
								+ ":"
								+ (document.location.port == "" ? "8080"
										: document.location.port);
					}

					// Open a WebSocket connection
					var ws = $websocket(wsUri);

					var atp = [];

					// Buffers
					var buffers = {};
					var tests = "";

					// Open Buffer
					function openBuffer(id, name, text, mode, writable) {
						buffers[id] = CodeMirror.Doc(text, mode);
						var opt = document.createElement("option");
						opt.appendChild(document.createTextNode(id));
						$(".files").append(
								"<li id='" + id
										+ "'><i class='fa fa-file-code-o'></i>"
										+ name
										+ "<i class='fa fa-lock'></i></li>");
						if (!writable) {
							$("#" + id).addClass("readOnly");
						}
					}

					// Select Buffer
					function selectBuffer(editor, name) {
						var buf = buffers[name];
						if (buf.getEditor())
							buf = buf.linkedDoc({
								sharedHist : true
							});
						var old = editor.swapDoc(buf);
						var linked = old.iterLinkedDocs(function(doc) {
							linked = doc;
						});
						if (linked) {
							for ( var name in buffers)
								if (buffers[name] == old)
									buffers[name] = linked;
							old.unlinkDoc(linked);
						}
						editor.focus();
					}

					// Node Content
					function nodeContent(id) {

						// Get Node
						var node = document.getElementById(id), val = node.textContent
								|| node.innerText;
						val = val.slice(val.match(/^\s*/)[0].length, val.length
								- val.match(/\s*$/)[0].length)
								+ "\n";
						return val

					}

					// Java Editor
					var javaEditor;

					// Java Editor - Hint
					CodeMirror.commands.autocomplete = function(cm) {
						cm.showHint({
							hint : CodeMirror.hint.anyword
						});
					}

					// Editor - Get Range
					function getSelectedRange() {
						return {
							from : javaEditor.getCursor(true),
							to : javaEditor.getCursor(false)
						};
					}

					// Editor - Format Selection
					function autoFormatSelection() {
						var range = getSelectedRange();
						javaEditor.autoFormatRange(range.from, range.to);
					}

					// Add Output
					function addOutput(string) {
						if ($('#build-output').val() == '') {
							$('#build-output').val(string);
						} else {
							$('#build-output').val(
									$('#build-output').val() + "\n" + string);
						}
						var psconsole = $('#build-output');
						if (psconsole.length)
							psconsole.scrollTop(psconsole[0].scrollHeight
									- psconsole.height());
					}

					// WebSocket - Response
					ws
							.onMessage(function(event) {
								var response;
								try {
									response = angular.fromJson(event.data);
									console.log('Response: ', response);
									switch (response.type) {
									case 'SAVE':
										if (!response.successful) {
											addOutput("[ERROR] Status: Failed");
										} else {
											addOutput("[INFO] Status: Success");
										}
										break;
									case 'SUBMIT':
										if (!response.successful) {
											addOutput("[ERROR] Status: Failed");
										} else {
											addOutput("[INFO] Status: Success");
										}
										break;
									case 'TEST':
										if (!response.successful) {
											addOutput("[ERROR] Status Failed");
											addOutput("[ERROR] Time: "
													+ response.time);
										} else {
											addOutput("[INFO] Status: Success");
											addOutput("[INFO] Time: "
													+ response.time);
										}
										break;
									case 'COMPILE':
										if (!response.successful) {
											addOutput("[ERROR] Status: Failed");
											addOutput("[ERROR] Time: "
													+ response.time);
										} else {
											addOutput("[INFO] Status: Success");
											addOutput("[INFO] Time: "
													+ response.time);
										}
										break;
									case 'HINT':
										angular
												.forEach(
														response.hints,
														function(hint) {
															$(".hints")
																	.append(
																			"<li id='"
																					+ hint.id
																					+ "'><i class='fa fa-question-circle'></i>"
																					+ hint.text
																					+ "</li>");
															$("#" + hint.id)
																	.hide()
																	.slideDown();
															var notification = new Audio(
																	'assets/sound/notification.mp3');
															notification.play();
														});
										break;
									case 'GET':
										console.log("Response: GET");

										javaEditor = CodeMirror
												.fromTextArea(
														document
																.getElementById("code-editor"),
														{
															lineNumbers : true,
															matchBrackets : true,
															styleActiveLine : true,
															autoCloseBrackets : true,
															indentWithTabs : true,
															scrollbarStyle : "simple",
															highlightSelectionMatches : {
																showToken : /\w/
															},
															extraKeys : {
																"F11" : function(
																		cm) {
																	cm
																			.setOption(
																					"fullScreen",
																					!cm
																							.getOption("fullScreen"));
																},
																"Esc" : function(
																		cm) {
																	if (cm
																			.getOption("fullScreen"))
																		cm
																				.setOption(
																						"fullScreen",
																						false);
																},
																"Alt-Shift-F" : function(
																		cm) {
																	autoFormatSelection();
																},
																"Ctrl-Space" : "autocomplete"
															},
															mode : "text/x-java"
														});

										// Select - Default Theme
										javaEditor.setOption("theme", "neat");

										var selectedBuff = false;

										// Response Files
										angular
												.forEach(
														response.files,
														function(file) {

															// HTML Append
															$("#loaded_files")
																	.append(
																			'<div id="'
																					+ file.filename
																					+ '" style="display:none;">'
																					+ file.content
																					+ '</div>');

															// Select Buffer
															if (!selectedBuff) {
																selectedBuff = file.filename;
																$(
																		"#selectedFile")
																		.html(
																				file.filename
																						+ '.java');
															}

															// Add Buffers
															openBuffer(
																	file.filename,
																	file.filename
																			+ ".java",
																	nodeContent(file.filename),
																	"text/x-java",
																	file.writable);

														});

										// Response Tests
										angular
												.forEach(
														response.testCases,
														function(test) {

															// HTML Append
															$("#loaded_tests")
																	.append(
																			'<div id="filetest_'
																					+ test.name
																					+ '" style="display:none;">'
																					+ test.description
																					+ '</div>');
															$(".tests")
																	.append(
																			"<li id='"
																					+ test.name
																					+ "'><i class='fa fa-file-text-o'></i>"
																					+ test.name
																					+ "</li>");

														});

										// Select File - Source
										$(".files li")
												.click(
														function() {

															// Bread Crumbs
															$("#selectedFile")
																	.html(
																			$(
																					this)
																					.attr(
																							"id")
																					+ '.java');

															// Menu
															$(
																	'.files li.active')
																	.removeClass(
																			'active');
															$(
																	'.tests li.active')
																	.removeClass(
																			'active');
															$(".active")
																	.removeClass(
																			"active");
															$(this).addClass(
																	"active");

															// Toggle
															$(
																	'.code-editor, .footer, .outputs')
																	.show();
															$(
																	'#code-marker, #assignment')
																	.hide();

															// Read Only
															if ($(this)
																	.hasClass(
																			"readOnly")) {
																javaEditor
																		.setOption(
																				"readOnly",
																				true);
																$(
																		'.code-editor')
																		.addClass(
																				"readOnly");
																$(
																		'#editorCodeFormat')
																		.addClass(
																				'disabled');
															} else {
																javaEditor
																		.setOption(
																				"readOnly",
																				false);
																$(
																		'.code-editor')
																		.removeClass(
																				"readOnly");
																$(
																		'#editorCodeFormat')
																		.removeClass(
																				'disabled');
															}

															// Buffer
															selectBuffer(
																	javaEditor,
																	$(this)
																			.attr(
																					"id"));

															// Button
															$('#test')
																	.html(
																			'<i class="fa fa-flask"></i>Test all');
															// $('#editorCodeFormat').removeClass('disabled');
															tests = "";

														});

										$(".assignments li").click();
										$("#selectedFile").html("Assignment");

										// Select Test - Source
										$(".tests li")
												.click(
														function() {

															// Bread Crumbs
															$("#selectedFile")
																	.html(
																			$(
																					this)
																					.attr(
																							"id"));

															// Menu
															$(
																	'.files li.active')
																	.removeClass(
																			'active');
															$(
																	'.tests li.active')
																	.removeClass(
																			'active');
															$(".active")
																	.removeClass(
																			"active");
															$(this).addClass(
																	"active");

															// Toggle
															$(
																	'.code-editor, #assignment')
																	.hide();
															$(
																	'#code-marker, .footer, .outputs')
																	.show();

															// Markdown
															document
																	.getElementById('code-marker').innerHTML = marked($(
																	'#filetest_'
																			+ $(
																					this)
																					.attr(
																							"id"))
																	.html());

															// Button
															$('#test')
																	.html(
																			'<i class="fa fa-flask"></i>Test this');
															$(
																	'#editorCodeFormat')
																	.addClass(
																			'disabled');
															tests = $(this)
																	.attr("id");

														});

										// Select Buff
										selectBuffer(javaEditor, selectedBuff);

										break;
									}
									if (response.consoleOutput) {
										addOutput(response.consoleOutput);
									}
								} catch (e) {
									console.log('Error: ', e);
									response = {
										'Error' : e
									};
								}
							});
					ws.onError(function(event) {
						console.log('Connection Error', event);
					});
					ws.onClose(function(event) {
						console.log('Connection Closed', event);
					});
					ws.onOpen(function() {
						console.log('Connection Open');
					});

					return {
						atp : atp,
						status : function() {
							return ws.readyState;
						},
						test : function() {
							return tests;
						},
						send : function(message) {
							if (angular.isString(message)) {
								console.log("Send STING");
								ws.send(message);
							} else if (angular.isObject(message)) {
								console.log("Send OBJECT");
								console.log(message);
								ws.send(JSON.stringify(message));
							}
						},
						format : function() {
							var totalLines = javaEditor.lineCount();
							javaEditor.autoFormatRange({
								line : 0,
								ch : 0
							}, {
								line : totalLines
							});
						},
						output : function(output) {
							addOutput(output);
						},
						sendSave : function() {
							var files = [];

							// Files
							$
									.each(
											buffers,
											function(buffer) {

												var file = [];

												// File Content
												var content = "";
												$
														.each(
																buffers[buffer]['children'][0]['lines'],
																function(line) {
																	content += buffers[buffer]['children'][0]['lines'][line]['text']
																			+ "\n";
																});

												files.push({
													"filename" : buffer,
													"content" : content
												});

											});

							ws.send(JSON.stringify({
								"type" : "SAVE",
								"files" : files
							}));

						}
					};
				});

// Controller
mod_editor
		.controller(
				'EditorController',
				function($scope, $state, $http, ATP, NewsFactory, Competition,
						Clock) {

					// Get all news items
					$scope.newsItems = NewsFactory.query();

					// Get active round
					Competition.getActiveRound().$promise.then(
							function(result) {
								if (!angular.isUndefined(result.id)) {
									$scope.round = result;
									Competition.setActiveRound(result);
								} else {
									$state.go('dashboard')
								}
							}, function(error) {
								console.log(error);
							});

					// listener for broadcast message, received when the
					// websocket 'clock' status is changed.
					$scope.$on('statuschanged', function(event, data) {
						console.log('dashboard status changed to ' + data)
						if (data === 'stopped') {
							$state.go('dashboard')
						}

					});

					// jQuery - OnLoad Event
					angular
							.element(document)
							.ready(
									function() {

										// Menu expand
										$(".menu-left h1 i.fa")
												.each(
														function() {
															$(this)
																	.on(
																			"click",
																			function() {
																				var minHeight = $(
																						this)
																						.parent()
																						.next()
																						.css(
																								'min-height');
																				$(
																						this)
																						.parent()
																						.next()
																						.css(
																								'min-height',
																								0);
																				$(
																						this)
																						.parent()
																						.next()
																						.slideToggle(
																								200,
																								function() {
																									$(
																											this)
																											.css(
																													'min-height',
																													minHeight);
																								});
																				if ($(
																						this)
																						.hasClass(
																								"fa-minus"))
																					$(
																							this)
																							.removeClass(
																									"fa-minus")
																							.addClass(
																									"fa-plus");
																				else
																					$(
																							this)
																							.removeClass(
																									"fa-plus")
																							.addClass(
																									"fa-minus");
																			});
														});

										// Open shortcut popup
										$("#editorCodeInfo")
												.on(
														"click",
														function() {
															showPopup(
																	'Shortcuts',
																	'Below you can find the shortcuts which are available for use:<br><br>Ctrl + F = Search<br>');
														});

										ATP.send({
											"type" : "GET"
										});

										// Auto Format - Code
										$('#editorCodeFormat')
												.click(
														function() {
															if (!$(
																	"#editorCodeFormat")
																	.hasClass(
																			"disabled")) {
																ATP.format();
															}
														});

										// Button Save
										$("#save").click(function() {

											ATP.output("[INFO] Saving...");
											ATP.sendSave();

										});

										// Button Compile
										$("#compile").click(function() {
											ATP.output("[INFO] Compiling...");
											ATP.send({
												"type" : "COMPILE"
											});
										});

										$("#test")
												.click(
														function() {
															ATP
																	.output("[INFO] "
																			+ ATP
																					.test()
																			+ ": Testing...");
															if (ATP.test() == "") {
																ATP
																		.send({
																			"type" : "TEST",
																			"parameters" : []
																		});
															} else {
																ATP
																		.send({
																			"type" : "TEST",
																			"parameters" : [ ATP
																					.test() ]
																		});
															}
														});

										// Button Submit
										$("#submit").click(function() {
											ATP.output("[INFO] Submitting...");
											ATP.send({
												"type" : "SUBMIT"
											});
										});

										// Button Save
										$("#clear").click(function() {
											$('#build-output').val('');
										});

									});

				});

jQuery.fn.center = function() {
	this.css("position", "absolute");
	this.css("top", Math.max(0,
			(($(window).height() - $(this).outerHeight()) / 2)
					+ $(window).scrollTop())
			+ "px");
	this.css("left", Math.max(0,
			(($(window).width() - $(this).outerWidth()) / 2)
					+ $(window).scrollLeft())
			+ "px");
	return this;
}
