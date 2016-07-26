package eel418_2016_1.DTOs;

import java.io.Serializable;
import javax.json.Json;
import javax.json.JsonObject;
import org.json.JSONObject;

public class Produto implements Serializable {

    private String titulo;
    private String url;
    private String linkimg;
    private String preco;
    private String nomefarmacia;
    private String urlfarmacia;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLinkimg() {
        return linkimg;
    }

    public void setLinkimg(String linkimg) {
        this.linkimg = linkimg;
    }

    public String getNomeFarmacia() {
        return nomefarmacia;
    }

    public void setNomeFarmacia(String nomefarmacia) {
        this.nomefarmacia = nomefarmacia;
    }

    public String getURLFarmacia() {
        return urlfarmacia;
    }

    public void setURLFarmacia(String urlfarmacia) {
        this.urlfarmacia = urlfarmacia;
    }

    public JSONObject toJSON() {
        JSONObject objetoJSON = new JSONObject();

        objetoJSON.put("nome", titulo);
        objetoJSON.put("url", url);
        objetoJSON.put("linkimg", linkimg);
        objetoJSON.put("preco", preco);
        objetoJSON.put("nomefarmacia", nomefarmacia);
        objetoJSON.put("urlfarmacia", urlfarmacia);

        return objetoJSON;
    }

}
