package ajax.ComJSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.StringReader;
import eel418_2016_1.DAO.ReferenciasBibliograficas;
import eel418_2016_1.DTOs.RequestBusca;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class Controller extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Não é um conjunto de pares nome-valor,
        // então tem que ler como se fosse um upload de arquivo...
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        request.getInputStream(), "UTF8"));
        String textoDoJson = br.readLine();

        JsonObject jsonObjectDeJava = null;
        // Ler e fazer o parsing do String para o "objeto json" java
        try ( //Converte o string em "objeto json" java
                // Criar um JsonReader.
                JsonReader readerDoTextoDoJson
                = Json.createReader(new StringReader(textoDoJson))) {
            // Ler e fazer o parsing do String para o "objeto json" java
            jsonObjectDeJava = readerDoTextoDoJson.readObject();
            // Acabou, então fechar o reader.
        } catch (Exception e) {
            e.printStackTrace();
        }

        ReferenciasBibliograficas rb = new ReferenciasBibliograficas();
        //received jsonObjectDeJava
        //1 - get value key botao
        String botao = jsonObjectDeJava.getString("botao");
        switch (botao) {
            case "buscar":
                RequestBusca dados = new RequestBusca();
                //patrimonio //(titulo, autoria, veiculo, data da publicacao1, data da publicacao2, palavras chave)
                Set<String> keys = jsonObjectDeJava.keySet();
                Iterator iter = keys.iterator();
                while (iter.hasNext()) {
                    switch ((String) iter.next()) {
                        case "patrimonio":
//                            JsonObject key = jsonObjectDeJava.getJsonObject("patrimonio");
                            dados.setTitulo(jsonObjectDeJava.getString("patrimonio"));
                            break;
                    }
                }

                ArrayList<eel418_2016_1.DTOs.Produto> result = rb.buscarLista(dados);

//                        Agora é só responder...
                JSONArray resultJson = new JSONArray();
                for (int i = 0; i < result.size(); i++) {
                    resultJson.put(result.get(i).toJSON());
                }

                JSONObject resultObj = new JSONObject();
                resultObj.put("result", resultJson);

//                System.out.println("++++++++++++++++++++++" + resultObj);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print(resultObj);
                out.flush();
                break;

//            case "frete":
//                
//                System.out.println(jsonObjectDeJava.toString());
//                String CEP = jsonObjectDeJava.getString("CEP");
//                String URL = jsonObjectDeJava.getString("URL");
//                
//                String NOMEFARMACIA = jsonObjectDeJava.getString("NOMEFARMACIA");
//                
//                String resultFrete = rb.calcularFrete(URL, CEP, NOMEFARMACIA);
//
//                JSONObject resultObjFrete = new JSONObject();
//                resultObjFrete.put("result", resultFrete);
//
////                System.out.println("++++++++++++++++++++++" + resultObj);
//                response.setContentType("application/json;charset=UTF-8");
//                PrintWriter outFrete = response.getWriter();
//                outFrete.print(resultObjFrete);
//                outFrete.flush();
//                
//                break;

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
