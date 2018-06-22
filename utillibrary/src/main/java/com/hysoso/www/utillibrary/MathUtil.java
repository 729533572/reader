package com.hysoso.www.utillibrary;

import java.util.Random;

/**
 * Created by LuHe on 2016/11/10.
 */

public class MathUtil {
    /**
     * 获取在max和min之间的随机整数
     *
     * @param max
     * @param min
     * @return
     */
    public static Integer getRandomNumber(Integer max,Integer min){
        Double f = min+Math.random()*(max-min);
        return f.intValue();
    }
    public static Float getRandomNumber(){
        Float num = null;
        Random random=new Random();
        while (num == null){
            num =  random.nextFloat();
            break;
        }
        return num;
    }
}
