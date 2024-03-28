import java.util.*;
import java.util.concurrent.*;
public class Examples {

    public void ejemploHashMap(){
        System.out.printf("*** ejemploHashMap ***");
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
        System.out.printf("*** END ***\n\n");
    }

    public void ejemploTreMap(){
        System.out.printf("*** ejemploTreMap ***");
        // Crear un TreeMap
        Map<String, Integer> mapa = new TreeMap<>();

        // Agregar elementos al mapa
        mapa.put("Juan", 25);
        mapa.put("Pedro", 28);
        mapa.put("María", 30);

        // Iterar sobre las entradas del mapa (ordenadas por clave)
        for (Map.Entry<String, Integer> entrada : mapa.entrySet()) {
            System.out.println("Nombre: " + entrada.getKey() + ", Edad: " + entrada.getValue());
        }
        System.out.printf("*** END ***\n\n");
    }

    public void ejemploLinkedHashMap(){
        System.out.printf("*** ejemploLinkedHashMap ***");
        // Crear un LinkedHashMap
        Map<String, Integer> mapa = new LinkedHashMap<>();

        // Agregar elementos al mapa
        mapa.put("Juan", 25);
        mapa.put("María", 30);
        mapa.put("Pedro", 28);

        // Iterar sobre las entradas del mapa (en orden de inserción)
        for (Map.Entry<String, Integer> entrada : mapa.entrySet()) {
            System.out.println("Nombre: " + entrada.getKey() + ", Edad: " + entrada.getValue());
        }
        System.out.printf("*** END ***\n\n");
    }

    public void ejemploConcurrentHashMap(){
        System.out.printf("*** ejemploConcurrentHashMap ***");
        ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();

        // Creamos e iniciamos varios hilos para agregar elementos al ConcurrentHashMap
        Thread thread1 = new Thread(new AddThread(concurrentHashMap,  "Juan", 25));
        Thread thread2 = new Thread(new AddThread(concurrentHashMap,  "Pedro", 30));
        Thread thread3 = new Thread(new AddThread(concurrentHashMap,  "María", 28));

        thread1.start();
        thread2.start();
        thread3.start();

        // Esperamos a que todos los hilos terminen
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Imprimimos el contenido del ConcurrentHashMap
        System.out.println("Contenido del ConcurrentHashMap:");
        concurrentHashMap.forEach((key, value) -> System.out.println("Nombre: " + key + ", Edad : " + value));
        System.out.printf("*** END ***\n\n");
    }

    public void ejemploWeakHashMap(){
        System.out.printf("*** ejemploWeakHashMap ***");
        // Crear un WeakHashMap
        WeakHashMap<String, Integer> mapa = new WeakHashMap<>();

        // Crear dos objetos String (clave)
        String juan = new String("Juan");
        String maria = new String("María");

        // Agregar elementos al mapa
        mapa.put(juan, 25);
        mapa.put(maria, 30);

        // Obtener el valor asociado a una clave
        int edadJuan = mapa.get(juan);
        System.out.println("La edad de Juan es: " + edadJuan);

        // Liberar las referencias a los objetos clave
        juan = null;

        // Solicitar la recolección de basura
        System.gc();

        // Iterar sobre las entradas del mapa
        for (Map.Entry<String, Integer> entrada : mapa.entrySet()) {
            System.out.println("Nombre: " + entrada.getKey() + ", Edad: " + entrada.getValue());
        }
        System.out.printf("*** END ***\n\n");
    }

    public void ejemploIdentityHashMap(){
        System.out.printf("*** ejemploIdentityHashMap ***");
        // Crear un IdentityHashMap
        IdentityHashMap<String, Integer> mapa = new IdentityHashMap<>();

        // Crear dos objetos String (clave)
        String juan1 = new String("Juan");
        String juan2 = new String("Juan");

        // Agregar elementos al mapa
        mapa.put(juan1, 25);
        mapa.put(juan2, 30);

        // Obtener el valor asociado a una clave
        int edadJuan1 = mapa.get(juan2);
        System.out.println("La edad de Juan (clave 2) es: " + edadJuan1);

        // Iterar sobre las entradas del mapa
        for (Map.Entry<String, Integer> entrada : mapa.entrySet()) {
            System.out.println("Nombre: " + entrada.getKey() + ", Edad: " + entrada.getValue());
        }
        System.out.printf("*** END ***\n\n");
    }

    public void ejemploEnumMap(){
        System.out.printf("*** ejemploEnumMap ***");
        // Crear un EnumMap para asociar actividades con cada día de la semana
        EnumMap<DiaSemana, String> actividadesPorDia = new EnumMap<>(DiaSemana.class);

        // Asociar actividades con cada día de la semana
        actividadesPorDia.put(DiaSemana.LUNES, "Ir al trabajo");
        actividadesPorDia.put(DiaSemana.MARTES, "Ir al gimnasio");
        actividadesPorDia.put(DiaSemana.MIERCOLES, "Cita con el médico");
        actividadesPorDia.put(DiaSemana.JUEVES, "Reunión con el equipo");
        actividadesPorDia.put(DiaSemana.VIERNES, "Salir a cenar con amigos");
        actividadesPorDia.put(DiaSemana.SABADO, "Día libre para actividades recreativas");
        actividadesPorDia.put(DiaSemana.DOMINGO, "Relajarse y descansar");

        // Mostrar las actividades de la semana
        for (Map.Entry<DiaSemana, String> entrada : actividadesPorDia.entrySet()) {
            System.out.println("Actividad para " + entrada.getKey() + ": " + entrada.getValue());
        }
        System.out.printf("*** END ***\n\n");
    }

}

enum DiaSemana {
    LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO
}

class AddThread implements Runnable {
    private ConcurrentHashMap<String, Integer> concurrentHashMap;
    private String nombre;
    private Integer edad;

    public AddThread(ConcurrentHashMap<String, Integer> concurrentHashMap, String nombre, Integer edad) {
        this.concurrentHashMap = concurrentHashMap;
        this.nombre = nombre;
        this.edad = edad;
    }

    @Override
    public void run() {
        concurrentHashMap.put(nombre, edad);
        System.out.println("Agregado: " + nombre + " : " + edad + " por " + Thread.currentThread().getName());
    }
}