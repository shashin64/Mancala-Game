var kalahApp = angular.module('kalahApp', ['ngRoute', 'gameModule']);




kalahApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
    when('/rules', {
        templateUrl: 'templates/rules.html'
    }).
    when('/player', {
        templateUrl: 'templates/lobby.html',
        controller: 'lobbyController'
    }).
    when('/game/:id', {
        templateUrl: 'templates/game.html',
        controller: 'gameController'
    }).
    otherwise({
        redirectTo: '/player'
    });
}]);