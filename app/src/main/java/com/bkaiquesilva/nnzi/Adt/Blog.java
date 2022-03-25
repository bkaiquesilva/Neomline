package com.bkaiquesilva.nnzi.Adt;

public class Blog {
    private String IMAGE;
    private String uid;
    private String keys;
    private String identificador;
    private String enome;
    private String meu_identifica;
    private String nova_msn;

    public Blog(String IMAGE, String uid, String keys, String identificador, String enome, String meu_identifica, String nova_msn) {
        this.IMAGE = IMAGE;
        this.uid = uid;
        this.keys = keys;
        this.identificador = identificador;
        this.enome = enome;
        this.meu_identifica = meu_identifica;
        this.nova_msn = nova_msn;
    }

    public Blog() {

    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getEnome() {
        return enome;
    }

    public void setEnome(String enome) {
        this.enome = enome;
    }

    public String getMeu_identifica() {
        return meu_identifica;
    }

    public void setMeu_identifica(String meu_identifica) {
        this.meu_identifica = meu_identifica;
    }

    public String getNova_msn() {
        return nova_msn;
    }

    public void setNova_msn(String nova_msn) {
        this.nova_msn = nova_msn;
    }

}