package org.bluenautilus.data;

import java.util.UUID;
/**
 * Created by bstevens on 1/25/15.
 */
public class ConfigItems {

   UUID uniqueId = null;


   ConfigItems(){
       uniqueId = UUID.randomUUID();
   }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
}
