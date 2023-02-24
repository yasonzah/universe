package com.fluffy.universe.models;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender getGenderByString(String representation) {
        for (Gender gender : Gender.values()) {
            if (gender.toString().equalsIgnoreCase(representation)) {
                return gender;
            }
        }
        throw new RuntimeException("Gender for " + representation + " not found");
    }

    @Override
    public String toString() {
        return String.valueOf(super.toString().charAt(0));
    }
}
