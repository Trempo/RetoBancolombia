package models;

/**
 * Objeto que modela un registro de la tabla client.
 */
public class client {
    //Identificador único del cliente
    private int id;

    //Codigo unico del cliente
    private String code;

    //Sexo del cliente (1 - hombre, 0 - mujer)
    private int male;

    //Tipo del cliente
    private int type;

    //Ubicacion geográfica del cliente
    private int location;

    //Compañía del cliente
    private int company;

    //Si el cliente tiene contenido encriptado (1 - Si, 0 - No)
    private int encrypt;


    /**
     * Constructor con parametros
     * @param id, id del cliente
     * @param code, codigo del cliente
     * @param male, sexo del cliente
     * @param type, tipo del cliente
     * @param location, ubicacion geografica del cliente
     * @param company, compañia del cliente
     * @param encrypt, si tiene encriptacion
     */
    public client(int id, String code, int male, int type, int location, int company, int encrypt) {
        this.id = id;
        this.code = code;
        this.male = male;
        this.type = type;
        this.location = location;
        this.company = company;
        this.encrypt = encrypt;
    }

    /**
     * Retorna el id del cliente
     * @return id del cliente
     */
    public int getId() {
        return id;
    }

    /**
     * Cambia el id del cliente
     * @param id, nuevo id del cliente
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna el código del cliente
     * @return el código del cliente
     */
    public String getCode() {
        return code;
    }

    /**
     * Cambia el código del cliente
     * @param code, nuevo código del cliente
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Retorna el sexo del cliente
     * 1 - Hombre
     * 0 - Mujer
     * @return el sexo del cliente
     */
    public int getMale() {
        return male;
    }


    /**
     * Cambia el sexo del cliente
     * 1 - Hombre
     * 0 - Mujer
     * @param male, el nuevo sexo del cliente
     */
    public void setMale(int male) {
        this.male = male;
    }

    /**
     * Retorna el tipo del cliente
     * @return el tipo del cliente
     */
    public int getType() {
        return type;
    }

    /**
     * Cambia el tipo del cliente
     * @param type, el nuevo tipo del cliente
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Retorna la ubicación geográfica de cliente
     * @return la ubicación geográfica del cliente
     */
    public int getLocation() {
        return location;
    }

    /**
     * Cambia la ubicación geográfica del cliente
     * @param location, la nueva ubicación geográfica del cliente
     */
    public void setLocation(int location) {
        this.location = location;
    }


    /**
     * Retorna la compañía del cliente
     * @return la compañía del cliente
     */
    public int getCompany() {
        return company;
    }

    /**
     * Cambia la compañía del cliente
     * @param company, la nueva compañía del cliente
     */
    public void setCompany(int company) {
        this.company = company;
    }

    /**
     * Retorna si el cliente tiene contenido encriptado
     * 1 - Si
     * 0 - No
     * @return si esta encriptado el cliente o no
     */
    public int getEncrypt() {
        return encrypt;
    }

    /**
     * Cambia el estado de encriptación del cliente
     * 1 - Si
     * 0 - No
     * @param encrypt el nuevo valor de encriptación del cliente
     */
    public void setEncrypt(int encrypt) {
        this.encrypt = encrypt;
    }
}
