package com.amx.luckin.slice;

import com.amx.luckin.ResourceTable;
import com.amx.luckin.entity.Category;
import com.amx.luckin.entity.Coffee;
import com.amx.luckin.entity.ResultVo;
import com.amx.luckin.entity.User;
import com.amx.luckin.utils.HttpRequestUtil;
import com.amx.luckin.utils.UserDataUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.app.dispatcher.Group;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;
import java.util.*;

public class MainAbilitySlice extends AbilitySlice {

    private final Gson gson = new Gson();
    private boolean isShowCoffeeMenu = false;

    private String title;
    private String temperature;
    private String sweet;
    private Integer price;
    private int totalNum;

    private List<DirectionalLayout> everyCoffee;
    private Map<Integer,DirectionalLayout> tempOrderCoffee;

    private int curClickCoffee;

    private DirectionalLayout coffeeOrdered;
    private Text txt_totalNum;
    private Text txt_price;

    private HashSet<String> isCoffeeExist;



    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        String token = UserDataUtil.getUserInfo(this);
        if (token!= null && token.length() != 0){
            Map<String,String> user = getUserInfo(token);
            System.out.println("获得当前用户的信息: "+user);

            // 更新用户数据
            UserDataUtil.updateUserInfo(user.get("name"), user.get("telephone"),token,this);

            initParams();   // 初始化一些变量的声明
            initTabListTags();  // 初始化中间的TabList
            initBottomTabList();  // 初始化底部的TabList
            initCategory();    // 初始化左侧的一级分类
            initTemplateComponent();   // 初始化模板组件
            showCartCoffee();  // 结算栏

        }else {
            // 如果用户没有登录，就跳转【重定向】到登录界面
            present(new LoginSlice(),new Intent());
        }

    }

    /**
     * 获取用户信息：包括用户名和电话
     * @param token 携带的token信息
     * @return 返回一个User对象
     */
    private Map<String,String> getUserInfo(String token){
        Map<String,String> map = new HashMap<>();

        TaskDispatcher globalTaskDispatcher = getGlobalTaskDispatcher(TaskPriority.DEFAULT);
        globalTaskDispatcher.syncDispatch(() -> {
            String url = "http://10.16.28.124:8081/user/info?token="+token;

            String userInfo = HttpRequestUtil.sendGetRequest(this, url, null);
            System.out.println(userInfo);

            ResultVo resultVo = gson.fromJson(userInfo, ResultVo.class);
            System.out.println(resultVo);
            if (resultVo.getCode() == 200) {
                String dataString = gson.toJson(resultVo.getData());
                User user1 = gson.fromJson(dataString,User.class);
                map.put("name",user1.getName());
                map.put("telephone",user1.getTelephone());
                System.out.println(map);
            }
        });
        return map;
    }

    public void initParams(){
        isCoffeeExist = new HashSet<>();   // 当前咖啡是否已经添加到了已点菜单中
    }

    public void initTemplateComponent(){
        tempOrderCoffee = new HashMap<>();
        coffeeOrdered = (DirectionalLayout) findComponentById(ResourceTable.Id_coffee_order);
        txt_totalNum = (Text) findComponentById(ResourceTable.Id_dot_title);
        txt_price = (Text) findComponentById(ResourceTable.Id_total_price);
    }

    public void initTabListTags(){
        //1.初始化中部的TabList
        TabList tabList = (TabList) findComponentById(ResourceTable.Id_tab_list);
        String[] tabListTags = {"经典菜单","9.9元♥","冰雪季❄","我的常点"};
        if (tabList != null){
            for (String tabListTag : tabListTags) {
                TabList.Tab tab = tabList.new Tab(this);
                tab.setText(tabListTag);
                tabList.addTab(tab);
            }
        }
    }

    public void initBottomTabList(){
        DirectionalLayout bottomLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_bottom_layout);

        initOneBottomTab("首页",ResourceTable.Media_home,bottomLayout);
        initOneBottomTab("菜单",ResourceTable.Media_menu,bottomLayout);
        initOneBottomTab("潮品",ResourceTable.Media_chaoping,bottomLayout);
        initOneBottomTab("自由卡",ResourceTable.Media_card,bottomLayout);
        initOneBottomTab("我的",ResourceTable.Media_user,bottomLayout);

    }

    public void initOneBottomTab(String name,int pixel,DirectionalLayout directionalLayout){
        getUITaskDispatcher().asyncDispatch(() -> {
            DependentLayout temBottom = (DependentLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_template_bottomtab, null, false);

            Image image = (Image) temBottom.findComponentById(ResourceTable.Id_image);
            image.setPixelMap(pixel);

            Text text = (Text) temBottom.findComponentById(ResourceTable.Id_name);
            text.setText(name);

            directionalLayout.addComponent(temBottom);
        });
    }

    public void initCategory(){
        TaskDispatcher globalTaskDispatcher = getGlobalTaskDispatcher(TaskPriority.DEFAULT);
        globalTaskDispatcher.asyncDispatch(() -> {
            String url = "http://10.16.28.124:8081/luckin/category";
            String categoryString = HttpRequestUtil.sendGetRequest(this, url, null);
            System.out.println(categoryString);
            ResultVo resultVo = gson.fromJson(categoryString,ResultVo.class);
            if (resultVo.getCode() == 200){
                String dataString = gson.toJson(resultVo.getData());
                List<Category> categories =
                        gson.fromJson(dataString,new TypeToken<List<Category>>(){}.getType());

                // 找到需要插入模板的Layout
                DirectionalLayout categoryLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_first_category_layout);

                getUITaskDispatcher().asyncDispatch(()->{
                    for (Category category : categories) {
                        DirectionalLayout temCategory = (DirectionalLayout)
                                LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_template_category, null, false);
                        Text categoryTxt = (Text) temCategory.findComponentById(ResourceTable.Id_category_txt);

                        categoryTxt.setText(category.getTitle());

                        categoryLayout.addComponent(temCategory);

                        initFirstCoffee(category.getId(), category.getTitle(), category.getDesc());
                    }
                });
            }
        });
    }

    public void initFirstCoffee(int id,String categoryTitle,String categoryDesc){
        // 找到需要插入模板文件的布局空间
        DirectionalLayout coffeeLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_coffee_layout);

        // 渲染右侧每一个大类
        getUITaskDispatcher().asyncDispatch(()->{
            DirectionalLayout temFirstCoffee = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_template_coffee_first,null,false);

            Text categoryTxt = (Text) temFirstCoffee.findComponentById(ResourceTable.Id_category_title);
            categoryTxt.setText(categoryTitle);

            Text desc = (Text) temFirstCoffee.findComponentById(ResourceTable.Id_category_info);

            // 判断有没有换行符
            if (categoryDesc!=null && !categoryDesc.equals("")){
                if (categoryDesc.startsWith("两行")){
                    String[] texts = categoryDesc.substring(2).split("\\\\n");
                    Text desc_second = (Text) temFirstCoffee.findComponentById(ResourceTable.Id_category_info_second);
                    desc.setText(texts[0]);
                    desc_second.setText(texts[1]);
                    desc_second.setVisibility(Component.VISIBLE);
                }else {
                    desc.setText(categoryDesc);
                }
            }

            initCoffee(id,temFirstCoffee);

            coffeeLayout.addComponent(temFirstCoffee);
        });
    }

    public void initCoffee(int id,DirectionalLayout coffeeDirectionalLayout){
        TaskDispatcher globalTaskDispatcher = getGlobalTaskDispatcher(TaskPriority.DEFAULT);
        globalTaskDispatcher.asyncDispatch(() -> {
            String url = "http://10.16.28.124:8081/luckin/coffee?id=" + id;
            String coffeeString = HttpRequestUtil.sendGetRequest(this, url, null);

            ResultVo resultVo = gson.fromJson(coffeeString,ResultVo.class);
            if (resultVo.getCode() == 200){
                String dataString = gson.toJson(resultVo.getData());
                List<Coffee> coffees = gson.fromJson(dataString,new TypeToken<List<Coffee>>(){}.getType());

                everyCoffee = new ArrayList<>();  // 将每一个布局文件保存起来

                getUITaskDispatcher().asyncDispatch(()->{
                    for(int i = 0;i< coffees.size();i++){
                        Coffee coffee = coffees.get(i);
                        DirectionalLayout temCoffee = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_template_coffee, null, false);

                        //  Image coffeeImg = (Image) temCoffee.findComponentById(ResourceTable.Id_coffee_img);
                        //  LoadImageUtil.loadImg(this, coffee.getImage(), coffeeImg);

                        Text coffeeTitle = (Text) temCoffee.findComponentById(ResourceTable.Id_coffee_title);
                        coffeeTitle.setText(coffee.getTitle());

                        Text coffeePrice = (Text) temCoffee.findComponentById(ResourceTable.Id_coffee_price);
                        coffeePrice.setText("￥"+coffee.getPrice());

                        //获取加减
                        Component btn_minus =  temCoffee.findComponentById(ResourceTable.Id_minus);
                        Component btn_add =  temCoffee.findComponentById(ResourceTable.Id_add);
                        Text txt_num = (Text) temCoffee.findComponentById(ResourceTable.Id_num);

                        int curI = i;
                        btn_add.setClickedListener(component -> {

                            curClickCoffee = curI;

                            showCoffeeMenu();

                            // 切换到咖啡详情页
                            Intent intent1 = new Intent();
                            intent1.setParam("coffeeId",coffee.getId());
                            intent1.setParam("coffeeTitle",coffee.getTitle());
                            intent1.setParam("coffeePrice",coffee.getPrice());
                            presentForResult(new CoffeeDetailSlice(),intent1,200);

                            if (btn_minus.getVisibility() == Component.HIDE) {
                                btn_minus.setVisibility(Component.VISIBLE);
                                txt_num.setVisibility(Component.VISIBLE);
                            }

                        });

                        btn_minus.setClickedListener(component -> {
                            int num = Integer.parseInt(txt_num.getText()) - 1;
                            if (num >= 1){
                                txt_num.setText(num + "");
                            }

                            if (num == 0) {
                                btn_minus.setVisibility(Component.HIDE);
                                txt_num.setVisibility(Component.HIDE);
                                txt_num.setText(0+"");
                                isCoffeeExist.remove(coffee.getTitle());

                            }
                            int curTotalNum = Integer.parseInt(txt_totalNum.getText());
                            txt_totalNum.setText(curTotalNum - 1 + "");

                            unShowCoffeeMenu();
                            countCartPrice(coffee.getPrice(),false);
                            updateCoffeeOrder(false);

                        });

                        everyCoffee.add(temCoffee);
                        coffeeDirectionalLayout.addComponent(temCoffee);

                    }
                });
            }
        });
    }


    // 计算已点菜单价格
    private void countCartPrice(int price, boolean isadd) {
        int total = Integer.parseInt(txt_price.getText().substring(1));
        if (isadd) {
            total = total + price;
        } else {
            total = total - price;
        }
        txt_price.setText("￥"+total);
    }

    // 显示菜单
    private void showCoffeeMenu(){
        Component logo = findComponentById(ResourceTable.Id_btn_cart);
        Component menuInfo = findComponentById(ResourceTable.Id_menu_info_dl);
        Component coffeeNum = findComponentById(ResourceTable.Id_coffee_num_dl);

        logo.setVisibility(Component.HIDE);
        menuInfo.setVisibility(Component.VISIBLE);
        coffeeNum.setVisibility(Component.VISIBLE);
    }

    // 不显示菜单
    private void unShowCoffeeMenu(){
        Component logo = findComponentById(ResourceTable.Id_btn_cart);
        Component menuInfo = findComponentById(ResourceTable.Id_menu_info_dl);
        Component coffeeNum = findComponentById(ResourceTable.Id_coffee_num_dl);

        int curTotalNum = Integer.parseInt(txt_totalNum.getText());
        if (curTotalNum == 0){
            logo.setVisibility(Component.VISIBLE);
            menuInfo.setVisibility(Component.HIDE);
            coffeeNum.setVisibility(Component.HIDE);
        }
    }

    // 显示所有的已点咖啡
    private void showCartCoffee(){
        Component cartDL = findComponentById(ResourceTable.Id_cart_dl);
        Component coffeeOrder = findComponentById(ResourceTable.Id_coffee_order);
        Component discount =findComponentById(ResourceTable.Id_discount_txt);
        cartDL.setClickedListener(component -> {
            if (!isShowCoffeeMenu){
                discount.setVisibility(Component.HIDE);
                coffeeOrder.setVisibility(Component.VISIBLE);
                cartDL.setMarginTop(vp2px(28));
                isShowCoffeeMenu = !isShowCoffeeMenu;
            }else {
                discount.setVisibility(Component.VISIBLE);
                coffeeOrder.setVisibility(Component.HIDE);
                isShowCoffeeMenu = !isShowCoffeeMenu;
                cartDL.setMarginTop(0);
            }
        });
    }

    private int vp2px(int vp) {
        return AttrHelper.vp2px(vp, this);
    }

    private int fp2px(int fp){
        return AttrHelper.fp2px(fp, this);
    }

    @Override
    protected void onResult(int requestCode, Intent resultIntent) {
        super.onResult(requestCode, resultIntent);
        System.out.println("切换回来------------" + "onResult");
        if(requestCode == 200 ){
            sweet = resultIntent.getStringParam("sweet");
            temperature = resultIntent.getStringParam("temperature");
            totalNum = (Integer) resultIntent.getParams().getParam("totalNum");
            title = resultIntent.getStringParam("title");
            price = (Integer) resultIntent.getParams().getParam("price");
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);

        // 更新每一个咖啡后面紧跟的咖啡数量
        DirectionalLayout curCoffee = everyCoffee.get(curClickCoffee);
        Text textNum = (Text) curCoffee.findComponentById(ResourceTable.Id_num);
        int num = Integer.parseInt(textNum.getText()) + totalNum;
        textNum.setText(num + "");
//        System.out.println("点单后数量为:"+textNum.getText());

        // 更新总数量
        int curTotalNum = Integer.parseInt(txt_totalNum.getText());
        txt_totalNum.setText(curTotalNum + totalNum + "");

        // 更新当前总价
        countCartPrice(totalNum * price,true);

        // 加载已点咖啡到核算栏
        if (!isCoffeeExist.contains(title)){
            initCoffeeOrder();
            isCoffeeExist.add(title);
        }else{
            updateCoffeeOrder(true);
        }

    }


    private void initCoffeeOrder(){
        DirectionalLayout tempOrder = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_template_ordered, null, false);
        Text tempTitle = (Text) tempOrder.findComponentById(ResourceTable.Id_coffee_title);
        Text tempPrice = (Text) tempOrder.findComponentById(ResourceTable.Id_coffee_price);
        Text tempTaste = (Text) tempOrder.findComponentById(ResourceTable.Id_coffee_taste);
        Text tempNum = (Text) tempOrder.findComponentById(ResourceTable.Id_coffee_num);

        getUITaskDispatcher().asyncDispatch(() -> {
            tempTitle.setText(title);
            tempPrice.setText("￥"+price);
            tempTaste.setText(temperature+"/"+sweet);
            tempNum.setText(totalNum + "");
            coffeeOrdered.addComponent(tempOrder);
        });

        tempOrderCoffee.put(curClickCoffee, tempOrder);
    }

    /**
     * 更新 底部的已点咖啡
     * @param isAdd:判断是否点击的是添加按钮
     */
    private void updateCoffeeOrder(boolean isAdd){
        DirectionalLayout curClickOrderCoffee = tempOrderCoffee.get(curClickCoffee);
        Text tempNum = (Text) curClickOrderCoffee.findComponentById(ResourceTable.Id_coffee_num);
        int num = Integer.parseInt(tempNum.getText());
        if (isAdd){
            tempNum.setText(num + totalNum + "");
        } else {
            tempNum.setText(num - 1 + "");
        }
    }

    @Override
    protected void onBackground() {
        super.onBackground();
    }
}
