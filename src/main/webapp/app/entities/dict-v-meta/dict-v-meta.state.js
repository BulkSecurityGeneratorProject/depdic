(function() {
    'use strict';

    angular
        .module('depdicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dict-v-meta', {
            parent: 'entity',
            url: '/dict-v-meta',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'depdicApp.dictVMeta.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dict-v-meta/dict-v-metas.html',
                    controller: 'DictVMetaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dictVMeta');
                    $translatePartialLoader.addPart('colTyp');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('dict-v-meta-detail', {
            parent: 'dict-v-meta',
            url: '/dict-v-meta/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'depdicApp.dictVMeta.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dict-v-meta/dict-v-meta-detail.html',
                    controller: 'DictVMetaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dictVMeta');
                    $translatePartialLoader.addPart('colTyp');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DictVMeta', function($stateParams, DictVMeta) {
                    return DictVMeta.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dict-v-meta',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dict-v-meta-detail.edit', {
            parent: 'dict-v-meta-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict-v-meta/dict-v-meta-dialog.html',
                    controller: 'DictVMetaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DictVMeta', function(DictVMeta) {
                            return DictVMeta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dict-v-meta.new', {
            parent: 'dict-v-meta',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict-v-meta/dict-v-meta-dialog.html',
                    controller: 'DictVMetaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                colTyp: null,
                                colKr: null,
                                colEn: null,
                                jsonDesc: null,
                                memo: null,
                                dtm: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dict-v-meta', null, { reload: 'dict-v-meta' });
                }, function() {
                    $state.go('dict-v-meta');
                });
            }]
        })
        .state('dict-v-meta.edit', {
            parent: 'dict-v-meta',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict-v-meta/dict-v-meta-dialog.html',
                    controller: 'DictVMetaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DictVMeta', function(DictVMeta) {
                            return DictVMeta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dict-v-meta', null, { reload: 'dict-v-meta' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dict-v-meta.delete', {
            parent: 'dict-v-meta',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict-v-meta/dict-v-meta-delete-dialog.html',
                    controller: 'DictVMetaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DictVMeta', function(DictVMeta) {
                            return DictVMeta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dict-v-meta', null, { reload: 'dict-v-meta' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
