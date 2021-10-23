import sql.sql;

import java.io.*;

/**
 * Reto BINTIC 2021 - Bancolombia 22 - 25 de octubre 2021.
 *
 * Realizado por Felipe Bedoya.
 * GitHub: Trempo
 * Linkedin: https://www.linkedin.com/in/felipe-bedoya-65570121a/
 *
 * Este es el punto de entrada para el programa solución de la especificación dada. Se utiliza principalmente para
 * cargar los datos, generar los archivos de salida y llamar las subrutinas de consultas SQL.
 *
 * La solución es reactiva a cualquier archivo de filtro o especificación de las mesas, incluso a aquellos que tengan más
 * mesas y multiples filtros.
 *
 */
public class Main {
    public static void main( String[] args ) {

        //Ubicaciones de los archivos de salida y entrada
        String salida = "./io/salida.txt";
        String entrada = "./io/entrada.txt";

        // Objetos de escritura a salida txt
        FileWriter write;
        try {
            write = new FileWriter(salida, false);
            PrintWriter printWriter = new PrintWriter(write);
            sql sql = new sql(printWriter);

            File archivo = new File(entrada);

            try {
                //Objetos de lectura de la entrada txt
                BufferedReader reader = new BufferedReader(new FileReader(archivo));
                String linea = reader.readLine();
                String query = "";

                while(linea!=null){
                    //Se reconoce si es un titulo de grupo con el caracter '<'
                    if(linea.charAt(0)=='<'){
                        if(!query.equals("")){
                            /**
                             * Como termina el listado de filtros del grupo anterior
                             * se ejecuta una consulta general con los filtros acumulados
                             * que el método queryBuilder convierte en una sentencia viable.
                             */
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

