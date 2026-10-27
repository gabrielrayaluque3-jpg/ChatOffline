package es.iesfranciscodelosrios.chat;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class GestorXML {
    private final Path archivoUsuarios;
    private final Path archivoMensajes;

    public GestorXML() {
        this(Path.of("usuarios.xml"), Path.of("mensajes.xml"));
    }
    public GestorXML(Path archivoUsuarios, Path archivoMensajes) {
        this.archivoUsuarios = archivoUsuarios;
        this.archivoMensajes = archivoMensajes;
    }

    // TODO Fase 1: guardar usuarios.xml usando JAXB (Marshaller).
    public void guardarUsuarios(ListaUsuarios usuarios) throws JAXBException {
        guardar(usuarios, archivoUsuarios);
        throw new UnsupportedOperationException("Pendiente: guardarUsuarios");
    }

    // TODO Fase 1: recuperar usuarios.xml usando JAXB (Unmarshaller).
    // Si no existe el archivo, devolver una ListaUsuarios vacía.
    public ListaUsuarios leerUsuarios() throws JAXBException {
        ListaUsuarios lista = leer(archivoUsuarios, ListaUsuarios.class, ListaUsuarios::new);
        if (lista.getUsuarios() == null) lista.setUsuarios(new java.util.ArrayList<>());
        return lista;
    }

    // TODO Fase 2: guardar mensajes.xml usando JAXB.
    public void guardarMensajes(ListaMensajes mensajes) throws JAXBException {
        guardar(mensajes, archivoMensajes);
        throw new UnsupportedOperationException("Pendiente: guardarMensajes");
    }

    // TODO Fase 2: recuperar mensajes.xml usando JAXB.
    // Si no existe el archivo, devolver una ListaMensajes vacía.
    public ListaMensajes leerMensajes() throws JAXBException {
        ListaMensajes lista = leer(archivoMensajes, ListaMensajes.class, ListaMensajes::new);
        if (lista.getMensajes() == null) lista.setMensajes(new java.util.ArrayList<>());
        return lista;

        //throw new UnsupportedOperationException("Pendiente: leerMensajes");
    }
    private void guardar(Object objeto, Path destino) throws JAXBException {
        JAXBContext contexto = JAXBContext.newInstance(objeto.getClass());
        Marshaller marshaller = contexto.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.marshal(objeto, destino.toFile());
    }
    private <T> T leer(Path origen, Class<T> clase, Supplier<T> vacio) throws JAXBException {
        if (!Files.exists(origen)) {
            return vacio.get();
        }
        Unmarshaller unmarshaller = JAXBContext.newInstance(clase).createUnmarshaller();
        return clase.cast(unmarshaller.unmarshal(origen.toFile()));
    }
}
