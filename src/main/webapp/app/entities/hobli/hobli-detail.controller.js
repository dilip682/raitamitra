(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .controller('HobliDetailController', HobliDetailController);

    HobliDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Hobli'];

    function HobliDetailController($scope, $rootScope, $stateParams, previousState, entity, Hobli) {
        var vm = this;

        vm.hobli = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('raitamitraApp:hobliUpdate', function(event, result) {
            vm.hobli = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
