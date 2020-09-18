package com.deepsea.mua.stub.utils.eventbus;

/**
 * @author lyx
 * @date 2018/10/24
 */
public interface ClickEventType {
    int Click1 = 0X01;//详情点赞取消 点赞 通知到动态列表
    int Click2 = 0X02;//详情关注取消 关注 通知到动态列表
    int Click3 = 0X03;//详情删除按钮，通知到列表
    int Click4 = 0X04;//发布动态成功通知刷新列表
    int Click5 = 0X05;//动态详情评论了更新到列表
    int Click6 = 0X06;//录音成功了，回传到发布页面
    int Click7 = 0X07;//拉黑成功
    int Click8 = 0X08;//更新用户信息了
    int Click9 = 0X09;//删除私聊会话


}
