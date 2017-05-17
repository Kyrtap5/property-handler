import java.io.*;
import java.util.Optional;
import java.util.Properties;

public class PropertyHandler {
    private String fileName;
    private Properties properties = new Properties();
    private InputStream inStream = null;
    private OutputStream outStream = null;

    /**
     * Creates a new PropertyHandler for the given filename.
     *
     * @param fileName The filename to be read or written on
     */
    public PropertyHandler(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Writes the given key under the give value in the property file
     *
     * @param key   The key to be written on
     * @param value The value to be saved
     */
    public void setProperty(String key, String value) {
        try {
            //Set property
            if (!propertyFileExists()) {
                File newFile = new File(fileName);
                newFile.createNewFile();
                //Continue with the recently created file
                outStream = new FileOutputStream(newFile, false);
            } else outStream = new FileOutputStream(fileName);
            properties.setProperty(key, value);
            properties.store(outStream, null);
        } catch (IOException ioException) {
            System.err.println("Error while writing property: " + ioException.toString());
            ioException.printStackTrace();
        } finally {
            //Close OutputStream before returning
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException ioException) {
                    System.err.println("Error while writing property: " + ioException.toString());
                    ioException.printStackTrace();
                }
            }
        }
    }

    /**
     * Gets a value from the property file under the given key, returns String
     *
     * @param key The key storing the wanted value
     * @return The value hashed under the given key, null if value was not found
     */
    public String getProperty(String key) {
        String ret = null;
        try {
            //Get the value from the properties file
            if (!propertyFileExists()) {
                File newFile = new File(fileName);
                newFile.createNewFile();
                //Continue with the recently created file
                inStream = new FileInputStream(newFile);
            } else inStream = new FileInputStream(fileName);
            properties.load(inStream);
            ret = properties.getProperty(key);
        } catch (IOException ioException) {
            //Print IOException
            System.err.println("Error while reading property: " + ioException.toString());
            ioException.printStackTrace();
        } finally {
            //Close the InputStream before returning
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ioException) {
                    System.err.println("Error while reading property: " + ioException.toString());
                    ioException.printStackTrace();
                }
            }
        }
        //Return the value
        return ret;
    }

    /**
     * Gets a value from the property file under the given key, returns Optional
     *
     * @param key The key storing the wanted value
     * @return The value hashed under the given key, empty Optional if value was not found
     */
    public Optional<String> getOptionalProperty(String key) {
        return Optional.of(getProperty(key));
    }

    /**
     * Checks if fileName exists in the file directory
     * @return false if file does not exist, true if it does
     */
    public boolean propertyFileExists() {
        File file = new File(fileName);
        return file.exists() && !file.isDirectory();
    }
}