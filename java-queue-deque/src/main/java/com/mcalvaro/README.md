# Interfaz Queue

- Una cola es una estructura de datos abstractos lineales con el orden particular de realizar operaciones: primero en entrar, primero en salir (FIFO).

![](https://cdn.codegym.cc/images/article/dce95449-a469-416c-8bb5-e7f2c820e2eb/512.webp)

- Queue en Java es una interfaz. La interfaz Queue tiene 2 superinterfaces, 4 interfaces diferentes que heredan de la cola
y una lista de clases que lo implementan.


### Superinterfaces
- `Colección<E>`
- `Iterable<E>`

### Subinterfaces
- `Deque<E>`
- `BlockingDeque<E>`
- `BlockingQueue<E>`
- `TransferQueue<E>`
### Implementaciones Conocidas
- `LinkedList`                  
- `PriorityQueue`
- `AbstractQueue`
- `ArrayBlockingQueue`
- `ArrayDeque`
- `ConcurrentLinkedDeque`
- `ConcurrentLinkedQueue`
- `DelayQueue`
- `LinkedBlockingDeque`
- `LinkedBlockingQueue`
- `LinkedTransferQueue`
- `PriorityBlockingQueue`
- `SynchronousQueue`

![](https://media.geeksforgeeks.org/wp-content/cdn-uploads/20200903183026/Queue-Deque-PriorityQueue-In-Java.png)

> La interfaz Queue está presente en el paquete java.util, se utiliza para contener los elementos que están a punto de procesarse en orden FIFO (primero en entrar, primero en salir)
> - Es una lista ordenada de objetos
- En Java, la interfaz Queue es un subtipo de la interfaz Collection y representa una colección de elementos en un orden específico

Al ser una interfaz, la cola necesita una clase concreta para la declaración y las clases más comunes son `PriorityQueue` y `LinkedList` en Java.
> Tenga en cuenta que ninguna de estas implementaciones es segura para subprocesos. `PriorityBlockingQueue` es una implementación alternativa si se necesita una implementación segura para subprocesos.

## Crear un Cola

```java
package com.mcalvaro;

import java.util.LinkedList;
import java.util.Queue;

public class MainApplication {
    public static void main(String[] args) {
        System.out.println("Queue Interface");

        Queue<String> queue = new LinkedList<>();
    }
}

```
- La interfaz Queue proporciona varios métodos para agregar, eliminar e inspeccionar elementos en la cola
    - `add(element)` -> Agrega un elemento al final de la cola. Si la cola está llena, genera una excepción.
    - `offer(element)` -> agrega un elemento al final de la cola. Si la cola está llena, devuelve falso
    - `remove()` -> elimina y devuelve el elemento al principio de la cola. Si la cola está vacía, genera una excepción
    - `poll()` -> elimina y devuelve el elemento al principio de la cola. Si la cola está vacía, devuelve nulo.
    - `element()` -> Devuelve el elemento al principio de la cola sin eliminarlo. Si la cola está vacía, genera una excepción.
    - `peek()` -> Devuelve el elemento al principio de la cola sin eliminarlo. Si la cola está vacía, devuelve nulo.

-----------------------------------
# Interfaz Deque
La interfaz Deque presente en el paquete java.util es un subtipo de la interfaz `Queue`

> Los Deques soportan la inserción y extracción del elemento por ambos extremos. 

Se puede utilizar como cola (primero en entrar, primero en salir/FIFO) o como pila (último en entrar, primero en salir/LIFO)

### Ventajas de utilizar Deque
1. **De doble extremo:** La principal ventaja de la interfaz Deque es que proporciona una cola de doble extremo, que permite agregar y eliminar elementos de ambos extremos de la cola 
2. **Flexibilidad:** La interfaz Deque proporciona una serie de métodos para agregar, eliminar y recuperar elementos de ambos extremos de la cola, lo que le brinda una gran flexibilidad en su uso.
3. **Operaciones de bloqueo**: La interfaz Deque proporciona métodos de bloqueo, como `takeFirst` y `takeLast`, que le permiten esperar a que los elementos estén disponibles o que haya espacio disponible en la cola.

### Desventajas de usar Deque
1. **Rendimiento**: el rendimiento de un Deque puede ser más lento que el de otras estructuras de datos, como una lista vinculada o una matriz, porque proporciona más funcionalidad.
2. **Dependiente de la implementación:** el comportamiento de un Deque puede depender de la implementación que utilice. Por ejemplo, algunas implementaciones pueden proporcionar operaciones seguras para subprocesos, mientras que otras no.

# Interfaz PriorityQueue
Se utiliza PriorityQueue cuando se supone que los objetos deben procesarse según la prioridad.
Los elementos de la cola de prioridad se ordenan según el orden natural o mediante un comparador proporcionado en el momento de la construcción de la cola, según el constructor que se utilice.

> La clase implementa las interfaces `Serializable` , `Iterable<E>` , `Collection<E>` , `Queue<E>` .

### Puntos importantes
- PriorityQueue no permite valores nulos.
- No podemos crear una PriorityQueue de objetos que no sean comparables. (Requiere que los objetos que almacena sean comparables, ya que necesita ordenarlos internamente según un criterio de prioridad)
- PriorityQueue son colas independientes (Cada instancia de PriorityQueue es independiente de las demás.).
- Si varios elementos tienen el mismo valor más bajo, uno de ellos se seleccionará de forma arbitraria para ser la cabeza.

- Dado que PriorityQueue no es seguro para subprocesos, Java proporciona la clase `PriorityBlockingQueue`
- Hereda métodos de las clases `AbstractQueue` , `AbstractCollection`` , `Collection` y `Object`.


`PriorityQueue()`: Crea una PriorityQueue con la capacidad inicial predeterminada (11) que ordena sus elementos según su orden natural.
```java
PriorityQueue<E> pq = nueva PriorityQueue<E>();
```
