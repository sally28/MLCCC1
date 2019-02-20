(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mlc-class-category', {
            parent: 'entity',
            url: '/mlc-class-category',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mlcccApp.mlcClassCategory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mlc-class-category/mlc-class-categories.html',
                    controller: 'MlcClassCategoryController',
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
        .state('mlc-class-category-detail', {
            parent: 'mlc-class-category',
            url: '/mlc-class-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mlcccApp.mlcClassCategory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mlc-class-category/mlc-class-category-detail.html',
                    controller: 'MlcClassCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mlcClassCategory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MlcClassCategory', function($stateParams, MlcClassCategory) {
                    return MlcClassCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'mlc-class-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('mlc-class-category-detail.edit', {
            parent: 'mlc-class-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-class-category/mlc-class-category-dialog.html',
                    controller: 'MlcClassCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MlcClassCategory', function(MlcClassCategory) {
                            return MlcClassCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mlc-class-category.new', {
            parent: 'mlc-class-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-class-category/mlc-class-category-dialog.html',
                    controller: 'MlcClassCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mlc-class-category', null, { reload: 'mlc-class-category' });
                }, function() {
                    $state.go('mlc-class-category');
                });
            }]
        })
        .state('mlc-class-category.edit', {
            parent: 'mlc-class-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-class-category/mlc-class-category-dialog.html',
                    controller: 'MlcClassCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MlcClassCategory', function(MlcClassCategory) {
                            return MlcClassCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mlc-class-category', null, { reload: 'mlc-class-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mlc-class-category.delete', {
            parent: 'mlc-class-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mlc-class-category/mlc-class-category-delete-dialog.html',
                    controller: 'MlcClassCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MlcClassCategory', function(MlcClassCategory) {
                            return MlcClassCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mlc-class-category', null, { reload: 'mlc-class-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
