import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentStructureSolution {

    // Clase interna para la demostracion de la solucion
    static class EventBuffer {
        // Solucion: ConcurrentLinkedQueue maneja la concurrencia internamente de forma segura.
        private final Queue<String> events = new ConcurrentLinkedQueue<>();

        public void addEvent(String event) {
            events.add(event);
        }

        public int size() {
            return events.size();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        EventBuffer buffer = new EventBuffer();

        int numberOfThreads = 10;
        int eventsPerThread = 10_000;
        Thread[] producers = new Thread[numberOfThreads];

        System.out.println("--- EJECUTANDO VERSION CON SOLUCION (ConcurrentLinkedQueue) ---");

        for (int i = 0; i < numberOfThreads; i++) {
            int producerId = i;
            producers[i] = new Thread(() -> {
                for (int j = 0; j < eventsPerThread; j++) {
                    buffer.addEvent("Producer-" + producerId + "-Event-" + j);
                }
            }, "Producer-" + i);
        }

        for (Thread producer : producers) {
            producer.start();
        }

        for (Thread producer : producers) {
            producer.join();
        }

        int expected = numberOfThreads * eventsPerThread;
        System.out.println("Eventos esperados: " + expected);
        System.out.println("Eventos reales...: " + buffer.size());

        if (expected == buffer.size()) {
            System.out.println("¡BRAVO! La estructura es Thread-Safe, no se perdio nada.");
        }
    }
}