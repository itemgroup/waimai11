package com.amx.luckin.slice;

import com.amx.luckin.ResourceTable;
import com.amx.luckin.provider.CoffeeImagePageSliderProvider;
import com.amx.luckin.utils.ShowToastUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;


import java.util.ArrayList;
import java.util.List;

public class CoffeeDetailSlice extends AbilitySlice {

    private final int[] images = {
            ResourceTable.Media_lunbo2,
            ResourceTable.Media_lunbo4};

    private Text txt_num;   // 底部核算栏当前开发已点数量

    /**
     * 以下变量都是需要回调的
     */
    private String title;
    private String temperature = "热";
    private String sweet = "不另外加糖";
    private Integer price;
    private int totalNum = 1;


    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_coffee_detail);

        if (intent != null){
            title = (String) intent.getParams().getParam("coffeeTitle");
            price = (Integer) intent.getParams().getParam("coffeePrice");
            loadCoffeeInfo(title);
        }

        loadPageSlider();  // 加载轮播图
        getCoffeeChoose(); // 获取咖啡选择

        addChartNow(); // 加入购物车

    }

    // 加载咖啡的详情页信息
    private void loadCoffeeInfo(String coffeeTitle){
        Text coffeeTitleTxt = (Text) findComponentById(ResourceTable.Id_coffee_title);
        coffeeTitleTxt.setText(coffeeTitle);

        Text priceTxt = (Text) findComponentById(ResourceTable.Id_buy_info);
        String[] info = priceTxt.getText().split("\\+");
        priceTxt.setText(coffeeTitle+"￥"+price+"+"+info[1]+"+"+info[2]);

        Text totalPrice = (Text) findComponentById(ResourceTable.Id_total_price);
        totalPrice.setText("￥"+price);

        // 获取已点数量
        txt_num = (Text) findComponentById(ResourceTable.Id_num2);

        //获取加减
        Component btn_minus = findComponentById(ResourceTable.Id_minus2);
        Component btn_add = findComponentById(ResourceTable.Id_add2);

        btn_add.setClickedListener(component -> {
            int num = Integer.parseInt(txt_num.getText()) + 1;
            txt_num.setText(num + "");
            totalNum = num;
        });

        btn_minus.setClickedListener(component -> {
            int num = Integer.parseInt(txt_num.getText());
            if (num == 1){
                ShowToastUtil.showToast(this,"再减就没有啦");
            }else{
                txt_num.setText(num - 1 + "");
                totalNum = num - 1;
            }
        });


        // TO DO...
        // 实际开发中应该需要根据MainSlice传过来的CoffeeId加载页面内容【如轮播图，内容详情】
    }


    // 加载轮播图组件PageSlider
    private void loadPageSlider(){
        //根据id获得滑动页面组件对象
        PageSlider ps = (PageSlider) this.findComponentById(ResourceTable.Id_coffee_detail_pageSlider);
        //创建适配器对象，将当前界面对象和封装好的集合发送过去
        CoffeeImagePageSliderProvider coffeeDetailImageProvider = new CoffeeImagePageSliderProvider(initPage(), this);
        //将适配器加载至滑动组件上，完成同步组装
        ps.setProvider(coffeeDetailImageProvider);
    }

    /**
     * 获取用户的温度选择和口味选择
     */
    private void getCoffeeChoose(){
        ShapeElement selectElement = new ShapeElement();
        selectElement.setRgbColor(new RgbColor(235,237,249));
        selectElement.setCornerRadius(vp2px(8));

        ShapeElement unselectElement = new ShapeElement();
        unselectElement.setRgbColor(new RgbColor(255,255,255));


        List<Button> temperatureBtnList = new ArrayList<>();
        Button temperatureBtn = (Button) findComponentById(ResourceTable.Id_tem1);
        Button temperatureBtn2 = (Button) findComponentById(ResourceTable.Id_tem2);
        temperatureBtnList.add(temperatureBtn);
        temperatureBtnList.add(temperatureBtn2);
        for(Button tempBtn:temperatureBtnList){
            tempBtn.setClickedListener(component-> {
                temperature = temperatureBtn.getText();

                tempBtn.setBackground(selectElement);
                tempBtn.setTextColor(new Color(0xff242995));

                for (Button curButton1 : temperatureBtnList) {
                    if (curButton1 != tempBtn) {
                        curButton1.setBackground(unselectElement);
                        curButton1.setTextColor(new Color(0xff6a6a6a));
                    }
                }

                updateMenu();
            });
        }


        List<Button> sweetBtnList = new ArrayList<>();
        Button sweet1 = (Button) findComponentById(ResourceTable.Id_sweet1);
        Button sweet2 = (Button) findComponentById(ResourceTable.Id_sweet2);
        Button sweet3 = (Button) findComponentById(ResourceTable.Id_sweet3);
        sweetBtnList.add(sweet1);
        sweetBtnList.add(sweet2);
        sweetBtnList.add(sweet3);

        for(Button sweetBtn:sweetBtnList){
            sweetBtn.setClickedListener(component-> {
                sweet = sweetBtn.getText();

                sweetBtn.setBackground(selectElement);
                sweetBtn.setTextColor(new Color(0xff242995));

                for (Button curButton2 : sweetBtnList) {
                    if (curButton2 != sweetBtn) {
                        curButton2.setBackground(unselectElement);
                        curButton2.setTextColor(new Color(0xff6a6a6a));
                    }
                }

                updateMenu();
            });
        }

    }

    private int fp2px(int fp) {
        return AttrHelper.fp2px(fp, this);
    }

    private int vp2px(int vp) {
        return AttrHelper.vp2px(vp, this);
    }

    /**
     * 更新购物车信息
     */
    private void updateMenu(){
        Text priceTxt = (Text) findComponentById(ResourceTable.Id_buy_info);
        String[] info = priceTxt.getText().split("\\+");
        priceTxt.setText(info[0]+"+"+temperature+"￥0+"+sweet+"￥0");
    }

    private void addChartNow(){
        Button addChartBtn = (Button) findComponentById(ResourceTable.Id_add_chart);
        addChartBtn.setClickedListener(component -> {
            Intent intent1 = new Intent();
            intent1.setParam("price", price);
            intent1.setParam("title", title);
            intent1.setParam("temperature", temperature);
            intent1.setParam("sweet", sweet);
            intent1.setParam("totalNum", totalNum);
            setResult(intent1);
            terminate();  // 关闭当前页面
        });
    }

    private List<Integer> initPage() {
        List<Integer> imagesArray = new ArrayList<>();
        for (int image : images) {
            imagesArray.add(image);
        }
        return imagesArray;
    }

    @Override
    protected void onActive() {
        super.onActive();
        System.out.println("加载了onActive--------------");
    }

    @Override
    protected void onForeground(Intent intent) {
        super.onForeground(intent);
        System.out.println("加载了onForeground--------------");

    }

    @Override
    protected void onStop() {
        super.onStop();

        txt_num.setText(1 + "");
    }
}
