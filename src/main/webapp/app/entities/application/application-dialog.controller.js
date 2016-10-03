(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .controller('ApplicationDialogController', ApplicationDialogController);

    ApplicationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Application', 'District', 'Taluka', 'Hobli', 'Village', 'Status'];

    function ApplicationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Application, District, Taluka, Hobli, Village, Status) {
        var vm = this;

        vm.application = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.districts = District.query();
        vm.talukas = Taluka.query();
        vm.hoblis = Hobli.query();
        vm.villages = Village.query();
        vm.statuses = Status.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.application.id !== null) {
                Application.update(vm.application, onSaveSuccess, onSaveError);
            } else {
                Application.save(vm.application, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('raitamitraApp:applicationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.appDate = false;

        vm.setApplicantPhoto = function ($file, application) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        application.applicantPhoto = base64Data;
                        application.applicantPhotoContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
