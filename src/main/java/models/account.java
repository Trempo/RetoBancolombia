package models;

/**
 * Objeto que modela un registro de la tabla account.
 */
public class account {
    //Identificador único de la cuenta
    private int id;

    //Referencia al cliente dueño de la cuenta
    private int client_id;

    //Balance de la cuenta
    private double balance;

    /**
     * Constructor con parametros
     * @param id, id de la cuenta
     * @param client_id, id del cliente dueño
     * @param balance, balance de la cuenta
     */
    public account(int id, int client_id, double balance) {
        this.id = id;
        this.client_id = client_id;
        this.balance = balance;
    }

    //Getters y setters

    /**
     * Se retorna el id de la cuenta
     * @return id de la cuenta
     */
    public int getId() {
        return id;
    }

    /**
     * Se establece el id de la cuenta
     * @param id, nuevo id de la cuenta
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Se retorna el id del cliente dueño
     * de la cuenta
     * @return el id del cliente dueño de la cuenta
     */
    public int getClient_id() {
        return client_id;
    }

    /**
     * Se establece el id del cliente dueño
     * @param client_id, el nuevo id del cliente dueño
     */
    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    /**
     * Se retorna el balance de la cuenta
     * @return el balance de la cuenta
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Se cambia el balance de la cuenta
     * @param balance, nuevo balance de la cuenta
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
