(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .controller('TalukaDetailController', TalukaDetailController);

    TalukaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Taluka'];

    function TalukaDetailController($scope, $rootScope, $stateParams, previousState, entity, Taluka) {
        var vm = this;

        vm.taluka = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('raitamitraApp:talukaUpdate', function(event, result) {
            vm.taluka = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
