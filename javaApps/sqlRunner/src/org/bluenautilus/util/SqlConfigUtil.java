package org.bluenautilus.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.SqlConfigItemsList;
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
public class SqlConfigUtil {

    private static Log log = LogFactory.getLog(SqlConfigUtil.class);

    private static final String CONFIG_FILENAME = "sqlconfig.json";
    private static final String BAK_FILENAME = "sqlconfig.json.old";

    private static SqlConfigItemsList sqlConfigItemsList = new SqlConfigItemsList();


    public static void saveOffCurrent(SqlConfigItemsList newList) {
       sqlConfigItemsList = newList;

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
                mapper.writeValue(newFile, sqlConfigItemsList);

                // display to console
                System.out.println(mapper.writeValueAsString(sqlConfigItemsList));

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

    public static SqlConfigItemsList readInConfiguration(){

        ObjectMapper mapper = new ObjectMapper();
          File file = new File(CONFIG_FILENAME);
        try {

            // read from file, convert it to user class
            sqlConfigItemsList = mapper.readValue(file,SqlConfigItemsList.class);

            // display to console
            System.out.println(sqlConfigItemsList);

        } catch (JsonGenerationException e) {

            log.error(e);

        } catch (JsonMappingException e) {

            log.error(e);

        } catch (IOException e) {

            log.error(e);

        }
        return sqlConfigItemsList;
    }


    public static SqlConfigItemsList getSqlConfigItemsList() {
        return sqlConfigItemsList;
    }

    public static void setSqlConfigItemsList(SqlConfigItemsList sqlConfigItemsList) {
        SqlConfigUtil.sqlConfigItemsList = sqlConfigItemsList;
    }

    public static List<SqlConfigItems> getUuidList(List<UUID> itemsToGet){
        List<SqlConfigItems> matches = new ArrayList<>();
        for(SqlConfigItems item:sqlConfigItemsList.getSqlConfigItems()){
            if(itemsToGet.contains(item.getUniqueId())){
                matches.add(item);
            }
        }
        return matches;
    }
}



