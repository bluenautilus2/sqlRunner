package org.bluenautilus.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.data.SqlConfigItemsList;
import org.bluenautilus.gui.dataStoreGroupConfiguration.DataStoreTableModel;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
    private static final String BIG_ICON = "sqlserver_big.jpg";
    private static final String ICON = "sqlserver.png";
    public static ImageIcon sqlserverBig = null;
    public static ImageIcon sqlserverSmall = null;

    private static SqlConfigItemsList sqlConfigItemsList = new SqlConfigItemsList();


    public static void saveOffCurrent() {
        saveOffCurrent(sqlConfigItemsList);
    }

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

        Image small = null;
        Image big = null;
        try {
            small = ImageIO.read(new File(ICON));
            big = ImageIO.read(new File(BIG_ICON));
        } catch (IOException ioe) {
            log.error(ioe);
        }

        small = small.getScaledInstance(DataStoreTableModel.IMAGE_HEIGHT_IN_PIXELS, DataStoreTableModel.IMAGE_HEIGHT_IN_PIXELS, 0);
        sqlserverBig = new ImageIcon(big);
        sqlserverSmall = new ImageIcon(small);


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



    public static void addAndSave(SqlConfigItems items) {
        SqlConfigUtil.getSqlConfigItemsList().addSqlConfigItem(items);
        SqlConfigUtil.saveOffCurrent();
    }

    public static void removeAndSave(SqlConfigItems gone) {
        SqlConfigUtil.getSqlConfigItemsList().removeSqlConfigItem(gone.getUniqueId());
        SqlConfigUtil.saveOffCurrent();
    }

    public static void replaceWithUpdatesAndSave(SqlConfigItems updated){
        SqlConfigUtil.getSqlConfigItemsList().replace(updated);
        SqlConfigUtil.saveOffCurrent();
    }

}



