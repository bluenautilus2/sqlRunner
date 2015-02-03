package org.bluenautilus.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.CassConfigItemsList;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * User: bluenautilus2
 * Date: 5/3/13
 * Time: 11:20 PM
 */
public class CassConfigUtil {

    private static Log log = LogFactory.getLog(CassConfigUtil.class);

    private static final String CONFIG_FILENAME = "cassconfig.json";
    private static final String BAK_FILENAME = "cassconfig.json.old";

    private static CassConfigItemsList cassConfigItemsList = new CassConfigItemsList();

    public static synchronized void saveOffCurrent(CassConfigItemsList newList) {
        cassConfigItemsList = newList;

        try {
            File backup = new File(BAK_FILENAME);
            if (backup.exists()) {
                boolean success = backup.delete();
                if (!success) {
                    throw new Exception("Could not delete " + backup.getAbsolutePath());
                }
            }

            File configFile = new File(CONFIG_FILENAME);

            if (configFile.exists()) {
                boolean success = configFile.renameTo(backup);
                if (!success) {
                    throw new Exception("Could not rename " + configFile.getAbsolutePath() + " to " + backup.getAbsolutePath());
                }
            }

            //we blew the old one away, make a new object.
            File newFile = new File(CONFIG_FILENAME);
            newFile.createNewFile();

            ObjectMapper mapper = new ObjectMapper();

            try {

                // convert user object to json string, and save to a file
                mapper.writeValue(newFile, cassConfigItemsList);

                // display to console
                System.out.println(mapper.writeValueAsString(cassConfigItemsList));

            } catch (JsonGenerationException e) {

                log.error(e);

            } catch (JsonMappingException e) {

                log.error(e);

            } catch (IOException e) {

                log.error(e);

            }


        } catch (Exception ex) {
            log.error(ex);
        }


    }

    public static synchronized CassConfigItemsList readInConfiguration(){

        ObjectMapper mapper = new ObjectMapper();
          File file = new File(CONFIG_FILENAME);
        try {

            // read from file, convert it to user class
            cassConfigItemsList = mapper.readValue(file,CassConfigItemsList.class);

            // display to console
            System.out.println(cassConfigItemsList);

        } catch (JsonGenerationException e) {

            log.error(e);

        } catch (JsonMappingException e) {

            log.error(e);

        } catch (IOException e) {

            log.error(e);

        }
        return cassConfigItemsList;
    }

    public static CassConfigItemsList getCassConfigItemsList() {
        return cassConfigItemsList;
    }

    public static void setCassConfigItemsList(CassConfigItemsList cassConfigItemsList) {
        CassConfigUtil.cassConfigItemsList = cassConfigItemsList;
    }

    public static List<CassConfigItems> getUuidList(List<UUID> itemsToGet){
        List<CassConfigItems> matches = new ArrayList<>();
        for(CassConfigItems item:cassConfigItemsList.getCassConfigItems()){
            if(itemsToGet.contains(item.getUniqueId())){
                matches.add(item);
            }
        }
        return matches;
    }
}



