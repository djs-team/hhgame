package com.deepsea.mua.stub.entity;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by JUN on 2019/10/16
 */
public class ProfileModel {

    private String jyxs;    //交友心声
    private MenuBean jbxx;  //基本信息
    private MenuBean grzl;  //详细资料

    private transient List<ProfileMenu> mOptions;

    public String getJyxs() {
        return jyxs;
    }

    public void setJyxs(String jyxs) {
        this.jyxs = jyxs;
    }

    public MenuBean getJbxx() {
        return jbxx;
    }

    public void setJbxx(MenuBean jbxx) {
        this.jbxx = jbxx;
    }

    public MenuBean getGrzl() {
        return grzl;
    }

    public void setGrzl(MenuBean grzl) {
        this.grzl = grzl;
    }

    public List<ProfileMenu> getOptions() {
        return mOptions;
    }

    public void setOptions(List<ProfileMenu> options) {
        mOptions = options;
    }

    public static class MenuBean {
        private String name;
        private List<ProfileMenu> menus;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ProfileMenu> getMenus() {
            return menus;
        }

        public void setMenus(List<ProfileMenu> menus) {
            this.menus = menus;
        }
    }

    public static class ProfileMenu {
        //1值单选,2数字多选,3值多选,4输入框,5数字单选,6城市,7多级
        private String mold;
        private String column;
        private String type_name;
        private String name;
        private String default_name;
        private List<String> list;
        private LinkedHashMap<String, List<String>> option;

        private transient String menuName;

        public String getMold() {
            return mold;
        }

        public void setMold(String mold) {
            this.mold = mold;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDefault_name() {
            return default_name;
        }

        public void setDefault_name(String default_name) {
            this.default_name = default_name;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public LinkedHashMap<String, List<String>> getOption() {
            return option;
        }

        public void setOption(LinkedHashMap<String, List<String>> option) {
            this.option = option;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }
    }
}
