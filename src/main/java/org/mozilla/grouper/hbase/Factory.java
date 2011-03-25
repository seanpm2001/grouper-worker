package org.mozilla.grouper.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableFactory;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTableInterfaceFactory;
import org.apache.hadoop.hbase.util.Bytes;
import org.mozilla.grouper.base.Config;


public class Factory {

    public Factory(Config conf) {
        conf_ = conf;
        hbaseConf_ = HBaseConfiguration.create();
        if (conf_.hbaseZk() != null) {
            hbaseConf_.set("hbase.zookeeper.quorum", conf_.hbaseZk());
        }
        if (conf_.hbaseZkNode() != null) {
            hbaseConf_.set("zookeeper.znode.parent", conf_.hbaseZkNode());
        }
    }

    public org.apache.hadoop.conf.Configuration hbaseConfig() {
        return hbaseConf_;
    }

    public HTableInterface table(String name) {
        return tableFactory_.createHTableInterface(hbaseConf_,
                                                   Bytes.toBytes(tableName(name)));
    }

    public String tableName(String name) {
        return conf_.prefix() + name;
    }

    public void release(HTableInterface table) {
        tableFactory_.releaseHTableInterface(table);
    }

    /** Row keys that are (hopefully) in sync with those used by the REST service! */
    public Keys keys() {
        return new SimpleKeys();
    }

    private final Config conf_;
    private final org.apache.hadoop.conf.Configuration hbaseConf_;
    private final HTableInterfaceFactory tableFactory_ = new HTableFactory();
}
