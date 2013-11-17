package org.bluenautilus.checksum;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.zip.CRC32;


/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 10/31/13
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrcStore {

    private static final String crcFile = "crcStore.txt";
    PropertiesConfiguration currentProp = null;
    private HashMap<String, Long> crcStore = new HashMap<String, Long>();


    public void loadValuesFromFile() throws IOException, ConfigurationException {

        InputStream stream = null;
        try {
            File file = new File(crcFile);
            stream = new FileInputStream(file);

            currentProp = new PropertiesConfiguration();
            currentProp.load(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public void saveCurrentValuesToFile() throws Exception {
        File original = new File(crcFile);
        if (original.exists()) {
            boolean success = original.delete();
            if (!success) {
                throw new Exception("Could not delete " + original.getAbsolutePath());
            }
        }

        File newFile = new File(crcFile);

        if (newFile.exists()) {
            throw new Exception("Could not delete " + newFile.getAbsolutePath() + " to " + newFile.getAbsolutePath());

        }

        newFile.createNewFile();

        PropertiesConfiguration newProp = new PropertiesConfiguration();

        Set<String> keys = this.crcStore.keySet();

        for (String key : keys) {
            newProp.addProperty(key, this.crcStore.get(key));
        }

        newProp.save(newFile);

    }

    /**
     *      open file, get crc. return it. close file.
     */

    public Long getCrc(File file) throws IOException {

        //this method always closes the file.
        byte[] byteArray = FileUtils.readFileToByteArray(file);

        CRC32 crc = new CRC32();
        crc.update(byteArray);
        return crc.getValue();
    }

    public Long getCRC(String key) {
        return this.crcStore.get(key);
    }

    public boolean didCRCChange(String key, Long newCRC) {
        Long originalCRC = this.getCRC(key);
        return originalCRC.equals(newCRC);
    }

    public void addCrc(String key, Long crc) {
        this.crcStore.put(key, crc);
    }

}
