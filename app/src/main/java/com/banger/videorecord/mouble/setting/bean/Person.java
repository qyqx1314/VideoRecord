package com.banger.videorecord.mouble.setting.bean;

import org.litepal.crud.DataSupport;
import org.litepal.crud.DataSupport;


import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujm on 2016/5/25.
 */
public class Person extends DataSupport {
    private int id;
    private String name;
    private String gender;
    private List<Phone> phones = new ArrayList<Phone>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
}
