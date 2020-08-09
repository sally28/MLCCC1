(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mlc-account', {
            parent: 'entity',
            url: '/mlc-account?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MlcAccounts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mlc-account/mlc-accounts.html',
                    controller: 'MlcAccountController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('mlc-account-detail', {
            parent: 'mlc-account',
            url: '/mlc-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MlcAccount'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mlc-account/mlc-account-detail.html',
                    controller: 'MlcAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MlcAccount', function($stateParams, MlcAccount) {
                    return MlcAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'mlc-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('mlc-account-detail.edit', {
            parent: 'mlc-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-account/mlc-account-dialog.html',
                    controller: 'MlcAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MlcAccount', function(MlcAccount) {
                            return MlcAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mlc-account.new', {
            parent: 'mlc-account',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-account/mlc-account-dialog.html',
                    controller: 'MlcAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                email: null,
                                password: null,
                                createDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mlc-account', null, { reload: 'mlc-account' });
                }, function() {
                    $state.go('mlc-account');
                });
            }]
        })
        .state('mlc-account.edit', {
            parent: 'mlc-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-account/mlc-account-dialog.html',
                    controller: 'MlcAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MlcAccount', function(MlcAccount) {
                            return MlcAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mlc-account', null, { reload: 'mlc-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mlc-account.delete', {
            parent: 'mlc-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-account/mlc-account-delete-dialog.html',
                    controller: 'MlcAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MlcAccount', function(MlcAccount) {
                            return MlcAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mlc-account', null, { reload: 'mlc-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
