package com.deepsea.mua.kit.di.module;

import com.deepsea.mua.app.im.mua.ConversationFragment;
import com.deepsea.mua.app.im.mua.FriendApplyFragment;
import com.deepsea.mua.app.im.mua.FriendMessageFragment;
import com.deepsea.mua.app.im.mua.FriendMineFragment;
import com.deepsea.mua.app.im.mua.GiftChatFragment;
import com.deepsea.mua.app.im.mua.GiftKnapsackFragment;
import com.deepsea.mua.app.im.mua.GiftPanelFragment;
import com.deepsea.mua.app.im.mua.MessageFragment;
import com.deepsea.mua.app.im.mua.SystemMsgFragment;
import com.deepsea.mua.app.im.ui.MessageMainFragment;
import com.deepsea.mua.mine.fragment.FanFragment;
import com.deepsea.mua.mine.fragment.FollowFragment;
import com.deepsea.mua.mine.fragment.GuardInfoFragment;
import com.deepsea.mua.mine.fragment.MDFragment;
import com.deepsea.mua.mine.fragment.MineFragment;
import com.deepsea.mua.voice.fragment.FullServiceManFragment;
import com.deepsea.mua.voice.fragment.FullServiceWomenFragment;
import com.deepsea.mua.voice.fragment.RecentActiveFragment;
import com.deepsea.mua.voice.fragment.RoomFragment;
import com.deepsea.mua.voice.fragment.SongApointmentFragment;
import com.deepsea.mua.voice.fragment.SongBanchangFragment;
import com.deepsea.mua.voice.fragment.SongOriginalFragment;
import com.deepsea.mua.voice.fragment.SongPlayingFragment;
import com.deepsea.mua.voice.fragment.SongRankFragment;
import com.deepsea.mua.voice.fragment.SortApplyMicForManyFragment;
import com.deepsea.mua.voice.fragment.SortApplyMicFragment;
import com.deepsea.mua.voice.fragment.SortFriendsFragment;
import com.deepsea.mua.voice.fragment.SortRecentActiveForManyFragment;
import com.deepsea.mua.voice.fragment.SortVisitorInRoomForManyFragment;
import com.deepsea.mua.voice.fragment.SortVisitorInRoomFragment;
import com.deepsea.mua.voice.fragment.VoiceFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by JUN on 2019/3/24
 */
@Module
public abstract class FragmentsModuleKit {

    @ContributesAndroidInjector
    abstract ConversationFragment contributesConversationFragment();

    @ContributesAndroidInjector
    abstract FollowFragment contributesFollowFragment();

    @ContributesAndroidInjector
    abstract FanFragment contributesFanFragment();

    @ContributesAndroidInjector
    abstract MDFragment contributesMDFragment();

    @ContributesAndroidInjector
    abstract VoiceFragment contributesVoiceFragment();

    @ContributesAndroidInjector
    abstract MessageFragment contributesMessageFragment();

    @ContributesAndroidInjector
    abstract MineFragment contributesMineFragment();

    @ContributesAndroidInjector
    abstract RoomFragment contributesRoomFragment();


    @ContributesAndroidInjector
    abstract FriendMessageFragment contributesFriendMessageFragment();

    @ContributesAndroidInjector
    abstract GiftPanelFragment contributesGiftPanelFragment();

    @ContributesAndroidInjector
    abstract SystemMsgFragment contributesSystemMsgFragment();

    @ContributesAndroidInjector
    abstract MessageMainFragment contributesMessageMainFragment();

    @ContributesAndroidInjector
    abstract GiftChatFragment contributesGiftChatFragment();

    @ContributesAndroidInjector
    abstract GiftKnapsackFragment contributesGiftKnapsackFragment();

    @ContributesAndroidInjector
    abstract FullServiceManFragment contributesFullServiceManFragment();

    @ContributesAndroidInjector
    abstract FullServiceWomenFragment contributesFullServiceWomenFragment();

    @ContributesAndroidInjector
    abstract SortApplyMicFragment contributesSortApplyMicFragment();

    @ContributesAndroidInjector
    abstract SortVisitorInRoomFragment contributesSortVisitorInRoomFragment();

    @ContributesAndroidInjector
    abstract RecentActiveFragment contributesRecentActiveFragment();


    @ContributesAndroidInjector
    abstract SongPlayingFragment contributesSongPlayingFragment();

    @ContributesAndroidInjector
    abstract SongOriginalFragment contributesSongOriginalFragment();

    @ContributesAndroidInjector
    abstract SongApointmentFragment contributesSongApointmentFragment();

    @ContributesAndroidInjector
    abstract SongBanchangFragment contributesSongBanchangFragment();

    @ContributesAndroidInjector
    abstract SongRankFragment contributesSongRankFragment();

    @ContributesAndroidInjector
    abstract SortApplyMicForManyFragment contributesSortApplyMicForManyFragment();

    @ContributesAndroidInjector
    abstract SortRecentActiveForManyFragment contributesSortRecentActiveForManyFragment();

    @ContributesAndroidInjector
    abstract SortVisitorInRoomForManyFragment contributesSortVisitorInRoomForManyFragment();

    @ContributesAndroidInjector
    abstract SortFriendsFragment contributesSortFriendsFragment();

    @ContributesAndroidInjector
    abstract FriendMineFragment contributesFriendMineFragment();

    @ContributesAndroidInjector
    abstract FriendApplyFragment contributesFriendApplyFragment();

    @ContributesAndroidInjector
    abstract GuardInfoFragment contributesGuardInfoFragment();

}
