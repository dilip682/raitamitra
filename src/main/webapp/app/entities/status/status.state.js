(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('status', {
            parent: 'entity',
            url: '/status',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Statuses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/status/statuses.html',
                    controller: 'StatusController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('status-detail', {
            parent: 'entity',
            url: '/status/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Status'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/status/status-detail.html',
                    controller: 'StatusDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Status', function($stateParams, Status) {
                    return Status.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'status',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('status-detail.edit', {
            parent: 'status-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/status/status-dialog.html',
                    controller: 'StatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Status', function(Status) {
                            return Status.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('status.new', {
            parent: 'status',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/status/status-dialog.html',
                    controller: 'StatusDialogController',
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
                    $state.go('status', null, { reload: 'status' });
                }, function() {
                    $state.go('status');
                });
            }]
        })
        .state('status.edit', {
            parent: 'status',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/status/status-dialog.html',
                    controller: 'StatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Status', function(Status) {
                            return Status.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('status', null, { reload: 'status' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('status.delete', {
            parent: 'status',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/status/status-delete-dialog.html',
                    controller: 'StatusDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Status', function(Status) {
                            return Status.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('status', null, { reload: 'status' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
