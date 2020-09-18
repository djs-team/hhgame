package com.deepsea.mua.voice.lrc;


import com.deepsea.mua.voice.lrc.impl.LrcRow;

import java.util.List;

/**
 * 展示歌词的接口
 */
public interface ILrcView {

    /**
     * 设置要展示的歌词行集合
     */
    void setLrc(List<LrcRow> lrcRows);

    /**
     * 设置一行歌词
     *
     * @param lrcRow
     */
    void setLrcOneLine(LrcRow lrcRow);

    /**
     * 获取要展示的歌词行集合
     */
    List<LrcRow> getLrc();

    /**
     * 获取高亮的歌词
     *
     * @return
     */
    LrcRow getCurrentLrc();

    /**
     * 获取下一首歌曲
     *
     * @return
     */
    LrcRow getNextLrc();

    /**
     * 音乐播放的时候调用该方法滚动歌词，高亮正在播放的那句歌词
     */
    void seekLrcToTime(long time);

    /**
     * 设置歌词拖动时候的监听类
     */
    void setListener(ILrcViewListener listener);

    /**
     * 设置默认无歌词时候--展示
     */
    void setLoadingTipText(String tipText);

    void stopShowLrc(boolean isStop);

    int  getHightLightPos();
}
