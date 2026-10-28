package es.iesfranciscodelosrios.chat;

import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestorChat {
    private final GestorXML gestorXML = new GestorXML();

    // TODO Fase 1: comprobar duplicados y registrar el usuario en XML.
    public void registrarUsuario(String nombre) throws JAXBException {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        ListaUsuarios lista = gestorXML.leerUsuarios();
        if (existeUsuario(lista, nombre)) {
            throw new IllegalArgumentException("El usuario " + nombre + " ya existe.");
        }
        lista.getUsuarios().add(new Usuario(nombre));
        gestorXML.guardarUsuarios(lista);
    }

    private boolean existeUsuario(ListaUsuarios lista, String nombre) {
        return lista.getUsuarios().stream()
                .anyMatch(u -> u.getNombre().equalsIgnoreCase(nombre));
    }

    // TODO Fase 2: validar usuarios, crear mensaje con fecha y persistirlo.
    public void enviarMensaje(String emisor, String destinatario, String contenido) throws JAXBException {
        if(emisor==null||destinatario==null||contenido==null||contenido.isEmpty()){
            throw new IllegalArgumentException("Emisor, destinatario y contenido son obligatorios.");
        }
        ListaUsuarios usuarios = gestorXML.leerUsuarios();
        if(!existeUsuario(usuarios,emisor)){
            throw new IllegalArgumentException("El emisor "+emisor+ "no existe.");
        }
        if(!existeUsuario(usuarios,destinatario)){
            throw new IllegalArgumentException("El destinatario  "+destinatario+ "no existe.");
        }
        String fecha = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Mensaje mensaje = new Mensaje(emisor,destinatario,contenido,fecha);
        ListaMensajes mensajes = gestorXML.leerMensajes();
        mensajes.getMensajes().add(mensaje);
        gestorXML.guardarMensajes(mensajes);
    }

    // TODO Fase 3: filtrar mensajes en ambos sentidos (A -> B y B -> A).
    public List<Mensaje> consultarConversacion(String usuarioA, String usuarioB) throws JAXBException {
        if(usuarioA==null||usuarioB==null){
            throw new IllegalArgumentException("Introduce los usuarios.");
        }
        ListaUsuarios usuarios = gestorXML.leerUsuarios();
        if(!existeUsuario(usuarios,usuarioA)){
            throw new IllegalArgumentException("El usuario "+usuarioA+" no existe");
        }
        if(!existeUsuario(usuarios,usuarioB)){
            throw new IllegalArgumentException("El usuario "+usuarioB+" no existe");
        }
        List<Mensaje> mensajes = gestorXML.leerMensajes().getMensajes();
        List<Mensaje> conversacion = new ArrayList<>();
        for(Mensaje mensaje : mensajes){
            boolean deAaB = false;
            boolean deBaA = false;
            if(mensaje.getEmisor().equalsIgnoreCase(usuarioA)&&mensaje.getDestinatario().equalsIgnoreCase(usuarioB)) deAaB=true;
            if(mensaje.getEmisor().equalsIgnoreCase(usuarioB)&&mensaje.getDestinatario().equalsIgnoreCase(usuarioA)) deBaA=true;
            if (deAaB || deBaA){
                conversacion.add(mensaje);
            }

        }
        return conversacion;
    }

    // TODO Fase 4: utilizar Stream API para contar mensajes por emisor.
    public Map<String, Long> estadisticas(String usuarioA, String usuarioB) throws JAXBException {
        throw new UnsupportedOperationException("Pendiente: estadisticas");
    }

    // TODO Fase 4: exportar la conversación a un archivo de texto.
    public void exportarConversacion(String usuarioA, String usuarioB, Path destino)
            throws JAXBException, IOException {
        throw new UnsupportedOperationException("Pendiente: exportarConversacion");
    }

    // TODO Fase 5: validar extensión/tamaño, copiar con InputStream/OutputStream
    // a media/ y guardar los metadatos del adjunto en el mensaje XML.
    public void adjuntarArchivo(Mensaje mensaje, Path archivoOrigen) throws IOException, JAXBException {
        throw new UnsupportedOperationException("Pendiente: adjuntarArchivo");
    }
}
