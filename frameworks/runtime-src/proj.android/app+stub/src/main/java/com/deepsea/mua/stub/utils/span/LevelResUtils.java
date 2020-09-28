package com.deepsea.mua.stub.utils.span;

import com.deepsea.mua.stub.R;

/**
 * Created by JUN on 2019/4/19
 * 等级图片资源
 */
public class LevelResUtils {

    /**
     * 爵位等级图片资源
     *
     * @param level
     * @return
     */
    public static int getNobilityRes(int level) {
        switch (level) {
            case 1:
                return R.drawable.ic_duke_1;
            default:
                return R.drawable.ic_duke_1;
        }
    }

    /**
     * 守护等级图片资源
     *
     * @param level
     * @return
     */
    public static int getGaurdRes(int level) {
        switch (level) {
            case 1:
                return R.drawable.ic_guard_1;
            default:
                return R.drawable.ic_guard_1;
        }
    }



    /**
     * 用户等级图片资源
     *
     * @param level
     * @return
     */
    public static int getLevelRes(int level) {
        if (level >= 1 && level < 6) {
            return R.drawable.ic_level_1;
        } else if (level >= 6 && level < 11) {
            return R.drawable.ic_level_2;
        } else if (level >= 11 && level < 16) {
            return R.drawable.ic_level_3;
        } else if (level >= 16 && level < 21) {
            return R.drawable.ic_level_4;
        } else if (level >= 21 && level < 26) {
            return R.drawable.ic_level_5;
        } else if (level >= 26) {
            return R.drawable.ic_level_6;
        }
        return R.drawable.ic_level_1;
    }

    /**
     * 用户发送文字颜色
     *
     * @param level 用户等级
     * @return
     */
    public static int getSendMsgColor(int level) {
        if (level >= 1 && level < 6) {
            return 0xFFDCDCDC;
        } else if (level >= 6 && level < 11) {
            return 0xFF7AFFC3;
        } else if (level >= 11 && level < 16) {
            return 0xFF85C9FF;
        } else if (level >= 16 && level < 21) {
            return 0xFFDD83FF;
        } else if (level >= 21 && level < 26) {
            return 0xFFFFB080;
        } else if (level >= 26) {
            return 0xFFFFE44B;
        }
        return 0xFFDCDCDC;
    }

    /**
     * 系统消息颜色
     *
     * @param level
     * @return
     */
    public static int getSystemMsgColor(int level) {
        switch (level) {
            case 1:
                return 0xFF25FFFB;
            default:
                return 0xFF25FFFB;
        }
    }

    /**
     * 用户身份图标
     *
     * @param identity
     * @return
     */
    public static int getUserIdentity(int identity) {
        switch (identity) {
            case 1:
                return R.drawable.ic_manager_bg;
            case 2:
                return R.drawable.ic_host_bg;
        }
        return R.drawable.ic_manager_bg;
    }
}
