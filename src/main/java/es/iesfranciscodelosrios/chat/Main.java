package es.iesfranciscodelosrios.chat;

import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws JAXBException, IOException {
        Scanner sc = new Scanner(System.in);
        GestorChat chat = new GestorChat();
        int op = 0;

        System.out.println("\t--- Chat Offline - Proyecto UD1--- ");
        do{
            System.out.println("1. Registrar usuario\n" +
                    "2. Enviar mensaje\n" +
                    "3. Consultar conversación\n" +
                    "4. Estadísticas\n" +
                    "5. Exportar conversación\n" +
                    "6. Adjuntar archivo\n" +
                    "0. Salir");

            System.out.print("Introduce una opción: ");
            op = sc.nextInt();
            String emisor = " ";
            String destinatario = " ";
            switch (op){
                case 1 :
                    System.out.println("\t--- REGISTRAR USUARIO ---");
                    System.out.print("Nombre de usuario: ");
                    String nombre = sc.next();
                    chat.registrarUsuario(nombre);
                    System.out.println("Usuario registrado con éxito.");
                break;
                case 2 :
                    System.out.println("\t--- ENVIAR MENSAJE ---");
                    System.out.print("Nombre del emisor: ");
                    emisor = sc.next();
                    System.out.print("Nombre del destinatario: ");
                    destinatario = sc.next();
                    System.out.print("Contenido del mensaje: ");
                    sc.nextLine();
                    String contenido = sc.nextLine();
                    chat.enviarMensaje(emisor,destinatario,contenido);
                    System.out.println("Mensaje enviado con éxito.");
                break;
                case 3 :
                    System.out.println("\t--- CONSULTAR CONVERSACIÓN ---");
                    System.out.print("Nombre de un usuario: ");
                    String usuarioA = sc.next();
                    System.out.print("Nombre de otro usuario: ");
                    String usuarioB = sc.next();
                    List<Mensaje> mensajes = chat.consultarConversacion(usuarioA,usuarioB);
                    for (Mensaje m : mensajes){
                        System.out.println(m);
                    }
                break;
                case 4 :
                    System.out.println("\t--- ESTADÍSTICAS ---");
                    System.out.println("Próximamente...");
                break;
                case 5 :
                    System.out.println("\t--- EXPORTAR CONVERSACIÓN ---");
                    System.out.print("Nombre de un usuario: ");
                    String usuario1 = sc.next();
                    System.out.print("Nombre de otro usuario: ");
                    String usuario2 = sc.next();
                    System.out.print("Nombre del archivo: ");
                    Path archivo = Paths.get(sc.next());
                    chat.exportarConversacion(usuario1,usuario2,archivo);
                break;
                case 6 :
                    System.out.println("\t--- ADJUNTAR ARCHIVO ---");
                    System.out.println("Próximamente...");
                break;
                case 0 :
                    System.out.println("Saliendo del programa...");
                break;
                default :
                    System.out.println("Introduce una opción válida");
            }
        }while(op!=0);
        sc.close();
    }
}
