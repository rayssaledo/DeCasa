'use strict';

(function() {
	var app = angular.module('MyPayment', []);
	app.controller("MyPaymentController", function($scope) {
	
		$scope.total = 0;
		$scope.calctotal = function() {
			$scope.total = $scope.total + 50;
		};
	});	

	//Falata fazer a parte de decrementar, caso o usuário clique no checkbox já selecionado. (Farei depois).


})();


