


import es.iesfranciscodelosrios.chat.GestorChat;

import java.awt.Desktop;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        GestorChat chat = new GestorChat();
        System.out.println("Chat Offline - Proyecto UD1");

        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            String entrada = teclado.nextLine().trim();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Opción no válida.");
                continue;
            }
            try {
                switch (opcion) {
                    case 1 -> registrar(teclado, chat);
                    case 2 -> enviar(teclado, chat);
                    case 3 -> consultar(teclado, chat);
                    case 4 -> estadisticas(teclado, chat);
                    case 5 -> exportar(teclado, chat);
                    case 6 -> adjuntar(teclado, chat);
                    case 7 -> empaquetar(teclado, chat);
                    case 8 -> abrirAdjunto(teclado, chat);
                    case 0 -> System.out.println("¡Hasta pronto!");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Aviso: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        teclado.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n===== MENÚ =====");
        System.out.println("1. Registrar usuario");
        System.out.println("2. Enviar mensaje");
        System.out.println("3. Consultar conversación");
        System.out.println("4. Estadísticas");
        System.out.println("5. Exportar conversación (TXT/CSV)");
        System.out.println("6. Adjuntar archivo");
        System.out.println("7. Empaquetar conversación + adjuntos (ZIP)");
        System.out.println("8. Abrir/exportar un adjunto");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
    }

    private static String pedir(Scanner teclado, String texto) {
        System.out.print(texto);
        return teclado.nextLine().trim();
    }

    private static void registrar(Scanner t, GestorChat chat) throws Exception {
        chat.registrarUsuario(pedir(t, "Nombre de usuario: "));
        System.out.println("Usuario registrado.");
    }

    private static void enviar(Scanner t, GestorChat chat) throws Exception {
        String emisor = pedir(t, "Emisor: ");
        String destinatario = pedir(t, "Destinatario: ");
        String contenido = pedir(t, "Mensaje: ");
        chat.enviarMensaje(emisor, destinatario, contenido);
        System.out.println("Mensaje enviado.");
    }

    private static void consultar(Scanner t, GestorChat chat) throws Exception {
        String a = pedir(t, "Usuario que consulta: ");
        String b = pedir(t, "Conversación con: ");
        List<Mensaje> mensajes = chat.consultarConversacion(a, b);
        if (mensajes.isEmpty()) {
            System.out.println("No hay mensajes entre ambos usuarios.");
            return;
        }
        mensajes.forEach(m -> System.out.println(chat.formatearMensaje(m)));
    }

    private static void estadisticas(Scanner t, GestorChat chat) throws Exception {
        String a = pedir(t, "Usuario A: ");
        String b = pedir(t, "Usuario B: ");
        Map<String, Long> porEmisor = chat.estadisticas(a, b);
        long total = porEmisor.values().stream().mapToLong(Long::longValue).sum();
        System.out.println("--- Resumen de la conversación ---");
        System.out.println("Total de mensajes: " + total);
        porEmisor.forEach((u, n) -> System.out.println("  " + u + ": " + n));
        System.out.println("Mensajes con adjunto: " + chat.contarAdjuntos(a, b));
        System.out.println("Palabras más comunes:");
        chat.palabrasMasComunes(a, b, 5).forEach((p, n) -> System.out.println("  " + p + " (" + n + ")"));
    }

    private static void exportar(Scanner t, GestorChat chat) throws Exception {
        String a = pedir(t, "Usuario A: ");
        String b = pedir(t, "Usuario B: ");
        String ruta = pedir(t, "Archivo destino (.txt o .csv) [conversacion.txt]: ");
        if (ruta.isEmpty()) ruta = "conversacion.txt";
        chat.exportarConversacion(a, b, Path.of(ruta));
        System.out.println("Conversación exportada a " + ruta);
    }

    private static void adjuntar(Scanner t, GestorChat chat) throws Exception {
        String emisor = pedir(t, "Emisor: ");
        String destinatario = pedir(t, "Destinatario: ");
        String contenido = pedir(t, "Texto del mensaje (puede ser breve): ");
        if (contenido.isEmpty()) contenido = "(adjunto)";
        String ruta = pedir(t, "Ruta del archivo a adjuntar: ");
        // Se valida el archivo antes de nada; el mensaje se guarda ya con el adjunto.
        Mensaje m = chat.crearMensajeSinGuardar(emisor, destinatario, contenido);
        chat.adjuntarArchivo(m, Path.of(ruta));
        System.out.println("Mensaje enviado con adjunto.");
    }

    private static void empaquetar(Scanner t, GestorChat chat) throws Exception {
        String a = pedir(t, "Usuario A: ");
        String b = pedir(t, "Usuario B: ");
        String ruta = pedir(t, "ZIP destino [conversacion.zip]: ");
        if (ruta.isEmpty()) ruta = "conversacion.zip";
        chat.empaquetarConversacion(a, b, Path.of(ruta));
        System.out.println("ZIP creado: " + ruta);
    }

    private static void abrirAdjunto(Scanner t, GestorChat chat) throws Exception {
        String a = pedir(t, "Usuario A: ");
        String b = pedir(t, "Usuario B: ");
        List<Mensaje> conAdjunto = chat.consultarConversacion(a, b).stream()
                .filter(m -> m.getAdjuntoRuta() != null).toList();
        if (conAdjunto.isEmpty()) {
            System.out.println("No hay adjuntos en esta conversación.");
            return;
        }
        for (int i = 0; i < conAdjunto.size(); i++) {
            System.out.println((i + 1) + ". " + chat.formatearMensaje(conAdjunto.get(i)));
        }
        int n = Integer.parseInt(pedir(t, "Número de adjunto: "));
        if (n < 1 || n > conAdjunto.size()) throw new IllegalArgumentException("Número fuera de rango.");
        Mensaje m = conAdjunto.get(n - 1);
        String carpeta = pedir(t, "Carpeta destino (vacío = abrir directamente): ");
        if (carpeta.isEmpty()) {
            if (chat.adjuntoFaltante(m)) throw new IllegalArgumentException("El adjunto ya no está disponible.");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(Path.of(m.getAdjuntoRuta()).toFile());
            } else {
                System.out.println("No se puede abrir desde aquí. Ruta: " + m.getAdjuntoRuta());
            }
        } else {
            Path copia = chat.exportarAdjunto(m, Path.of(carpeta));
            System.out.println("Adjunto exportado a " + copia);
        }
    }
}



















