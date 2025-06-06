package ru.sdmitriy612.io.formatters;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.io.files.FileIOController;
import ru.sdmitriy612.io.files.WrongChecksumException;

import java.io.IOException;

/**
 * XMLFormatter class serializes {@link CollectionManager} to an XML file.
 * <p>
 *     This class uses Jackson's {@link XmlMapper} to handle XML serialization and deserialization.
 * </p>
 */
public class XMLFormatter implements Formatter<CollectionManager> {
    private final String pathToFile;
    private final XmlMapper xmlMapper = new XmlMapper();

    /**
     * Constructor creating File if it not exists.
     * <p>
     *    Sets xmlMapper JavaTimeModule register and enables WRITE_XML_DECLARATION
     *    for writing xml declaration in file.
     * </p>
     * @param pathToFile string path to file with collection. If file not exists, new file will be created
     */
    public XMLFormatter(String pathToFile){

        this.pathToFile = pathToFile;

        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);

    }

    /**
     * Reads serialized collectionManager from file and returns deserialized instance.
     * @return CollectionManager deserialized instance from file.
     */
    @Override
    public CollectionManager read() {

        try(FileIOController fileIOController = new FileIOController(pathToFile, true)){

            String fileContent = fileIOController.readAll();
            if(fileContent == null) return null;
            return xmlMapper.readValue(fileContent, CollectionManager.class);

        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * Reads a serialized {@link CollectionManager} from the XML file with checksum validation.
     * @return the deserialized {@link CollectionManager} instance from the file
     * @throws WrongChecksumException if the file checksum does not match
     */
    @Override
    public CollectionManager safeRead() throws WrongChecksumException {

        try(FileIOController fileIOController = new FileIOController(pathToFile, true)){

            String fileContent = fileIOController.safeRead();
            if(fileContent == null) return null;
            return xmlMapper.readValue(fileContent, CollectionManager.class);

        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }

    }
    /**
     * Serializes the given {@link CollectionManager} and writes it to the XML file with checksum validation.
     * @param collectionManager the {@link CollectionManager} instance to be serialized
     */
    @Override
    public void safeWrite(CollectionManager collectionManager) {

        try{
            String xml = xmlMapper.writeValueAsString(collectionManager);

            try(FileIOController fileIOController = new FileIOController(pathToFile, false)){
                fileIOController.safeWrite(xml);
            }catch (IOException e){
                throw new RuntimeException(e.getMessage());
            }

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Serializes collectionManager and writes it to file.
     * @param collectionManager collection manager to be serialized
     */
    @Override
    public void write(CollectionManager collectionManager) {

        try{
            String xml = xmlMapper.writeValueAsString(collectionManager);

            try(FileIOController fileIOController = new FileIOController(pathToFile, false)){
                fileIOController.safeWrite(xml);
            }catch (IOException e){
                throw new RuntimeException(e.getMessage());
            }

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Do nothing because no resources to close for this implementation.
     */
    @Override
    public void close() {

    }
}
