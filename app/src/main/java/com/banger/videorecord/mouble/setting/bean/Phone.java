package com.banger.videorecord.mouble.setting.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by zhujm on 2016/5/25.
 */
public class Phone extends DataSupport {
    private int id;
    private String phoneNumber;
    private Person person;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
