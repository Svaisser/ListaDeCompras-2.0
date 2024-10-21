angular.module('meuApp').controller('MeuController', function ($scope, $http) {

  $scope.listaCompras = [];
  $scope.compraEditando = {};
  $scope.ErroInclusao = '';

  $scope.carregarCompras = function () {
    $http(
      {
        url: 'compras.json',
        method: 'GET'
      }).then(function (response) {
        $scope.listaCompras = response.data;
      })
  }

  $scope.enviarCompras = function () {
    const url = 'http://localhost:8080/compras/';
    const data = $scope.listaCompras;

    $http.post(url, data)
      .then(function (response) {
        console.log('Sucesso:', response.data);
      })
      .catch(function (error) {
        console.error('Erro:', error);
      });
  };

  $scope.carregarCompras();

  function getNextId() {
    if ($scope.listaCompras.length === 0) {
      return 1;
    } else {
      var ids = $scope.listaCompras.map(function (c) { return c.id; }).sort(function (a, b) { return a - b; });

      for (var i = 0; i < ids.length; i++) {
        if (ids[i] !== i + 1) {
          return i + 1;
        }
      }
      return ids[ids.length - 1] + 1;
    }
  }

  $scope.adicionarCompra = function () {
    $scope.ErroInclusao = '';

    var item = $scope.frmCompras.item;
    var quantia = $scope.frmCompras.quantia;
    var id = getNextId();
    if (quantia < 1) {
      $scope.ErroInclusao = 'A quantitidade precisa ser no mínimo 1.';
      return;
    }
    if (!item || !quantia) {
      $scope.ErroInclusao = 'Por favor, preencha todos os campos obrigatórios.';
      return;
    }

    $scope.listaCompras.push({
      "item": item,
      "quantia": quantia,
      "id": id
    });

    // Envia os dados para o servidor
    $http({
      url: '/addCompras',
      method: 'POST',
      data: {
        "item": item,
        "quantia": quantia,
        "id": id
      }
    }).then(function (response) {
      console.log(response.data);
    }).catch(function (error) {
      $scope.ErroInclusao = 'Erro ao adicionar a compra: ' + error.message;
    });

    $scope.frmCompras = {
      "item": "",
      "quantia": ""
    };

    enviarCompras()
  };

  $scope.mostrarModalConfirmacao = false;
  $scope.itemParaExcluir = null;

  $scope.excluirCompra = function (item) {
    $scope.itemParaExcluir = item;
    $scope.mostrarModalConfirmacao = true;
  };

  $scope.confirmarExclusao = function () {
    $http({
      url: '/deleteCompra',
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      data: { item: $scope.itemParaExcluir }
    }).then(function (response) {
      $scope.carregarCompras();

      $scope.exclusaoAviso = true;

      $scope.mostrarModalConfirmacao = false;
      $scope.itemParaExcluir = null;

      // Após 3 segundos, ocultar a div novamente
      setTimeout(function () {
        $scope.$apply(function () {
          $scope.exclusaoAviso = false;
        });
      }, 3000);

    }).catch(function (error) {
      $scope.ErroExclusao = error.data || 'Erro ao excluir a compra.';
      alert($scope.ErroExclusao);
    });
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

    $http({
      url: '/updateCompra/' + id,
      method: 'PUT',
      data: {
        item: item,
        quantia: quantia,
        descricao: descricao
      }
    }).then(function (response) {
      console.log('Compra atualizada com sucesso:', response.data); // Mensagem de sucesso
    }).catch(function (error) {
      $scope.ErroInclusao = 'Erro ao atualizar a compra: ' + error.message;
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