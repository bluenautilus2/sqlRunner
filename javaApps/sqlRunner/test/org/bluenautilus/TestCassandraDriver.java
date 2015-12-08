package org.bluenautilus;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * Created by bstevens on 12/4/15.
 */
public class TestCassandraDriver{

    public static void main(String[] args){
        String queryString = "--comment \n INSERT INTO workflow_group_position_type_workflows(workflow_group_position_type_workflow_id,created_by,created_date,last_updated_by,last_updated_date,position_type,workflow_group_id,workflow_id) VALUES ('6','3',1236937652000,'8739154',DATEOF(NOW()),6,'1','1'); ";
        Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
        Session session = cluster.connect("pa");

        ResultSet results = session.execute(queryString);
        session.close();
    }

}
