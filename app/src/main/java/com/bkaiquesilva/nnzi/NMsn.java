package com.bkaiquesilva.nnzi;

public class NMsn {
    private String menssa;
    private String mident;
    private String dataa;
    private String imagem;
    private String audio;
    private String typo;
    private String visto;

    public NMsn(String menssa, String mident, String dataa, String imagem, String typo, String audio, String visto) {
        this.menssa = menssa;
        this.mident = mident;
        this.dataa = dataa;
        this.imagem = imagem;
        this.typo = typo;
        this.audio = audio;
        this.visto = visto;
    }

    public NMsn() {

    }

    public String getMenssa() {
        return menssa;
    }

    public void setMenssa(String menssa) {
        this.menssa = menssa;
    }

    public String getMident() {
        return mident;
    }

    public void setMident(String mident) {
        this.mident = mident;
    }

    public String getDataa() {
        return dataa;
    }

    public void setDataa(String dataa) {
        this.dataa = dataa;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getTypo() {
        return typo;
    }

    public void setTypo(String typo) {
        this.typo = typo;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVisto() {
        return visto;
    }

    public void setVisto(String visto) {
        this.visto = visto;
    }
}
