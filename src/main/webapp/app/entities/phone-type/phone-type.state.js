(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('phone-type', {
            parent: 'entity',
            url: '/phone-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PhoneTypes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/phone-type/phone-types.html',
                    controller: 'PhoneTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('phone-type-detail', {
            parent: 'phone-type',
            url: '/phone-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PhoneType'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/phone-type/phone-type-detail.html',
                    controller: 'PhoneTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PhoneType', function($stateParams, PhoneType) {
                    return PhoneType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'phone-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('phone-type-detail.edit', {
            parent: 'phone-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/phone-type/phone-type-dialog.html',
                    controller: 'PhoneTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PhoneType', function(PhoneType) {
                            return PhoneType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('phone-type.new', {
            parent: 'phone-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/phone-type/phone-type-dialog.html',
                    controller: 'PhoneTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                phoneType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('phone-type', null, { reload: 'phone-type' });
                }, function() {
                    $state.go('phone-type');
                });
            }]
        })
        .state('phone-type.edit', {
            parent: 'phone-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/phone-type/phone-type-dialog.html',
                    controller: 'PhoneTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PhoneType', function(PhoneType) {
                            return PhoneType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('phone-type', null, { reload: 'phone-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('phone-type.delete', {
            parent: 'phone-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/phone-type/phone-type-delete-dialog.html',
                    controller: 'PhoneTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PhoneType', function(PhoneType) {
                            return PhoneType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('phone-type', null, { reload: 'phone-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
