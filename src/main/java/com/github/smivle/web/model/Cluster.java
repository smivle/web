package com.github.smivle.web.model;

/**
 *
 * @author pc
 */
public class Cluster {
    private long clusterHash;
    private int count;

    public long getClusterHash() {
        return clusterHash;
    }

    public int getCount() {
        return count;
    }

    public void setClusterHash(long clusterHash) {
        this.clusterHash = clusterHash;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
