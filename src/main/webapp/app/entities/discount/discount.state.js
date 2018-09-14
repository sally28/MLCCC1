(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('discount', {
            parent: 'entity',
            url: '/discount',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Discounts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/discount/discounts.html',
                    controller: 'DiscountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('discount-detail', {
            parent: 'discount',
            url: '/discount/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Discount'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/discount/discount-detail.html',
                    controller: 'DiscountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Discount', function($stateParams, Discount) {
                    return Discount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'discount',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('discount-detail.edit', {
            parent: 'discount-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount/discount-dialog.html',
                    controller: 'DiscountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Discount', function(Discount) {
                            return Discount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('discount.new', {
            parent: 'discount',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount/discount-dialog.html',
                    controller: 'DiscountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                startDate: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('discount', null, { reload: 'discount' });
                }, function() {
                    $state.go('discount');
                });
            }]
        })
        .state('discount.edit', {
            parent: 'discount',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount/discount-dialog.html',
                    controller: 'DiscountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Discount', function(Discount) {
                            return Discount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('discount', null, { reload: 'discount' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('discount.delete', {
            parent: 'discount',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount/discount-delete-dialog.html',
                    controller: 'DiscountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Discount', function(Discount) {
                            return Discount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('discount', null, { reload: 'discount' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
