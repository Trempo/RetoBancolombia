# RetoBancolombia

## Proyecto solución para el Reto BINTIC 2021

Este es un proyecto Maven de Intellij Idea. Se elaboró con JDK 13.

Para la realización de la solución se planteó elaborar una sola sentencia SQL que pudiera cumplir con N mesas y M filtros incluidos en un archivo de especificación. Se procesa el archivo y se construye la sentencia acordemente para luego ejecutarla. La sentencia no considera la diferencia de sexos, por lo que esto luego se corrige eliminando los clientes que sobran del sexo de mayor número.

Se utilizan 3 estructuras de datos para guardar el estado de los clientes, un ArrayList que contiene los clientes de la actual sentencia, un ArrayList de clientes miembros de mesas ya establecida y un HashMap que empareja códigos de clientes con montos.

Se consideró esta como la solución más óptima ya que se aprovecha de la posibilidad de optimizar una base datos por medio de buenos índices y prácticas.

Felipe Bedoya 2021.