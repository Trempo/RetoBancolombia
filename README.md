# RetoBancolombia

## Proyecto solución para el Reto BINTIC 2021

Este es un proyecto Maven de Intellij Idea. Se elaboró con JDK 13.

Para la solución se elaboró una sola sentencia SQL que cumpla con N mesas y M filtros incluidos en un archivo de especificación. Se procesa el archivo y se construye la sentencia para luego ejecutarla. La sentencia no considera la diferencia de sexos, por lo que se corrige eliminando los clientes que sobran del sexo de mayor número.

La solución es universal y funciona con cualquier filtro para cualquier mesa. Solo se tiene que alterar el archivo de entrada. Los resultados se observan en el archivo de salida. Ambos archivos están en la carpeta io del proyecto.

Se utilizan 3 estructuras de datos para guardar el estado de los clientes: un ArrayList que contiene los clientes de la actual sentencia, un ArrayList de clientes miembros de mesas ya establecidas y un HashMap que empareja códigos de clientes con montos.

Se considera esta la solución más óptima, ya que se aprovecha de la posibilidad de optimizar una base datos por medio de buenos índices y buenas prácticas.

Realizado por Felipe Bedoya.
GitHub: Trempo
Linkedin: https://www.linkedin.com/in/felipe-bedoya-65570121a/