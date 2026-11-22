import java.util.ArrayList;
public class ArbolBMas {

    private final int orden;
    private NodoArbolBMas raiz;

    public ArbolBMas(int orden) {
        if (orden < 3) {
            throw new IllegalArgumentException("El orden mínimo de un árbol B+ es 3.");
        }
        this.orden = orden;
        raiz = new NodoArbolBMas(true);
    }

    public boolean insertar(int clave, String dato) {
        if (buscar(clave)) {
            return false;
        }

        insertarRecursivo(this.raiz, clave, dato);

        if(this.raiz.getClaves().size() == orden) {
            NodoArbolBMas nuevaRaiz = new NodoArbolBMas(false);
            nuevaRaiz.getHijos().add(this.raiz);
            dividirHijo(nuevaRaiz, 0);
            this.raiz = nuevaRaiz;
        }
        return true;
    }

    private void insertarRecursivo(NodoArbolBMas nodo, int llave, String dato) {
        int i = nodo.getClaves().size() - 1;

        if(nodo.esHoja()) {
            while(i >= 0 && llave < nodo.getClaves().get(i)) {
                i--;
            }
            nodo.getClaves().add(i + 1, llave);
            nodo.getDatos().add(i + 1, dato);
        } else {
            while(i >= 0 && llave < nodo.getClaves().get(i)) {
                i--;
            }
            i++;
            NodoArbolBMas hijo = nodo.getHijos().get(i);

            insertarRecursivo(hijo, llave, dato);

            if(hijo.getClaves().size() == orden) {
                dividirHijo(nodo, i);
            }
        }
    }
    private void dividirHijo(NodoArbolBMas padre, int indice) {
        NodoArbolBMas nodoLleno = padre.getHijos().get(indice);
        NodoArbolBMas nuevoNodo = new NodoArbolBMas(nodoLleno.esHoja());
        int mitad = orden / 2;

        if (nodoLleno.esHoja()) {
            nuevoNodo.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(mitad, nodoLleno.getClaves().size())));
            nodoLleno.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(0, mitad)));

            nuevoNodo.setDatos(new ArrayList<>(nodoLleno.getDatos().subList(mitad, nodoLleno.getDatos().size())));
            nodoLleno.setDatos(new ArrayList<>(nodoLleno.getDatos().subList(0, mitad)));

            nuevoNodo.setSiguiente(nodoLleno.getSiguiente());
            nodoLleno.setSiguiente(nuevoNodo);

            int llaveMedia = nuevoNodo.getClaves().getFirst();
            padre.getClaves().add(indice, llaveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        }
        else {
            int claveMedia = nodoLleno.getClaves().get(mitad);
            nuevoNodo.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(mitad + 1, nodoLleno.getClaves().size())));
            nodoLleno.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(0, mitad)));

            nuevoNodo.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(mitad + 1, nodoLleno.getHijos().size())));
            nodoLleno.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(0, mitad + 1)));

            padre.getClaves().add(indice, claveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        }
    }

    public void imprimirArbol() {
        imprimirNodo(raiz, "", true);
    }

    private void imprimirNodo(NodoArbolBMas nodo, String indentacion, boolean esUltimo) {
        System.out.println(indentacion + "+- " + (nodo.esHoja() ? "Hoja > " : "Interno > ") + nodo.getClaves());
        indentacion += esUltimo ? "   " : "|  ";
        for(int i = 0; i < nodo.getHijos().size(); i++) {
            imprimirNodo(nodo.getHijos().get(i), indentacion, i == (nodo.getHijos().size() - 1));
        }
    }

    public void imprimirHojas() {
        NodoArbolBMas hoja = raiz;
        while (!hoja.esHoja()) {
            hoja = hoja.getHijos().getFirst();
        }

        StringBuilder sb = new StringBuilder();
        while (hoja != null) {
            sb.append(hoja.getClaves());
            if (hoja.getSiguiente() != null) {
                sb.append(" -> ");
            }
            hoja = hoja.getSiguiente();
        }
        System.out.println(sb);
    }

    public boolean buscar(int llave) {
        return encontrarHoja(llave).getClaves().contains(llave);
    }

    public String obtenerDato(int llave) {
        NodoArbolBMas hoja = encontrarHoja(llave);
        int posicion = hoja.getClaves().indexOf(llave);
        return posicion >= 0 ? hoja.getDatos().get(posicion) : null;
    }

    private NodoArbolBMas encontrarHoja(int llave) {
        NodoArbolBMas nodo = raiz;
        while (!nodo.esHoja()) {
            nodo = nodo.getHijos().get(indiceHijo(nodo, llave));
        }
        return nodo;
    }

    private int indiceHijo(NodoArbolBMas nodo, int llave) {
        int i = 0;
        while (i < nodo.getClaves().size() && llave >= nodo.getClaves().get(i)) {
            i++;
        }
        return i;
    }

    private int minimoClaves(NodoArbolBMas nodo) {
        return nodo.esHoja() ? orden / 2 : (orden + 1) / 2 - 1;
    }

    public boolean eliminar(int clave) {
        if (!buscar(clave)) {
            return false;
        }

        eliminarRecursivo(raiz, clave);

        if (!raiz.esHoja() && raiz.getClaves().isEmpty()) {
            raiz = raiz.getHijos().getFirst();
        }
        return true;
    }

    private void eliminarRecursivo(NodoArbolBMas nodo, int clave) {
        if (nodo.esHoja()) {
            int posicion = nodo.getClaves().indexOf(clave);
            nodo.getClaves().remove(posicion);
            nodo.getDatos().remove(posicion);
            return;
        }

        int i = indiceHijo(nodo, clave);
        NodoArbolBMas hijo = nodo.getHijos().get(i);
        eliminarRecursivo(hijo, clave);

        if (hijo.getClaves().size() < minimoClaves(hijo)) {
            rebalancear(nodo, i);
        }
    }

    private void rebalancear(NodoArbolBMas padre, int i) {
        NodoArbolBMas izquierdo = i > 0 ? padre.getHijos().get(i - 1) : null;
        NodoArbolBMas derecho = i < padre.getHijos().size() - 1 ? padre.getHijos().get(i + 1) : null;

        if (izquierdo != null && izquierdo.getClaves().size() > minimoClaves(izquierdo)) {
            prestarDeIzquierdo(padre, i);
        } else if (derecho != null && derecho.getClaves().size() > minimoClaves(derecho)) {
            prestarDeDerecho(padre, i);
        } else if (izquierdo != null) {
            fusionar(padre, i - 1);
        } else {
            fusionar(padre, i);
        }
    }

    private void prestarDeIzquierdo(NodoArbolBMas padre, int i) {
        NodoArbolBMas hijo = padre.getHijos().get(i);
        NodoArbolBMas hermano = padre.getHijos().get(i - 1);

        if (hijo.esHoja()) {
            hijo.getClaves().addFirst(hermano.getClaves().removeLast());
            hijo.getDatos().addFirst(hermano.getDatos().removeLast());
            padre.getClaves().set(i - 1, hijo.getClaves().getFirst());
        } else {
            hijo.getClaves().addFirst(padre.getClaves().get(i - 1));
            padre.getClaves().set(i - 1, hermano.getClaves().removeLast());
            hijo.getHijos().addFirst(hermano.getHijos().removeLast());
        }
    }

    private void prestarDeDerecho(NodoArbolBMas padre, int i) {
        NodoArbolBMas hijo = padre.getHijos().get(i);
        NodoArbolBMas hermano = padre.getHijos().get(i + 1);

        if (hijo.esHoja()) {
            hijo.getClaves().addLast(hermano.getClaves().removeFirst());
            hijo.getDatos().addLast(hermano.getDatos().removeFirst());
            padre.getClaves().set(i, hermano.getClaves().getFirst());
        } else {
            hijo.getClaves().addLast(padre.getClaves().get(i));
            padre.getClaves().set(i, hermano.getClaves().removeFirst());
            hijo.getHijos().addLast(hermano.getHijos().removeFirst());
        }
    }

    private void fusionar(NodoArbolBMas padre, int i) {
        NodoArbolBMas izquierdo = padre.getHijos().get(i);
        NodoArbolBMas derecho = padre.getHijos().get(i + 1);

        if (izquierdo.esHoja()) {
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getDatos().addAll(derecho.getDatos());
            izquierdo.setSiguiente(derecho.getSiguiente());
        } else {
            izquierdo.getClaves().add(padre.getClaves().get(i));
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getHijos().addAll(derecho.getHijos());
        }

        padre.getClaves().remove(i);
        padre.getHijos().remove(i + 1);
    }

    public void recorrerRango(int claveInicial, int n) {
        if (n <= 0) {
            System.out.println("La cantidad de elementos debe ser mayor que cero.");
            return;
        }

        NodoArbolBMas hoja = encontrarHoja(claveInicial);
        int posicion = 0;
        while (posicion < hoja.getClaves().size() && hoja.getClaves().get(posicion) < claveInicial) {
            posicion++;
        }

        int impresos = 0;
        while (hoja != null && impresos < n) {
            while (posicion < hoja.getClaves().size() && impresos < n) {
                System.out.println("  " + hoja.getClaves().get(posicion) + " -> " + hoja.getDatos().get(posicion));
                posicion++;
                impresos++;
            }
            hoja = hoja.getSiguiente();
            posicion = 0;
        }

        if (impresos == 0) {
            System.out.println("No hay elementos con clave mayor o igual a " + claveInicial + ".");
        } else if (impresos < n) {
            System.out.println("(Se llegó al final del árbol: solo había " + impresos + " elemento(s).)");
        }
    }
}