angular.module('meuApp').controller('userController', function ($scope, $http) {

    $scope.frmUser = {
        "name" : "",
        "username" : "",
        "password" : ""
    }

    $scope.createUser = function () {
        $http.post('http://localhost:8080/users/', $scope.frmUser)
           .then(function (response) {
                console.log(response.data);
            })
           .catch(function (error) {
                console.error('Erro:', error);
            });

        $scope.frmUser = {
            "name" : "",
            "username" : "",
            "password" : ""
        };

        alert('Usuário criado com sucesso!');
    }
});