package com.amx.luckin.utils;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

public class ShowToastUtil {

    public static void showToast(Context context, String str) {

        // 创建文本组件
        Text text = new Text(context);
        text.setWidth(MATCH_CONTENT);
        text.setHeight(MATCH_CONTENT);
        text.setText(str); // 显示文本内容
        text.setTextSize(60); // 字号
        text.setPadding(30,10,30,10); // 内边距
        text.setMultipleLine(true); // 可多行显示文本内容
        text.setTextColor(Color.WHITE); // 文字颜色为白色
        text.setTextAlignment(TextAlignment.CENTER); // 居中显示
        // 文本组件使用灰色圆角背景
        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(0x888888FF));
        element.setShape(ShapeElement.RECTANGLE);
        element.setCornerRadius(15); // 圆角半径
        text.setBackground(element);

        // 创建定向布局，并加入文本组件
        DirectionalLayout layout = new DirectionalLayout(context);
        layout.setWidth(MATCH_PARENT);
        layout.setHeight(MATCH_CONTENT);
        layout.setAlignment(LayoutAlignment.CENTER); // 居中内容
        layout.addComponent(text);

        // 创建Toast对话框
        ToastDialog toastDialog = new ToastDialog(context);
        toastDialog.setComponent(layout); // 使用自定义组件
        toastDialog
                .setTransparent(true) // 设置背景透明
                .setDuration(3000) // 显示时间 3000毫秒
                .setAlignment(LayoutAlignment.BOTTOM + LayoutAlignment.HORIZONTAL_CENTER) // 居中下方显示
                .setOffset(0, 60) // 距离底边距200px距离
                .show();
    }
}
