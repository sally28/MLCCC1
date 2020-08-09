(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('registration-status', {
            parent: 'entity',
            url: '/registration-status',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RegistrationStatuses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/registration-status/registration-statuses.html',
                    controller: 'RegistrationStatusController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('registration-status-detail', {
            parent: 'registration-status',
            url: '/registration-status/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RegistrationStatus'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/registration-status/registration-status-detail.html',
                    controller: 'RegistrationStatusDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RegistrationStatus', function($stateParams, RegistrationStatus) {
                    return RegistrationStatus.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'registration-status',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('registration-status-detail.edit', {
            parent: 'registration-status-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registration-status/registration-status-dialog.html',
                    controller: 'RegistrationStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RegistrationStatus', function(RegistrationStatus) {
                            return RegistrationStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('registration-status.new', {
            parent: 'registration-status',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registration-status/registration-status-dialog.html',
                    controller: 'RegistrationStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('registration-status', null, { reload: 'registration-status' });
                }, function() {
                    $state.go('registration-status');
                });
            }]
        })
        .state('registration-status.edit', {
            parent: 'registration-status',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registration-status/registration-status-dialog.html',
                    controller: 'RegistrationStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RegistrationStatus', function(RegistrationStatus) {
                            return RegistrationStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('registration-status', null, { reload: 'registration-status' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('registration-status.delete', {
            parent: 'registration-status',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registration-status/registration-status-delete-dialog.html',
                    controller: 'RegistrationStatusDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RegistrationStatus', function(RegistrationStatus) {
                            return RegistrationStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('registration-status', null, { reload: 'registration-status' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
