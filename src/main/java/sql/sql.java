package sql;

import models.client;
import net.decryptAPI;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase que conecta con la base de datos y ejecuta las consultas en la base de datos.
 *
 * Base de Datos:
 * -- Host:                         127.0.0.1
 * -- Versión del servidor:         10.4.13-MariaDB - mariadb.org binary distribution
 * -- SO del servidor:              Win64
 * -- HeidiSQL Versión:             11.1.0.6116
 * -- user:                         "root"
 * -- password:                     ""
 * -- JDBC:                         "jdbc:mariadb://localhost:3306/evalart_reto"
 */
public class sql {
    //Objeto de conexión con la base de datos.
    private Connection conn;

    //Objeto para escribir al archivo de salida
    private final PrintWriter printWriter;

    //Historial de clientes ya invitados y miembros de otras mesas
    private final ArrayList<Integer> clientesInvitados;

    /**
     * Constructor con parametros
     * @param printWriter, objeto para escribir al archivo de salida preconfigurado desde el main.
     */
    public sql(PrintWriter printWriter){

        //Inicialización de los objetos y la conexión
        clientesInvitados = new ArrayList<>();
        this.printWriter = printWriter;
        try {
            String db = "jdbc:mariadb://localhost:3306/evalart_reto";
            conn = DriverManager.getConnection(db, "root", "");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Metodo que construye una consulta SQL a partir de filtros pasados como parametro y clientes en otras mesas.
     * @param caso, los filtros de la consulta
     * @return la consulta como un String
     */
    public String queryBuilder(String caso){
        /**
         * Parametro que sera concatenado al String de la consulta.
         * Se recorren los asistentes confirmados para añadir una condicion que los evite.
         */
        String excluirYaInvitados = clientesInvitados.size()>0? " WHERE ": "";
        for (int i = 0; i < clientesInvitados.size(); i++) {
            if(i>0){
                excluirYaInvitados+= " AND ";
            }
            excluirYaInvitados+= " client_id <> " + clientesInvitados.get(i) + " \n";
        }

        /**
         * Parametro que sera contatenado al String de la consulta.
         * Se construye a partir de los filtros pasados por parametro. Se procesan y dividen para encontra el identificador
         * de la restriccion y el valor de la misma.
         */
        String queryParam = "";
        String[] split = caso.split("\n");
        for (int i = 0; i < split.length; i++) {
            if(i>0){
                queryParam+=" AND ";
            }
            String restriccion = split[i].split(":")[0];
            int valor = Integer.parseInt(split[i].split(":")[1]);

            //Switch que concatena una condicion dependiendo del filtro.
            switch (restriccion){
                case "UG":
                    queryParam+=" location = " + valor;
                    break;
                case "TC":
                    queryParam+= " type = " + valor;
                    break;
                case "RI":
                    queryParam+= " unicos.monto >= " + valor;
                    break;
                case "RF" :
                    queryParam+= " unicos.monto <= " + valor;
                    break;
                default:
                    break;
            }
        }

        /**
         * Consulta general propuesta como solución para el Reto BINTIC 2019.
         * La consulta es de mi autoría (Felipe Bedoya). Se hacen consultas independientes para hombres y mujeres
         * de maximo 4 filas, ya que maximo puede haber 4 y 4 de cada sexo. Dentro de estas consultas hay subconsultas
         * que (yendo desde mas aninada a mas externa) suman los montos de todas las cuentas de un usuario, luego incluyen los
         * filtros construidos anteriormente y luego se ordena por monto.
         *
         * Lo único de la especificación que la consulta no considera es mismo número de hombres y mujeres. Esto lo
         * maneja generalQuery().
         *
         */
        String query =  String.format("SELECT * FROM\n" +
                "    (SELECT *\n" +
                "     FROM (SELECT c.*, unicos.monto as balance\n" +
                "           FROM client c INNER JOIN account a on c.id = a.client_id\n" +
                "                         INNER JOIN (SELECT SUM(balance) as monto, client_id, id\n" +
                "                                     FROM account\n" +
                "                                     %s \n" +
                "                                     GROUP BY client_id) unicos ON unicos.id = a.id\n" +
                "           WHERE %s \n" +
                "           GROUP BY company) t\n" +
                "    WHERE t.male = 1\n" +
                "    ORDER BY t.balance DESC, t.code ASC\n" +
                "    LIMIT 4\n" +
                ") hombres\n" +
                "UNION\n" +
                "    (SELECT *\n" +
                "     FROM (SELECT c.*, unicos.monto as balance\n" +
                "           FROM client c INNER JOIN account a on c.id = a.client_id\n" +
                "                         INNER JOIN (SELECT SUM(balance) as monto, client_id, id\n" +
                "                                     FROM account\n" +
                "                                     %s \n" +
                "                                     GROUP BY client_id) unicos ON unicos.id = a.id\n" +
                "           WHERE %s \n" +
                "           GROUP BY company) t\n" +
                "     WHERE t.male = 0\n" +
                "     ORDER BY t.balance DESC, t.code ASC\n" +
                "     LIMIT 4\n" +
                ")\n" +
                "ORDER BY balance desc, code ASC;", excluirYaInvitados,queryParam,excluirYaInvitados,queryParam);
        return query;
    }

    /**
     * Método que realiza la consulta en la base de datos y procesa la respuesta. Se mapean los resultados a objetos de
     * tipo cliente, incluyen condiciones como la de cancelación y se llama al reqSexo() para cumplir con el requerimiento
     * de mismo número de hombres y mujeres.
     * @param query el query construido por el queryBuilder()
     */
    public void generalQuery(String query){

        //Objeto que va a almacenar los clientes retornados por la sentencia
        ArrayList<models.client> clientes = new ArrayList<>();

        //Estructura que empareja codigos de clientes con sus montos.
        HashMap<String, Double> mapaBalances = new HashMap<>();

        //Conteo de mujeres y hombres en la actual consulta
        int mujeres=0;
        int hombres=0;
        try {

            //Creacion del objeto de sentencia y ejecucion.
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            //Recorrer las filas del resultado y se mapean a variables u objetos
            while (resultSet.next()) {
                int encrypt = Integer.parseInt(resultSet.getString("encrypt"));
                String code = resultSet.getString("code");

                //Si el codigo esta encriptado se llama al servicio Http que lo desencripta.
                if(encrypt==1){
                    code = decryptAPI.decrypt(code);
                }
                int id = Integer.parseInt(resultSet.getString("id"));
                int male = Integer.parseInt(resultSet.getString("male"));
                int type = Integer.parseInt(resultSet.getString("type"));
                int location = Integer.parseInt(resultSet.getString("location"));
                int company = Integer.parseInt(resultSet.getString("company"));
                double balance = Double.parseDouble(resultSet.getString("balance"));

                //Se añaden los clientes y balances a las estructuras establecidas
                clientes.add(new client(id,code,male,type,location,company,encrypt));
                mapaBalances.put(code, balance);

                //Actualiza el estado de sexo
                if(male==1){
                    hombres++;
                }else{
                    mujeres++;
                }

            }

            //Condiciones de cancelacion
            if(clientes.size()<4){
                printWriter.print("CANCELADA\n");
            }else {
                //Correccion por requerimiento de equivalencia de sexo
                reqSexo(mujeres, hombres, clientes, mapaBalances);

                //Se escribe el resultado en el archivo de salida y se actualizan los clientes invitados

                for (int i = 0; i < clientes.size(); i++) {
                    if (i > 0) {
                        printWriter.print(",");
                    }
                    printWriter.print(clientes.get(i).getCode());
                    clientesInvitados.add(clientes.get(i).getId());

                }
                printWriter.print("\n");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Metodo que corrige los clientes considerados para una mesa por el criterio de equivalencia de sexo
     * @param mujeres número actual de mujeres en la consulta
     * @param hombres número actual de hombres en la consulta
     * @param clientes estructura que almacena clientes
     * @param mapaBalances estructura que almacena balances
     */
    public void reqSexo(int mujeres, int hombres, ArrayList<models.client> clientes, HashMap<String, Double> mapaBalances){
        int dif = 0;
        int sexoMayor = 0;
        //Se calcula el excedente de los dos sexos y el sexo mayor.
        if(mujeres>hombres){
            dif = mujeres-hombres;

        }else if(hombres>mujeres){
            dif = hombres-mujeres;
            sexoMayor = 1;
        }

        /**
         * Por cada uno de los clientes excedentes del sexo mayor, se escoge el que tenga menor monto y se elimina de
         * la estructura.
         */
        for (int i = 0; i < dif; i++) {
            Integer min = null;
            //Búsqueda de un primer cliente para comparar
            for (int j = 0; j < clientes.size(); j++) {
                if(clientes.get(j).getMale()==sexoMayor){
                    min = j;
                }
            }
            if(min!=null){
                //Comparación de los clientes del sexo dado
                for (int j = 1; j < clientes.size(); j++) {
                    double balance = mapaBalances.get(clientes.get(j).getCode());
                    double balanceMin = mapaBalances.get(clientes.get(min).getCode());
                    if(balance<balanceMin && clientes.get(j).getMale()==sexoMayor){
                        min = j;
                    }
                }

                //Actualización de las estructuras
                mapaBalances.remove(clientes.get(min).getCode());
                clientes.remove(clientes.get(min));

            }

        }
    }


}
