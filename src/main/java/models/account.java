package models;

/**
 * Objeto que modela un registro de la tabla account.
 */
public class account {
    //Identificador único de la cuenta
    private int id;

    //Referencia al cliente dueño de la cuenta
    private int client_id;
    private double balance;

    public account(int id, int client_id, double balance) {
        this.id = id;
        this.client_id = client_id;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
