(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('school-district', {
            parent: 'entity',
            url: '/school-district',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SchoolDistricts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/school-district/school-districts.html',
                    controller: 'SchoolDistrictController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('school-district-detail', {
            parent: 'school-district',
            url: '/school-district/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SchoolDistrict'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/school-district/school-district-detail.html',
                    controller: 'SchoolDistrictDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SchoolDistrict', function($stateParams, SchoolDistrict) {
                    return SchoolDistrict.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'school-district',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('school-district-detail.edit', {
            parent: 'school-district-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/school-district/school-district-dialog.html',
                    controller: 'SchoolDistrictDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SchoolDistrict', function(SchoolDistrict) {
                            return SchoolDistrict.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('school-district.new', {
            parent: 'school-district',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/school-district/school-district-dialog.html',
                    controller: 'SchoolDistrictDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                county: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('school-district', null, { reload: 'school-district' });
                }, function() {
                    $state.go('school-district');
                });
            }]
        })
        .state('school-district.edit', {
            parent: 'school-district',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/school-district/school-district-dialog.html',
                    controller: 'SchoolDistrictDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SchoolDistrict', function(SchoolDistrict) {
                            return SchoolDistrict.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('school-district', null, { reload: 'school-district' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('school-district.delete', {
            parent: 'school-district',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/school-district/school-district-delete-dialog.html',
                    controller: 'SchoolDistrictDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SchoolDistrict', function(SchoolDistrict) {
                            return SchoolDistrict.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('school-district', null, { reload: 'school-district' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
