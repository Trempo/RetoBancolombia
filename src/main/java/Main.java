import sql.sql;

import java.io.*;
import java.sql.*;

public class Main {
    public static void main( String[] args ) throws SQLException, IOException {

        String salida = "./io/salida.txt";
        String entrada = "./io/entrada.txt";

        // Objetos de escritura al archivo txt
        FileWriter write = null;
        try {
            write = new FileWriter(salida, false);
            PrintWriter printWriter = new PrintWriter(write);
            sql sql = new sql(printWriter);

            File archivo = new File(entrada);

            try {
                BufferedReader reader = new BufferedReader(new FileReader(archivo));
                String linea = reader.readLine();
                String query = "";
                while(linea!=null){
                    if(linea.charAt(0)=='<'){
                        if(!query.equals("")){
                            sql.generalQuery(sql.queryBuilder(query));
                            query = "";
                        }
                        printWriter.print(linea + "\n");

                    }else{
                        query+=linea + "\n";
                    }
                    linea = reader.readLine();
                }
                sql.generalQuery(sql.queryBuilder(query));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

