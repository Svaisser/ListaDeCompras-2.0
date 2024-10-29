angular.module('meuApp').controller('userController', function ($scope, $http, $window) {

  // AŔEA DO CADASTRO

  $scope.ErroCadastro = '';
  $scope.SucessoCadastro = '';

  $scope.cadastroUser = {
    "name": "",
    "username": "",
    "password": ""
  }

  $scope.createUser = function () {

    $scope.ErroCadastro = '';

    if (!$scope.cadastroUser.name || !$scope.cadastroUser.username || !$scope.cadastroUser.password) {
      $scope.ErroCadastro = 'Todos os campos são obrigatórios!';
      setTimeout(function () {
        $scope.$apply(function () {
          $scope.ErroCadastro = false;
        });
      }, 5000);
      return;
    }

    $scope.getPasswordLength = function () {
      return $scope.cadastroUser.password ? $scope.cadastroUser.password.length : 0;
    };

    if ($scope.getPasswordLength() < 8) {
      $scope.ErroCadastro = 'A senha deve ter pelo menos 8 caracteres!';
      setTimeout(function () {
        $scope.$apply(function () {
          $scope.ErroCadastro = false;
        });
      }, 5000);
      return;
    }

    $http.post('http://localhost:8080/users/', $scope.cadastroUser)
      .then(function (response) {
        console.log(response.data);
        $scope.SucessoCadastro = 'Usuário criado com sucesso!';
        setTimeout(function () {
          $scope.$apply(function () {
            $scope.SucessoCadastro = false;
          });
        }, 5000);
        $scope.cadastroUser = {
          "name": "",
          "username": "",
          "password": ""
        };
      })
      .catch(function (error) {
        console.error('Erro:', error);

        const errorMessage = error.data && error.data.message
          ? error.data.message
          : 'Erro ao cadastrar o usuário.';

        $scope.ErroCadastro = errorMessage;
        setTimeout(function () {
          $scope.$apply(function () {
            $scope.ErroCadastro = false;
          });
        }, 5000);
      });
  }

  $scope.enterCadastro = function (event) {
    if (event.which === 13) {  // 13 é o código da tecla Enter
      event.preventDefault();
      $scope.createUser();
    }
  };

  // ÁREA DO LOGIN

  $scope.SucessoLogin = '';
  $scope.ErroLogin = '';
  $scope.frmLogin = {
    "username": "",
    "password": ""
  }

  $scope.loginUser = function () {

    $scope.ErroLogin = '';

    if (!$scope.frmLogin.username || !$scope.frmLogin.password) {
      $scope.ErroLogin = 'Todos os campos são obrigatórios!';
      setTimeout(function () {
        $scope.$apply(function () {
          $scope.ErroLogin = false;
        });
      }, 5000);
      return;
    }

    $http.post('http://localhost:8080/users/login', $scope.frmLogin).then(function (response) {
      const message = response.data.message || response.data;
      console.log(message);
      $scope.SucessoLogin = 'Login efetuado com sucesso!';
      setTimeout(function () {
        $scope.$apply(function () {
          $scope.SucessoLogin = false;
        });
      }, 5000);
      $scope.frmLogin = {
        "username": "",
        "password": ""
      };
      $window.location.href = "lista.html";
    }).catch(function (error) {
      console.error('Erro:', error);
    
      const errorMessage = error.data && error.data.message
        ? error.data.message
        : 'Erro ao fazer o login.';
    
      $scope.ErroLogin = errorMessage;
      setTimeout(function () {
        $scope.$apply(function () {
          $scope.ErroLogin = false;
        });
      }, 5000);
    });
  }

  $scope.enterLogin = function (event) {
    if (event.which === 13) {  // 13 é o código da tecla Enter
      event.preventDefault();
      $scope.loginUser();
    }
  };

});