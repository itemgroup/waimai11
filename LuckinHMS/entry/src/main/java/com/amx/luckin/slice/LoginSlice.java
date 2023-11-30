package com.amx.luckin.slice;

import com.amx.luckin.ResourceTable;
import com.amx.luckin.entity.Coffee;
import com.amx.luckin.entity.ResultVo;
import com.amx.luckin.entity.User;
import com.amx.luckin.utils.HttpRequestUtil;
import com.amx.luckin.utils.LogUtil;
import com.amx.luckin.utils.UserDataUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginSlice extends AbilitySlice {

    private final Gson gson = new Gson();

    private static final String TAG = LoginSlice.class.getSimpleName();

    TaskDispatcher dispatcher = createSerialTaskDispatcher(
            "dispatcher_name",
            TaskPriority.DEFAULT);

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_login);
        initAvatarRound();
        telephoneLogin();
    }

    /**
     * 将图片设置为圆形的
     */
    private void initAvatarRound(){
        Image avatarImg = (Image) findComponentById(ResourceTable.Id_avatar_img);
        float avatarImgHeight = avatarImg.getHeight();
        avatarImg.setCornerRadius(avatarImgHeight/2);
    }

    private void telephoneLogin() {
        Button telephoneBtn = (Button) findComponentById(ResourceTable.Id_telephone_login_btn);
        telephoneBtn.setClickedListener(component -> {
            // 点击手机号登录后，向后端请求登录，并返回Token
            TaskDispatcher globalTaskDispatcher = getGlobalTaskDispatcher(TaskPriority.DEFAULT);
            globalTaskDispatcher.asyncDispatch(() -> {
                String url = "http://10.16.28.124:8081/user/login";

                User user = new User("13317537175");
                String userString = gson.toJson(user);

                String userInfo = HttpRequestUtil.sendPostRequest(this, url, userString);

                ResultVo resultVo = gson.fromJson(userInfo, ResultVo.class);
                if (resultVo.getCode() == 200) {
                    String token = (String) resultVo.getData();
                    // 携带token参数去请求用户数据
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // 将信息保存到手机本地数据库中
                    int i = UserDataUtil.setUserInfo("111", "111", token, "token", this);

                    if(i>0){
                        LogUtil.info(TAG,"用户信息保存成功");
                    }else {
                        LogUtil.error(TAG,"用户信息保存失败");
                    }

                    Intent intent = new Intent();
                    present(new MainAbilitySlice(),intent);
                }
            });
        });
    }


}

