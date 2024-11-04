angular.module('meuApp').controller('listaCompras', function ($scope, $http, $window) {

  $scope.listaCompras = [];
  $scope.compraEditando = {};
  $scope.ErroInclusao = '';

  const token = localStorage.getItem('token');
  const idUser = localStorage.getItem('idUser');
  console.log('ID do usuário: ', idUser);
  console.log('Token JWT: ', token);

  if (token) {
    $http.get(`http://localhost:8080/compras/${idUser}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
      .then(function (response) {
        $scope.listaCompras = response.data;
      })
      .catch(error => {
        console.error('Erro com Axios ao buscar compras:', error);
        if (error.response) {
          const errorMessage = error.response.data || 'Acesso negado. Verifique seu login.';
          alert(errorMessage);
        }
      });
  } else {
    alert('Precisa estar logado para acessar sua lista.');
    $window.location.href = 'http://localhost:3000/view/login.html';
  }

  $scope.logoutUser = function () {
    localStorage.removeItem('token');
    localStorage.removeItem('idUser');
    window.location.href = 'http://localhost:3000/view/login.html';
  }


  $scope.adicionarCompra = function () {
    $scope.ErroInclusao = '';
    $scope.bd_compra = [];
    var item = $scope.frmCompras.item;
    var quantia = $scope.frmCompras.quantia;
    var compraIndex = $scope.listaCompras.findIndex(c => c.item === item);

    if (compraIndex > -1) {
      $scope.ErroInclusao = "Já existe esse item na lista.";
      setTimeout(function () {
        $scope.$apply(function () {
          $scope.ErroInclusao = false;
        });
      }, 3000);
      return;
    }
    if (quantia < 1) {
      $scope.ErroInclusao = 'A quantitidade precisa ser no mínimo 1.';
      setTimeout(function () {
        $scope.$apply(function () {
          $scope.ErroInclusao = false;
        });
      }, 3000);
      return;
    }
    if (item == "" || quantia == "") {
      $scope.ErroInclusao = 'Por favor, preencha todos os campos obrigatórios.';
      setTimeout(function () {
        $scope.$apply(function () {
          $scope.ErroInclusao = false;
        });
      }, 3000);
      return;
    }

    $scope.bd_compra.push({
      "item": item,
      "quantia": quantia,
      "idUser": idUser
    });

    $scope.frmCompras = {
      "item": "",
      "quantia": ""
    };

    $http({
      url: 'http://localhost:8080/compras/create',
      method: 'POST',
      data: $scope.bd_compra
    }).then(function (response) {
      console.log('Sucesso:', response.data);
      return $http.get(`http://localhost:8080/compras/${idUser}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
        .then(function (response) {
          return $scope.listaCompras = response.data;
        })
    }).catch(function (error) {
      console.error('Erro:', error);
    }, 3000);
  };

  $scope.mostrarModalConfirmacao = false;
  $scope.itemParaExcluir = null;

  $scope.excluirCompra = function (id) {
    $scope.itemParaExcluir = id;
    console.log('Excluir ' + id);
    $scope.mostrarModalConfirmacao = true;
  };

  $scope.confirmarExclusao = function () {
    const url = 'http://localhost:8080/compras/delete/' + $scope.itemParaExcluir;

    $http({
      url: url,
      method: 'DELETE'
    }).then(function (response) {
      // Exclusão bem-sucedida, agora recarrega a lista de compras
      console.log('Item excluído com sucesso:', response.data);

      // Recarrega a lista de compras após exclusão
      $http.get(`http://localhost:8080/compras/${idUser}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
        .then(function (response) {
          $scope.listaCompras = response.data;
          console.log('Lista de compras atualizada:', $scope.listaCompras);
        })
        .catch(function (error) {
          console.error('Erro ao recarregar a lista de compras:', error);
        });

    }).catch(function (error) {
      console.error('Erro ao excluir o item:', error);
      alert('Erro ao excluir o item. Tente novamente.');
    });

    // Fechar o modal de confirmação
    $scope.mostrarModalConfirmacao = false;
    $scope.itemParaExcluir = null;
  };



  $scope.cancelarExclusao = function () {
    $scope.mostrarModalConfirmacao = false;
    $scope.itemParaExcluir = null;
  };

  $scope.mostrarDetalhes = false;

  $scope.abrirDetalhes = function (compra) {
    $scope.compraDetalhes = angular.copy(compra);
    $scope.mostrarDetalhes = true;
  };

  $scope.fecharDetalhes = function () {
    $scope.mostrarDetalhes = false;
  };

  $scope.editarDetalhes = function (compraDetalhes) {
    var id = compraDetalhes.id;
    var item = compraDetalhes.item;
    var quantia = compraDetalhes.quantia;
    var descricao = compraDetalhes.descricao;

    if (quantia < 1) {
      $scope.ErroInclusao = 'A quantidade precisa ser no mínimo 1.';
      return;
    }

    if (!item || !quantia) {
      $scope.ErroInclusao = 'Por favor, preencha todos os campos obrigatórios.';
      return;
    }

    var compraIndex = $scope.listaCompras.findIndex(c => c.id === id);
    if (compraIndex > -1) {
      $scope.listaCompras[compraIndex] = {
        id: id,
        item: item,
        quantia: quantia,
        descricao: descricao
      };
    }

    //Edita o item no back.
    const url = 'http://localhost:8080/compras/update/' + id;
    $http({
      url: url,
      method: 'PUT',
      data: compraDetalhes
    }).then(function (response) {
      console.log('Sucesso:', response.data);
      $http.get(`http://localhost:8080/compras/${idUser}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
        .then(function (response) {
          $scope.listaCompras = response.data;
        })
    }).catch(function (error) {
      console.error('Erro:', error);
    });

    $scope.fecharDetalhes();
  };

  $scope.ajustarAltura = function (event) {
    const element = event.target;
    element.style.height = 'auto';
    element.style.height = (element.styleHeight) + 10;
    element.style.height = (element.scrollHeight) + 'px';
  };

});