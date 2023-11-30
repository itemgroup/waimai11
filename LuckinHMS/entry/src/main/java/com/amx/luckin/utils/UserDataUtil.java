package com.amx.luckin.utils;

import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.app.Context;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.RdbPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;


public class UserDataUtil {

    private static final Uri uri = Uri.parse("dataability:///com.amx.luckin.UserDataAbility/user_info");

    public static String getUserInfo(Context context){
        String token = "";
        DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(context);
        String[] columns = {"id","name","telephone","verify","token"};
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.isNotNull("token");

        try {
            //  获取记录
            ResultSet rs = dataAbilityHelper.query(uri, columns, predicates);
            System.out.println(rs.toString());
            if(rs != null){
                if(rs.goToLastRow()){
                    System.out.println(rs.toString());
                    token = rs.getString(4);
                }
            }
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }
        return token;
    }

    public static int setUserInfo(String name,String telephone,String token,String verifyName,Context context) {
        int i = 0;
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString("name",name);
        valuesBucket.putString("telephone",telephone);
        valuesBucket.putString("verify",verifyName);
        valuesBucket.putString("token",token);
        DataAbilityHelper dataAbilityHelper =
                DataAbilityHelper.creator(context);
        try {
            i = dataAbilityHelper.insert(uri, valuesBucket);
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static int updateUserInfo(String name,String telephone,String token,Context context) {
        int i = 0;
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString("name",name);
        valuesBucket.putString("telephone",telephone);

        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.equalTo("token",token);

        DataAbilityHelper dataAbilityHelper =
                DataAbilityHelper.creator(context);
        try {
            i = dataAbilityHelper.update(uri, valuesBucket,predicates);
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }
        return i;
    }


}
