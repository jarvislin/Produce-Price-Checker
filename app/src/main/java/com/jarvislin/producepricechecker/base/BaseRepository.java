package com.jarvislin.producepricechecker.base;


import com.jarvislin.producepricechecker.data.DataPreference_;
import com.jarvislin.producepricechecker.data.UserPreference_;
import com.jarvislin.producepricechecker.data.preference.FruitPreference_;
import com.jarvislin.producepricechecker.data.preference.VegetablePreference_;
import com.jarvislin.producepricechecker.network.NetworkService;

/**
 * Created by JarvisLin on 2017/1/7.
 */

public class BaseRepository {
    protected UserPreference_ userPreference;
    protected FruitPreference_ fruitPreference;
    protected VegetablePreference_ vegetablePreference;

    public BaseRepository(UserPreference_ userPreference, FruitPreference_ fruitPreference, VegetablePreference_ vegetablePreference) {
        this.userPreference = userPreference;
        this.fruitPreference = fruitPreference;
        this.vegetablePreference = vegetablePreference;
    }
}
