package eel418_2016_1.DTOs;

import java.io.Serializable;
import java.util.ArrayList;

public class ListaDeReferencias implements Serializable{
    private ArrayList<Produto> listaReferencias = new ArrayList<>();

    public void add(Produto ref){
        listaReferencias.add(ref);
    }
    
    public ArrayList<Produto> getListaReferencias() {
        return listaReferencias;
    }

    public void setListaReferencias(ArrayList<Produto> listaReferencias) {
        this.listaReferencias = listaReferencias;
    }


}
