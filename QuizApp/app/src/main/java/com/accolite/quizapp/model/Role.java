package com.accolite.quizapp.model;

public class Role {

    private int id;
    private String code;
    private String label;

    public Role(int id, String code, String label) {
        super();
        this.id = id;
        this.code = code;
        this.label = label;
    }

    public Role() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ", code=" + code + ", label=" + label + "]";
    }

}
