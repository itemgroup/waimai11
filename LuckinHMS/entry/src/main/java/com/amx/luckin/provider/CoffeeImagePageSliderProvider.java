package com.amx.luckin.provider;

import com.amx.luckin.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.util.List;

public class CoffeeImagePageSliderProvider extends PageSliderProvider {

    private List<Integer> images;
    private AbilitySlice abilitySlice;

    public CoffeeImagePageSliderProvider(List<Integer> images, AbilitySlice abilitySlice) {
        this.images = images;
        this.abilitySlice = abilitySlice;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        DirectionalLayout template = (DirectionalLayout) LayoutScatter.getInstance(abilitySlice).parse(ResourceTable.Layout_template_coffee_detail, null, false);
        Image detailImage = (Image) template.findComponentById(ResourceTable.Id_coffee_image);

        int pixel = images.get(i);
        detailImage.setPixelMap(pixel);

        componentContainer.addComponent(template);
        return template;
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        //滑出屏幕的组件进行移除
        componentContainer.removeComponent((Component) o);
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return true;
    }
}
