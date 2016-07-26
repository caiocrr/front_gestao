window.onload = iniciar;

var controle;

var dialogo;



function popularLista(objJSONresp) {

    limparLista();

    var items = objJSONresp.result;


    var div = document.getElementById("idTabelaResultados");
    var table = document.createElement('table');
    table.setAttribute("border", "1");
    table.setAttribute("frame", "void");
    table.setAttribute("rules", "rows");
    table.setAttribute("id", "tableResult");



    for (var i = 0; i < items.length; i++) {
        var tr = document.createElement('tr');
        tr.setAttribute("class", "spaceUnder");
        var td1 = document.createElement('td');
        td1.setAttribute("style", "width:12%");
        var td2 = document.createElement('td');
        td2.setAttribute("style", "width:40%");
        var td3 = document.createElement('td');
        td3.setAttribute("style", "width:10%");
        var td4 = document.createElement('td');
        td4.setAttribute("style", "width:18%");

        var div1 = document.createElement('div');
        div1.setAttribute("class", 'holder');

        var td5 = document.createElement('td');
        td5.setAttribute("style", "width:15%");

//        var td6 = document.createElement('td');
//        td6.setAttribute("style", "width:5%");

        var img = document.createElement('img');
        img.src = items[i].linkimg;
        img.setAttribute("style", "width: 150px; height: 150px;");



        var a = document.createElement('a');
        var linkText = document.createTextNode('Ir à loja');
        a.setAttribute("class", "btn btn-default");
        a.appendChild(linkText);
        a.href = items[i].url;
        a.setAttribute("target", "_blank");



//        var btnCalcFrete = document.createElement('button');
//        var btnCalcFreteText = document.createTextNode('Calcular frete');
//        btnCalcFrete.setAttribute("class", "btn btn-default");
//        btnCalcFrete.appendChild(btnCalcFreteText);

//        var url = items[i].url;
//        var cep = "21060-420";
//        var nomefarmacia = items[i].nomefarmacia;
//
//        var textFrete = document.createElement('div');
//        textFrete.setAttribute("style", "display:none;");
//
//        var divWaitFrete = document.createElement('div');
        
//        var imgLoading = document.createElement('img');
//        imgLoading.src = 'images/ajax_loader2.gif';
        
//        divWaitFrete.appendChild(imgLoading);
//        divWaitFrete.setAttribute("style", "display:none;");
        

//        btnCalcFrete.onclick = function (url, textFrete, nomefarmacia, divWaitFrete) {
//            return function () {
//                
//                var calcFrete = {};
//                calcFrete.botao = "frete";
//                calcFrete.CEP = cep;
//                calcFrete.URL = url;
//                calcFrete.NOMEFARMACIA = nomefarmacia;
//                this.style.display = 'none';
//                calcularFreteAJAX(JSON.stringify(calcFrete), textFrete, divWaitFrete);
//                
//                
//            };
//
//        }(url, textFrete, nomefarmacia , divWaitFrete);

        var text1 = document.createTextNode(items[i].nome);


//        var text3 = document.createTextNode();

        var imgLogo = document.createElement('img');
        imgLogo.src = items[i].urlfarmacia;
        div1.appendChild(imgLogo);

        td1.appendChild(img);
        td2.appendChild(text1);
        td3.appendChild(boldHTML("R$ " + items[i].preco));
        td4.appendChild(div1);
        td5.appendChild(a);
//        td6.appendChild(btnCalcFrete);
//        td6.appendChild(textFrete);
//        td6.appendChild(divWaitFrete);

        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        tr.appendChild(td5);
//        tr.appendChild(td6);

        table.appendChild(tr);

    }
    function boldHTML(text) {
        var element = document.createElement("b");
        element.innerHTML = text;
        return element;
    }

    div.appendChild(table);





}
;
function limparLista() {
    document.getElementById('idTabelaResultados').innerHTML = "";

}




function objLength(obj) {
    var i = 0;
    for (var x in obj) {
        if (obj.hasOwnProperty(x)) {
            i++;
        }
    }
    return i;
}
function buscarAJAX(stringJson) {

    var objPedidoAJAX = new XMLHttpRequest();
    objPedidoAJAX.open("POST", "controller");
    objPedidoAJAX.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    // Prepara recebimento da resposta: tipo da resposta JSON!
    objPedidoAJAX.responseType = 'json';
    objPedidoAJAX.onreadystatechange =
            function () {
                if (objPedidoAJAX.readyState === 4 && objPedidoAJAX.status === 200) {

                    document.getElementById('idMsgDialogo2').innerHTML = "Produtos localizados: " + objLength(objPedidoAJAX.response.result);
                    popularLista(objPedidoAJAX.response);
                    console.log(objPedidoAJAX.response);
                    hideLoading();
                }
                ;
            };
    // Envio do pedido

    objPedidoAJAX.send(stringJson);
    showLoading();
}
;

function calcularFreteAJAX(stringJson, textFrete, divWaitFrete) {

    var objPedidoAJAX = new XMLHttpRequest();
    objPedidoAJAX.open("POST", "controller");
    objPedidoAJAX.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    // Prepara recebimento da resposta: tipo da resposta JSON!
    objPedidoAJAX.responseType = 'json';
    objPedidoAJAX.onreadystatechange =
            function () {
                if (objPedidoAJAX.readyState === 4 && objPedidoAJAX.status === 200) {
                    hideLoadingFrete(divWaitFrete);
                    textFrete.innerHTML = '<b>' + objPedidoAJAX.response.result + '</b>';
                    textFrete.style.display = 'block';
//                    document.getElementById('idMsgDialogo2').innerHTML = "Done";
                    
                }
                ;
            };
    // Envio do pedido
    
    objPedidoAJAX.send(stringJson);
    showLoadingFrete(divWaitFrete);
}
;

function showLoading() {
    $("#divLoading").show();
}

function hideLoading() {
    $("#divLoading").hide();
}

function showLoadingFrete(divWaitFrete) {
    divWaitFrete.style.display = 'block';
}

function hideLoadingFrete(divWaitFrete) {
    divWaitFrete.style.display = 'none';
}


function limparBusca() {
    document.getElementById('idpatrimonio').value = "";
    document.getElementById('idtitulo').value = "";
    document.getElementById('idautoria').value = "";
    document.getElementById('idveiculo').value = "";
    document.getElementById('iddatapublicacao21').value = "";
    document.getElementById('iddatapublicacao22').value = "";
    document.getElementById('idpalchave2').value = "";
}

function iniciar() {

    var objDialogo = function () {
        this.elementoDialogo = document.getElementById('idDialogo');
        this.escreverMensagem = function (texto) {
            this.elementoDialogo.innerHTML = texto;
        };
    };

    dialogo = new objDialogo();


    function dadosEscolhidos(botao) {
        var resposta = {};
        resposta.botao = botao;
        resposta.patrimonio = document.getElementById('idpatrimonio').value;
        return resposta;
    }



    document.getElementById('idBuscar').addEventListener("click", function () {
        buscarAJAX(JSON.stringify(dadosEscolhidos("buscar")));
    }
    );






}
