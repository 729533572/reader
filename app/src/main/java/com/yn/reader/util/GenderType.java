package com.yn.reader.util;

/**
 * Created : lts .
 * Date: 2018/1/3
 * Email: lts@aso360.com
 */

public enum GenderType {

    MALE(1,"男"),
    FEMALE(2,"女");

    private String gender;
    private int id;

    GenderType(int id, String gender) {
        this.id = id;
        this.gender = gender;
    }

    public static String getGenderType(int id) {
        for (GenderType genderType : GenderType.values()) {
            if (genderType.id == id) {
                return genderType.gender;
            }
        }

        return "";
    }
}
