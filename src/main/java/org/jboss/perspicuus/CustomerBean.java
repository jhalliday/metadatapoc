package org.jboss.perspicuus;

/**
 * Created by jhalli on 06/07/16.
 */
public class CustomerBean {

    public int id;
    public String name;

    public CustomerBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
