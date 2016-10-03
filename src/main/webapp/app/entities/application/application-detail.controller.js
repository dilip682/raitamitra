(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .controller('ApplicationDetailController', ApplicationDetailController);

    ApplicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Application', 'District', 'Taluka', 'Hobli', 'Village', 'Status'];

    function ApplicationDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Application, District, Taluka, Hobli, Village, Status) {
        var vm = this;

        vm.application = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('raitamitraApp:applicationUpdate', function(event, result) {
            vm.application = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
