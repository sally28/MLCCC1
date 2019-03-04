(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mlc-class', {
            parent: 'entity',
            url: '/mlc-class?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MlcClasses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mlc-class/mlc-classes.html',
                    controller: 'MlcClassController',
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

                loginUser:['Principal', function(Principal){
                    return Principal.identity (false);
                }]
            }
        })
        .state('my-mlc-class', {
            parent: 'entity',
            url: '/my-class?page&sort&search',
            data: {
                authorities: ['ROLE_TEACHER'],
                pageTitle: 'MyMlcClasses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mlc-class/my-mlc-classes.html',
                    controller: 'MyMlcClassController',
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
                loginUser:['Principal', function(Principal){
                    return Principal.identity (false);
                }]
            }
        })
        .state('mlc-class-detail', {
            parent: 'mlc-class',
            url: '/mlc-class/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MlcClass'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mlc-class/mlc-class-detail.html',
                    controller: 'MlcClassDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MlcClass', function($stateParams, MlcClass) {
                    return MlcClass.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'mlc-class',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('mlc-class-detail.edit', {
            parent: 'mlc-class-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-class/mlc-class-dialog.html',
                    controller: 'MlcClassDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MlcClass', function(MlcClass) {
                            return MlcClass.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mlc-class.new', {
            parent: 'mlc-class',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-class/mlc-class-dialog.html',
                    controller: 'MlcClassDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                className: null,
                                textBook: null,
                                size: null,
                                minAge: null,
                                tuition: null,
                                registrationFee: null,
                                seqNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mlc-class', null, { reload: 'mlc-class' });
                }, function() {
                    $state.go('mlc-class');
                });
            }]
        })
        .state('mlc-class.edit', {
            parent: 'mlc-class',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-class/mlc-class-dialog.html',
                    controller: 'MlcClassDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MlcClass', function(MlcClass) {
                            return MlcClass.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mlc-class', null, { reload: 'mlc-class' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mlc-class.delete', {
            parent: 'mlc-class',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-class/mlc-class-delete-dialog.html',
                    controller: 'MlcClassDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MlcClass', function(MlcClass) {
                            return MlcClass.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mlc-class', null, { reload: 'mlc-class' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
