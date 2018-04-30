package de.ketchupbombe.module;

import lombok.Getter;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public enum ModuleDifficulty {


    EASY(1, "§a§lEASY"),
    MEDIUM(2, "§6§lMEDIUM"),
    HARD(3, "§c§lHARD");

    @Getter
    private final int value;
    @Getter
    private final String displayname;

    ModuleDifficulty(int value, String displayname){
        this.value = value;
        this.displayname = displayname;
    }

    public static ModuleDifficulty getDifficultyByValue(int value){
        for(ModuleDifficulty d : values()){
            if(d.getValue() == value)
                return d;
        }
        return ModuleDifficulty.EASY;
    }

}
