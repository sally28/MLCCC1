(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('account-flag-status', {
            parent: 'entity',
            url: '/account-flag-status',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AccountFlagStatuses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/account-flag-status/account-flag-statuses.html',
                    controller: 'AccountFlagStatusController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('account-flag-status-detail', {
            parent: 'account-flag-status',
            url: '/account-flag-status/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AccountFlagStatus'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/account-flag-status/account-flag-status-detail.html',
                    controller: 'AccountFlagStatusDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AccountFlagStatus', function($stateParams, AccountFlagStatus) {
                    return AccountFlagStatus.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'account-flag-status',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('account-flag-status-detail.edit', {
            parent: 'account-flag-status-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-flag-status/account-flag-status-dialog.html',
                    controller: 'AccountFlagStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountFlagStatus', function(AccountFlagStatus) {
                            return AccountFlagStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-flag-status.new', {
            parent: 'account-flag-status',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-flag-status/account-flag-status-dialog.html',
                    controller: 'AccountFlagStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('account-flag-status', null, { reload: 'account-flag-status' });
                }, function() {
                    $state.go('account-flag-status');
                });
            }]
        })
        .state('account-flag-status.edit', {
            parent: 'account-flag-status',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-flag-status/account-flag-status-dialog.html',
                    controller: 'AccountFlagStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountFlagStatus', function(AccountFlagStatus) {
                            return AccountFlagStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-flag-status', null, { reload: 'account-flag-status' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-flag-status.delete', {
            parent: 'account-flag-status',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-flag-status/account-flag-status-delete-dialog.html',
                    controller: 'AccountFlagStatusDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AccountFlagStatus', function(AccountFlagStatus) {
                            return AccountFlagStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-flag-status', null, { reload: 'account-flag-status' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
