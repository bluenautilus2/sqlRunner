package org.bluenautilus.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.DataStoreGroup;
import org.bluenautilus.data.DataStoreGroupList;
import org.bluenautilus.data.UuidConfigItem;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


/**
 * User: bluenautilus2
 * Date: 5/3/13
 * Time: 11:20 PM
 */
public class DataStoreGroupConfigUtil {

    private static Log log = LogFactory.getLog(DataStoreGroupConfigUtil.class);

    private static final String CONFIG_FILENAME = "datastores.json";
    private static final String BAK_FILENAME = "datastores.json.old";

    private static DataStoreGroupList dataStoreGroupList = new DataStoreGroupList();


    public static void saveOffCurrent(DataStoreGroupList newList) {
        dataStoreGroupList = newList;
        saveOffCurrent();
    }

    public static void saveOffCurrent() {

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
                mapper.writeValue(newFile, dataStoreGroupList);

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

    public static DataStoreGroupList readInConfiguration() {

        ObjectMapper mapper = new ObjectMapper();
        File file = new File(CONFIG_FILENAME);
        try {

            // read from file, convert it to user class
            dataStoreGroupList = mapper.readValue(file, DataStoreGroupList.class);

        } catch (JsonGenerationException e) {
            log.error(e);
        } catch (JsonMappingException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
        return dataStoreGroupList;
    }

    public static DataStoreGroupList getDataStoreGroupList() {
        return dataStoreGroupList;
    }

    public static void setDataStoreGroupList(DataStoreGroupList dataStoreGroupList) {
        DataStoreGroupConfigUtil.dataStoreGroupList = dataStoreGroupList;
    }

    public static void addAndSave(DataStoreGroup newGroup) {
        DataStoreGroupConfigUtil.getDataStoreGroupList().addGroup(newGroup);
        DataStoreGroupConfigUtil.saveOffCurrent();
    }

    public static void removeAndSave(UUID goneGroup) {
        dataStoreGroupList.removeDataStoreGroup(goneGroup);
        DataStoreGroupConfigUtil.saveOffCurrent();
    }

    public static void replaceWithUpdatesAndSave(DataStoreGroup updatedGroup){
       DataStoreGroupConfigUtil.dataStoreGroupList.replace(updatedGroup);
        DataStoreGroupConfigUtil.saveOffCurrent();
    }

    public static void removeDataStoreFromAllGroupsAndSave(UuidConfigItem removed){
        for(DataStoreGroup group:dataStoreGroupList.getDataStoreGroupList()){
            boolean found = false;
            UUID foundID = null;
            for(UUID id:group.getDataStores()){
                if(id.equals(removed.getUniqueId())){
                    found = true;
                    foundID = id;
                }
            }
            if(found){
                group.getDataStores().remove(foundID);
            }
        }
        DataStoreGroupConfigUtil.saveOffCurrent();
    }

    public static void updateMostRecentlyChosenDataStoreGroup(UUID chosenUUID){
        dataStoreGroupList.setLastUsedDataStoreGroupId(chosenUUID);
        DataStoreGroupConfigUtil.saveOffCurrent();
    }

    public static UUID getMostRecentlyChosenDataStoreGroup(){
        return dataStoreGroupList.getLastUsedDataStoreGroupId();
    }

    public static String getLastUsedFileFolderSql() {
        return dataStoreGroupList.getLastUsedFileFolderSql();
    }

    public static void updateLastUsedFileFolderSql(String lastUsedFileFolderSql) {
        dataStoreGroupList.setLastUsedFileFolderSql(lastUsedFileFolderSql);
        DataStoreGroupConfigUtil.saveOffCurrent();
    }

    public static String getLastUsedFileFolderCass() {
        return dataStoreGroupList.getLastUsedFileFolderCass();
    }

    public static void updateLastUsedFileFolderCass(String lastUsedFileFolderCass) {
        dataStoreGroupList.setLastUsedFileFolderCass(lastUsedFileFolderCass);
    }

}



