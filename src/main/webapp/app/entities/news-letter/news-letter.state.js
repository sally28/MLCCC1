(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('news-letter', {
            parent: 'entity',
            url: '/news-letter',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'NewsLetters'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/news-letter/news-letters.html',
                    controller: 'NewsLetterController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('news-letter-detail', {
            parent: 'news-letter',
            url: '/news-letter/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'NewsLetter'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/news-letter/news-letter-detail.html',
                    controller: 'NewsLetterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'NewsLetter', function($stateParams, NewsLetter) {
                    return NewsLetter.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'news-letter',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('news-letter-detail.edit', {
            parent: 'news-letter-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/news-letter/news-letter-dialog.html',
                    controller: 'NewsLetterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NewsLetter', function(NewsLetter) {
                            return NewsLetter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('news-letter.new', {
            parent: 'news-letter',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/news-letter/news-letter-dialog.html',
                    controller: 'NewsLetterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                fileName: null,
                                uploadDate: null,
                                uploadedBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('news-letter', null, { reload: 'news-letter' });
                }, function() {
                    $state.go('news-letter');
                });
            }]
        })
        .state('news-letter.edit', {
            parent: 'news-letter',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/news-letter/news-letter-dialog.html',
                    controller: 'NewsLetterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NewsLetter', function(NewsLetter) {
                            return NewsLetter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('news-letter', null, { reload: 'news-letter' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('news-letter.delete', {
            parent: 'news-letter',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/news-letter/news-letter-delete-dialog.html',
                    controller: 'NewsLetterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NewsLetter', function(NewsLetter) {
                            return NewsLetter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('news-letter', null, { reload: 'news-letter' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
