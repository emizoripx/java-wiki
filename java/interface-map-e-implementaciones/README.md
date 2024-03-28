# Interfaz Map y sus implementaciones

## Interfaz Map
Es una interfaz que representa una estructura de datos clave valor. En otras palabras asocia un valor con una clave 
que permite recuperar ese valor utilizando la clave asociada.

Al ser una interfaz no podemos crear objetos.

Las implementaciones de la interfaz Map son: HashMap, EnumMap, TreeMap, LinkedHashMap entre otras.

Ejemplo:

    import java.util.*;
    
    public class Main {
    public static void main(String[] args) {
    // Crear un Map utilizando HashMap
    Map<String, Integer> mapa = new HashMap<>();
    
            // Agregar elementos al mapa
            mapa.put("Juan", 25);
            mapa.put("María", 30);
            mapa.put("Pedro", 28);
    
            // Obtener el valor asociado a una clave
            int edadMaria = mapa.get("María");
            System.out.println("La edad de María es: " + edadMaria);
    
            // Iterar sobre las claves del mapa
            for (String nombre : mapa.keySet()) {
                System.out.println("Nombre: " + nombre + ", Edad: " + mapa.get(nombre));
            }
        }
    }

El ejemplo crea un Map usando la implementacion HashMap que asocia nombres de personas con sus edades,
la manera de agregar valores a tu map es usando el metodo put(), para recuperar el valor se usa el metodo get(),
finalente para imprimir todos los valores del map usamos el metod keySet

### Métodos De Mapa
La interfaz Map incluye todos los métodos del Collection interfaz. Es porque Collection es una súper interfaz de Map .

Además de los métodos disponibles en el Collection interfaz, el Map La interfaz también incluye los siguientes métodos:

- **put(K, V)** - Inserta la asociación de una clave K y un valor V en el mapa. Si la clave ya está presente, el nuevo valor reemplaza el valor anterior.
- **putAll()** - Inserta todas las entradas del mapa especificado en este mapa.
- **putIfAbsent(K, V)** - Inserta la asociación si la clave K aún no está asociado con el valor V .
- **get(K)** - Devuelve el valor asociado a la clave especificada K . Si no se encuentra la clave, devuelve null .
- **getOrDefault(K, valor predeterminado)** - Devuelve el valor asociado a la clave especificada K . Si no se encuentra la clave, devuelve el defaultValue .
- **containsKey(K)** - Comprueba si la clave especificada K está presente en el mapa o no.
- **containsValue(V)** - Comprueba si el valor especificado V está presente en el mapa o no.
- **replace(K, V)** - Reemplazar el valor de la clave K con el nuevo valor especificado V .
- **replace(K, valor anterior, valor nuevo)** - Reemplaza el valor de la clave K con el nuevo valor nuevoValor solo si la clave K está asociado con el valor oldValue .
- **remove(K)** - Elimina la entrada del mapa representada por la clave K .
- **remove(K, V)** - Elimina la entrada del mapa que tiene clave K asociado con el valor V .
- **keySet()** - Devuelve un conjunto de todas las claves presentes en un mapa.
- **values()** - Devuelve un conjunto de todos los valores presentes en un mapa.
- **entrySet()** - Devuelve un conjunto de todas las asignaciones clave/valor presentes en un mapa.

## Implementaciones

### **HashMap** 
Es la implementación más utilizada de la interfaz Map. Almacena los pares clave-valor en una tabla hash, lo que proporciona un acceso rápido a los elementos. No garantiza el orden de los elementos.

#### Ventajas
- Alta velocidad de búsqueda y recuperación: HashMap proporciona un acceso rápido a los elementos a través de la tabla hash, lo que lo hace ideal para operaciones de búsqueda y recuperación.
- Eficiencia en el uso de memoria: HashMap tiene un consumo de memoria eficiente y es adecuado para grandes conjuntos de datos.

#### Desventajas
- No ordenado: Los elementos en un HashMap no tienen un orden predecible. Si necesitas iterar sobre los elementos en un orden específico, como el orden de inserción o un orden basado en la clave, un HashMap no será adecuado.
- Sincronización: HashMap no es sincronizado, lo que significa que no es seguro para subprocesos. Si necesitas un Map que pueda ser accedido concurrentemente por múltiples hilos, tendrás que sincronizar manualmente el acceso 
a través de bloqueos o utilizar una implementación sincronizada como Collections.synchronizedMap().

**Cuando usar?**

- Se utiliza comúnmente cuando no se requiere un orden específico de los elementos.
- Proporciona un acceso rápido a los elementos a través de la tabla hash.
- Es adecuado para la mayoría de las situaciones generales donde la clave no necesita un orden particular.


### EnumMap
Es una implementación especializada de la interfaz Map en Java que está diseñada para su uso con tipos de enumeración (enum). Esta implementación está altamente optimizada para el rendimiento y el uso eficiente de la memoria cuando se utiliza con enumeraciones como claves.

#### Ventajas
- Eficiencia y rendimiento optimizados: EnumMap está altamente optimizado para trabajar con tipos de enumeración como claves, lo que significa que proporciona un rendimiento rápido y eficiente en comparación con otras implementaciones de Map cuando se utilizan enumeraciones.
- Uso eficiente de la memoria: Debido a su implementación interna especializada, EnumMap puede ser más eficiente en el uso de la memoria en comparación con otras implementaciones de Map, especialmente cuando se trabaja con un gran número de claves de enumeración.
- Ordenamiento natural de claves: EnumMap mantiene un orden natural de las claves basado en el orden en que las constantes de la enumeración están declaradas en el código fuente. Esto puede ser útil si necesitas iterar sobre las claves en un orden específico.
- Tipo seguro: Al utilizar enumeraciones como claves, EnumMap proporciona un alto nivel de seguridad de tipos. Esto significa que puedes evitar errores de programación relacionados con tipos al utilizar EnumMap.

#### Desventajas
- Limitado a enumeraciones como claves: La principal desventaja de EnumMap es que está diseñado específicamente para trabajar con tipos de enumeración como claves. Esto significa que no puedes utilizar EnumMap con otros tipos de claves, como cadenas o números.
- No permite claves nulas: EnumMap no permite claves nulas, ya que está diseñado para trabajar con enumeraciones, donde cada constante de la enumeración es una clave válida. Si necesitas permitir claves nulas, EnumMap no sería la elección adecuada.
- Requiere que las claves sean constantes de enumeración: Para utilizar EnumMap, debes tener una enumeración definida con un conjunto fijo de constantes. Si no tienes una enumeración adecuada, EnumMap no sería útil en tu caso.

**Cuando usar?**
- Asociación de datos con constantes de enumeración: Si tienes una enumeración que representa un conjunto fijo de constantes relacionadas y necesitas asociar algún tipo de datos con cada una de esas constantes, EnumMap es una elección natural. Por ejemplo, días de la semana, meses del año, tipos de moneda, etc.
- Eficiencia y rendimiento: Cuando estás trabajando con enumeraciones como claves, EnumMap puede proporcionar un rendimiento superior y un uso más eficiente de la memoria en comparación con otras implementaciones de Map. Si la eficiencia es una preocupación y estás trabajando exclusivamente con enumeraciones, EnumMap es una opción sólida.
- Seguridad de tipos: EnumMap ofrece un alto nivel de seguridad de tipos, ya que garantiza que solo se puedan utilizar constantes válidas de la enumeración como claves. Esto puede ayudar a evitar errores de programación relacionados con tipos al asociar datos con enumeraciones.
- Ordenamiento natural de claves: Si necesitas mantener un orden natural de las claves basado en el orden de declaración en la enumeración, EnumMap es una buena elección. Puedes confiar en que las iteraciones sobre un EnumMap proporcionarán los elementos en el orden en que fueron declarados en la enumeración.

### **TreeMap** 
Implementa la interfaz NavigableMap y utiliza un árbol rojo-negro para almacenar los pares clave-valor. Garantiza un ordenamiento natural de las claves o permite especificar un comparador personalizado para el ordenamiento.

#### Ventajas
- Ordenamiento natural o personalizado: TreeMap garantiza un ordenamiento natural de las claves o permite especificar un comparador personalizado para el ordenamiento, lo que lo hace útil en situaciones donde se necesita un orden específico de los elementos.
- Eficiencia en operaciones ordenadas: TreeMap proporciona operaciones de búsqueda, inserción y eliminación eficientes en conjuntos de datos ordenados.

#### Desventajas
- Rendimiento de inserción y eliminación: Aunque TreeMap proporciona un ordenamiento natural de las claves o un ordenamiento personalizado, las operaciones de inserción y eliminación son más lentas en comparación con HashMap. Esto se debe a la necesidad de mantener la estructura del árbol rojo-negro para garantizar el orden.
- Uso de memoria: TreeMap puede consumir más memoria que HashMap debido a la estructura del árbol rojo-negro, especialmente cuando el mapa contiene una gran cantidad de elementos.

**Cuando usar?**

- Se utiliza cuando necesitas un ordenamiento natural de las claves o un ordenamiento personalizado definido por un comparador.
- Útil cuando necesitas iterar sobre los elementos en un orden específico, como orden alfabético o numérico.

### **LinkedHashMap**
Esta implementación mantiene un orden de inserción de los elementos, además de permitir un acceso rápido a través de una estructura de lista doblemente enlazada. Combina las características de HashMap con un registro del orden de inserción.

#### Ventajas
- Mantenimiento del orden de inserción: LinkedHashMap mantiene el orden de inserción de los elementos, lo que es útil cuando necesitas iterar sobre los elementos en el mismo orden en que fueron insertados.
- Rendimiento predecible en operaciones de iteración: LinkedHashMap proporciona un rendimiento predecible en operaciones de iteración y es más rápido que TreeMap en operaciones que no requieren ordenamiento.

#### Desventajas
- Consumo de memoria: LinkedHashMap puede consumir más memoria que HashMap debido a la necesidad de mantener una lista doblemente enlazada para mantener el orden de inserción.
- Rendimiento de iteración: Aunque LinkedHashMap mantiene el orden de inserción, las operaciones de iteración pueden ser ligeramente más lentas que en HashMap debido al mantenimiento de la lista enlazada.

**Cuando usar?**

- Se utiliza cuando necesitas mantener el orden de inserción de los elementos.
- Útil cuando necesitas iterar sobre los elementos en el mismo orden en que fueron insertados.
- También proporciona un acceso rápido a los elementos a través de la tabla hash.

### **ConcurrentHashMap** 
Es una versión concurrente de HashMap, diseñada para admitir operaciones concurrentes sin bloqueos. Proporciona un alto rendimiento para aplicaciones con múltiples hilos de ejecución.

#### Ventajas
- Acceso seguro para subprocesos: ConcurrentHashMap proporciona acceso seguro para subprocesos sin necesidad de sincronización adicional, lo que lo hace ideal para entornos de programación concurrente.
- Alto rendimiento en operaciones concurrentes: ConcurrentHashMap proporciona un alto rendimiento para operaciones concurrentes sin bloqueos, lo que lo hace adecuado para aplicaciones con múltiples hilos de ejecución.

#### Desventajas
- Complejidad de uso: Aunque ConcurrentHashMap proporciona acceso seguro para subprocesos sin necesidad de sincronización adicional, su uso correcto puede ser más complejo que otras implementaciones debido a las garantías de consistencia débil y la necesidad de comprender los detalles de su comportamiento concurrente.

**Cuando usar?**

- Se utiliza en entornos multi-hilo donde múltiples hilos pueden acceder y modificar el mapa concurrentemente.
- Proporciona un alto rendimiento para operaciones concurrentes sin bloqueos.
- Útil cuando se necesita una estructura de datos de mapa segura para hilos.

### **WeakHashMap**
Implementa una tabla hash basada en referencias débiles. Las claves en un WeakHashMap son referencias débiles, lo que significa que pueden ser recolectadas por el recolector de basura si no hay otras referencias fuertes a ellas.

#### Ventajas
- Asociación de información con objetos débiles: WeakHashMap es útil para asociar información con objetos que pueden ser recolectados por el recolector de basura cuando ya no son referenciados, lo que facilita la gestión de memoria en ciertos escenarios.

#### Desventajas
- Recogida de basura: Aunque WeakHashMap es útil para asociar información con objetos que pueden ser recolectados por el recolector de basura, debes tener cuidado al usarlo, ya que las entradas débiles pueden ser eliminadas en cualquier momento durante la recolección de basura, lo que puede afectar el comportamiento esperado de tu aplicación.

**Cuando usar?**

- Se utiliza cuando necesitas asociar información con objetos que pueden ser recolectados por el recolector de basura cuando ya no son referenciados.
- Útil para implementar cachés o estructuras de datos donde los objetos deben ser eliminados cuando no están en uso.

### **IdentityHashMap** 
Es una implementación de Map que compara las claves utilizando el operador de identidad (==) en lugar del método equals(). Es útil en situaciones donde la igualdad de objetos se define por su identidad en lugar de su estado.

#### Ventajas
- Comparación basada en identidad: IdentityHashMap compara las claves utilizando la identidad (==) en lugar del método equals(), lo que lo hace útil en situaciones donde la igualdad de objetos se define por su identidad en lugar de su estado.

#### Desventajas
- Comparación basada en identidad: IdentityHashMap compara las claves utilizando la identidad (==) en lugar del método equals(). Esto puede llevar a resultados inesperados si estás acostumbrado a usar la igualdad basada en el método equals() y no tienes en cuenta la identidad de los objetos.

**Cuando usar?**

- Se utiliza cuando necesitas comparar las claves utilizando la identidad de los objetos (operador ==) en lugar del método equals().
- Útil en situaciones donde la igualdad de objetos se define por su identidad en lugar de su estado.
