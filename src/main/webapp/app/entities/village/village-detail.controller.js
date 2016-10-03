(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .controller('VillageDetailController', VillageDetailController);

    VillageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Village'];

    function VillageDetailController($scope, $rootScope, $stateParams, previousState, entity, Village) {
        var vm = this;

        vm.village = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('raitamitraApp:villageUpdate', function(event, result) {
            vm.village = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
