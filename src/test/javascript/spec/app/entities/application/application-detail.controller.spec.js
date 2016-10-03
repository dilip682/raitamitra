'use strict';

describe('Controller Tests', function() {

    describe('Application Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockApplication, MockDistrict, MockTaluka, MockHobli, MockVillage, MockStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockApplication = jasmine.createSpy('MockApplication');
            MockDistrict = jasmine.createSpy('MockDistrict');
            MockTaluka = jasmine.createSpy('MockTaluka');
            MockHobli = jasmine.createSpy('MockHobli');
            MockVillage = jasmine.createSpy('MockVillage');
            MockStatus = jasmine.createSpy('MockStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Application': MockApplication,
                'District': MockDistrict,
                'Taluka': MockTaluka,
                'Hobli': MockHobli,
                'Village': MockVillage,
                'Status': MockStatus
            };
            createController = function() {
                $injector.get('$controller')("ApplicationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'raitamitraApp:applicationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
