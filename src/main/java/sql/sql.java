package sql;

import models.client;
import net.decryptAPI;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class sql {
    private String db = "jdbc:mariadb://localhost:3306/evalart_reto";
    private Connection conn;
    private PrintWriter printWriter;
    public sql(PrintWriter printWriter){
        this.printWriter = printWriter;
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/evalart_reto", "root", "Prometeo1177");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String queryBuilder(String caso){
        String queryParam = "";
        String[] split = caso.split("\n");
        for (int i = 0; i < split.length; i++) {
            if(i>0){
                queryParam+=" AND ";
            }
            String restriccion = split[i].split(":")[0];
            int valor = Integer.parseInt(split[i].split(":")[1]);
            switch (restriccion){
                case "UG":
                    queryParam+=" location = " + valor;
                    break;
                case "TC":
                    queryParam+= " type = " + valor;
                    break;
                case "RI":
                    queryParam+= " balance >= " + valor;
                    break;
                case "RF" :
                    queryParam+= " balance <= " + valor;
                    break;
                default:
                    break;
            }
        }
        Statement stmt = null;
        String query = String.format("SELECT * FROM\n" +
                "    (SELECT *\n" +
                "     FROM (SELECT c.id as id, c.code as code, c.male as male, c.type as type, c.location as location, c.company as company, c.encrypt as encrypt, unicos.max as balance\n" +
                "           FROM client c INNER JOIN account a on c.id = a.client_id\n" +
                "                         INNER JOIN (SELECT SUM(balance) as max, client_id, id\n" +
                "                                     FROM account\n" +
                "                                     GROUP BY client_id) unicos ON unicos.id = a.id\n" +
                "           WHERE %s \n" +
                "           GROUP BY company) t\n" +
                "    WHERE t.male = 1\n" +
                "    ORDER BY t.balance DESC, t.code\n" +
                "    LIMIT 4\n" +
                ") hombres\n" +
                "UNION\n" +
                "    (SELECT *\n" +
                "     FROM (SELECT c.id as id, c.code as code, c.male as male, c.type as type, c.location as location, c.company as company, c.encrypt as encrypt, unicos.max as balance\n" +
                "           FROM client c INNER JOIN account a on c.id = a.client_id\n" +
                "                         INNER JOIN (SELECT SUM(balance) as max, client_id, id\n" +
                "                                     FROM account\n" +
                "                                     GROUP BY client_id) unicos ON unicos.id = a.id\n" +
                "           WHERE %s \n" +
                "           GROUP BY company) t\n" +
                "     WHERE t.male = 0\n" +
                "     ORDER BY t.balance DESC, t.code\n" +
                "     LIMIT 4\n" +
                ")\n" +
                "ORDER BY balance desc;", queryParam,queryParam);
        return query;
    }
    public void generalQuery(String query){
        ArrayList<models.client> clientes = new ArrayList<>();
        HashMap<String, Double> mapaBalances = new HashMap<>();
        int mujeres=0;
        int hombres=0;
        try {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                int encrypt = Integer.parseInt(resultSet.getString("encrypt"));
                String code = resultSet.getString("code");
                if(encrypt==1){
                    code = decryptAPI.decrypt(code);
                }
                int id = Integer.parseInt(resultSet.getString("id"));
                int male = Integer.parseInt(resultSet.getString("male"));
                int type = Integer.parseInt(resultSet.getString("type"));
                int location = Integer.parseInt(resultSet.getString("location"));
                int company = Integer.parseInt(resultSet.getString("company"));
                double balance = Double.parseDouble(resultSet.getString("balance"));

                clientes.add(new client(id,code,male,type,location,company,encrypt));
                mapaBalances.put(code, balance);
                if(male==1){
                    hombres++;
                }else{
                    mujeres++;
                }

            }

            if(clientes.size()<4){
                printWriter.print("CANCELADA\n");
            }else {
                reqSexo(mujeres, hombres, clientes, mapaBalances);

                for (int i = 0; i < clientes.size(); i++) {
                    if (i > 0) {
                        printWriter.print(",");
                    }
                    printWriter.print(clientes.get(i).getCode());
                }
                printWriter.print("\n");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


    public void reqSexo(int mujeres, int hombres, ArrayList<models.client> clientes, HashMap<String, Double> mapaBalances){
        int dif = 0;
        int sexoMayor = 0;
        if(mujeres>hombres){
            dif = mujeres-hombres;
            sexoMayor = 0;

        }else if(hombres>mujeres){
            dif = hombres-mujeres;
            sexoMayor = 1;
        }
        for (int i = 0; i < dif; i++) {
            Integer min = null;
            for (int j = 0; j < clientes.size(); j++) {
                if(clientes.get(j).getMale()==sexoMayor){
                    min = j;
                }
            }
            if(min!=null){
                for (int j = 1; j < clientes.size(); j++) {
                    double balance = mapaBalances.get(clientes.get(j).getCode());
                    double balanceMin = mapaBalances.get(clientes.get(min).getCode());
                    if(balance<balanceMin && clientes.get(j).getMale()==sexoMayor){
                        min = j;
                    }
                }
                mapaBalances.remove(clientes.get(min));
                clientes.remove(clientes.get(min));

            }

        }
    }


}
