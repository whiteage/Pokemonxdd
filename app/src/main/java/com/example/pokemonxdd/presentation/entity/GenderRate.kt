package com.example.pokemonxdd.presentation.entity

enum class GenderRate(val value: Int, val displayName: String) {
    GENDERLESS(-1, "No gender"),
    ONLY_MALE(0, "Male"),
    FEMALE_1_8(1, "87.5%M / 12.5%F"),
    FEMALE_2_8(2, "75%M/ 25%F"),
    FEMALE_3_8(3, "62.5%M / 37.5%F"),
    FEMALE_4_8(4, "50%M / 50%F"),
    FEMALE_5_8(5, "37.5%M / 62.5%F"),
    FEMALE_6_8(6, "25%M / 75%F"),
    FEMALE_7_8(7, "12.5%M / 87.5%F"),
    ONLY_FEMALE(8, "Female");

    companion object {
        fun fromValue(value: Int): GenderRate {
            return values().find { it.value == value } ?: GENDERLESS
        }

    }
}