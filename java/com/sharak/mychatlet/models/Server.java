package com.sharak.mychatlet.models;

public class Server {

    private String hostname;
    private int puerto;

    public static final String DEFAULT_HOSTNAME = "localhost";
    public static final int DEFAULT_PUERTO = 50;

    public Server(){ }

    public Server(String hostname,int puerto){
        this.hostname = hostname;
        this.puerto = puerto;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

}
