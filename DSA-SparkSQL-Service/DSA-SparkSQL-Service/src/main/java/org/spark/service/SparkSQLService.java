package org.spark.service;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.hive.thriftserver.HiveThriftServer2;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class SparkSQLService {
    private static Logger logger = Logger.getLogger(SparkSQLService.class.getName());

    private SparkSession spark;

    public SparkSession getSpark() {
        return spark;
    }

    public SparkSQLService() {
        startThriftServer2();
    }
    /*
    * https://spark.apache.org/docs/latest/sql-distributed-sql-engine.html
    *
     */
    private void startThriftServer2() {
        logger.info(">>> HiveThriftServer2 Starting ....");

        this.spark = SparkSession.builder()
                .master("local[*]")
                .config("spark.ui.port", "8081")
                .appName("SparkSQL-REST.Server")
                .enableHiveSupport()
                .config("hive.server2.thrift.port", "10000")
                .getOrCreate();

        HiveThriftServer2.startWithContext(spark.sqlContext());

        logger.info(">>> HiveThriftServer2 started successfully!");
    }
}

/* -- SparkSQL Hive Driver Config
<!-- https://mvnrepository.com/artifact/org.apache.hive/hive-jdbc -->
<dependency>
    <groupId>org.apache.hive</groupId>
    <artifactId>hive-jdbc</artifactId>
    <version>3.1.2</version>
</dependency>
*/