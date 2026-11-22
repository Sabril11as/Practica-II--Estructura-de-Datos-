import java.util.Scanner;
public class Main {

    private static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("        ÁRBOL B+  -  SIMULADOR       ");
        System.out.println("=====================================");

        int orden = leerEntero("Orden del árbol (mínimo 3, por ejemplo 4): ", 3, Integer.MAX_VALUE);
        ArbolBMas arbol = new ArbolBMas(orden);
        menu(arbol);

        System.out.println("¡Hasta pronto!");
    }

    private static void menu(ArbolBMas arbol) {
        int opcion;
        do {
            System.out.println();
            System.out.println("------------- MENÚ -------------");
            System.out.println(" 1. Insertar clave y dato");
            System.out.println(" 2. Eliminar clave");
            System.out.println(" 3. Buscar clave");
            System.out.println(" 4. Recorrer un rango de elementos");
            System.out.println(" 5. Mostrar el árbol");
            System.out.println(" 6. Mostrar las hojas enlazadas");
            System.out.println(" 7. Cargar datos de ejemplo");
            System.out.println(" 0. Salir");
            System.out.println("--------------------------------");
            opcion = leerEntero("Elija una opción: ", 0, 7);
            System.out.println();

            switch (opcion) {
                case 1 -> insertar(arbol);
                case 2 -> eliminar(arbol);
                case 3 -> buscar(arbol);
                case 4 -> recorrerRango(arbol);
                case 5 -> arbol.imprimirArbol();
                case 6 -> arbol.imprimirHojas();
                case 7 -> cargarEjemplo(arbol);
                default -> { }
            }
        } while (opcion != 0);
    }

    private static void insertar(ArbolBMas arbol) {
        int clave = leerEntero("Clave (número entero): ");
        System.out.print("Dato asociado: ");
        String dato = entrada.nextLine().trim();

        if (arbol.insertar(clave, dato)) {
            System.out.println("Se insertó la clave " + clave + ".");
            arbol.imprimirArbol();
        } else {
            System.out.println("La clave " + clave + " ya existe; no se insertó.");
        }
    }

    private static void eliminar(ArbolBMas arbol) {
        int clave = leerEntero("Clave a eliminar: ");

        if (arbol.eliminar(clave)) {
            System.out.println("Se eliminó la clave " + clave + ".");
            arbol.imprimirArbol();
        } else {
            System.out.println("La clave " + clave + " no existe en el árbol.");
        }
    }

    private static void buscar(ArbolBMas arbol) {
        int clave = leerEntero("Clave a buscar: ");

        if (arbol.buscar(clave)) {
            System.out.println("Encontrada: " + clave + " -> " + arbol.obtenerDato(clave));
        } else {
            System.out.println("La clave " + clave + " no existe en el árbol.");
        }
    }

    private static void recorrerRango(ArbolBMas arbol) {
        int clave = leerEntero("Clave inicial: ");
        int n = leerEntero("Cantidad de elementos a mostrar: ", 1, Integer.MAX_VALUE);
        System.out.println();
        arbol.recorrerRango(clave, n);
    }

    private static void cargarEjemplo(ArbolBMas arbol) {
        int[] claves = {10, 20, 5, 6, 15, 30, 25, 40, 45, 50};
        int nuevas = 0;
        for (int clave : claves) {
            if (arbol.insertar(clave, "Dato " + clave)) {
                nuevas++;
            }
        }
        System.out.println("Se cargaron " + nuevas + " claves de ejemplo (las repetidas se omiten).");
        arbol.imprimirArbol();
    }

    private static int leerEntero(String mensaje) {
        return leerEntero(mensaje, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("  Valor fuera de rango. Debe estar entre " + minimo + " y "
                        + (maximo == Integer.MAX_VALUE ? "el máximo permitido" : String.valueOf(maximo)) + ".");
            } catch (NumberFormatException e) {
                System.out.println("  Entrada inválida. Escriba un número entero.");
            }
        }
    }
}