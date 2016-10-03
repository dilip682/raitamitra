(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('village', {
            parent: 'entity',
            url: '/village',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Villages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/village/villages.html',
                    controller: 'VillageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('village-detail', {
            parent: 'entity',
            url: '/village/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Village'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/village/village-detail.html',
                    controller: 'VillageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Village', function($stateParams, Village) {
                    return Village.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'village',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('village-detail.edit', {
            parent: 'village-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/village/village-dialog.html',
                    controller: 'VillageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Village', function(Village) {
                            return Village.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('village.new', {
            parent: 'village',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/village/village-dialog.html',
                    controller: 'VillageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('village', null, { reload: 'village' });
                }, function() {
                    $state.go('village');
                });
            }]
        })
        .state('village.edit', {
            parent: 'village',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/village/village-dialog.html',
                    controller: 'VillageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Village', function(Village) {
                            return Village.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('village', null, { reload: 'village' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('village.delete', {
            parent: 'village',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/village/village-delete-dialog.html',
                    controller: 'VillageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Village', function(Village) {
                            return Village.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('village', null, { reload: 'village' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
