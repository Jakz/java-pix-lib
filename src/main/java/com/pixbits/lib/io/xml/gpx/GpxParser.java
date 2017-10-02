package com.pixbits.lib.io.xml.gpx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

public class GpxParser
{
  public static Gpx parse(Path path) throws IOException, SAXException, JAXBException
  {
    JAXBContext jaxbContext = JAXBContext.newInstance(Gpx.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    return (Gpx)jaxbUnmarshaller.unmarshal(Files.newBufferedReader(path));
    /*GpxParser handler = new GpxParser();
    XMLParser<Gpx> gpxParser = new XMLParser<>(handler);
    Gpx gpx = gpxParser.load(path);
    return gpx;*/
  }
  
  public static void save(Gpx gpx, Path path) throws JAXBException
  {
    JAXBContext jaxbContext = JAXBContext.newInstance(Gpx.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    
    jaxbMarshaller.marshal(gpx, path.toFile());
  }
}
