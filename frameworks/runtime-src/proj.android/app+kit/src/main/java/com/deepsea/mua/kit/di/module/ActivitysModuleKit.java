package com.deepsea.mua.kit.di.module;

import com.deepsea.mua.app.im.mua.ChatSettingActivity;
import com.deepsea.mua.app.im.mua.FriendAddActivity;
import com.deepsea.mua.app.im.mua.FriendRequestActivity;
import com.deepsea.mua.app.im.mua.MessageActivity;
import com.deepsea.mua.app.im.mua.MessageMainActivity;
import com.deepsea.mua.app.im.mua.MsgSettingActivity;
import com.deepsea.mua.app.im.mua.MyFriendActivity;
import com.deepsea.mua.app.im.mua.SystemMsgActivity;
import com.deepsea.mua.app.im.ui.ChatActivity;
import com.deepsea.mua.app.im.ui.MessageMainFragment;
import com.deepsea.mua.kit.di.scope.ActivityScope;
import com.deepsea.mua.mine.activity.ApplyHostActivity;
import com.deepsea.mua.mine.activity.AssistActivity;
import com.deepsea.mua.mine.activity.BindPhoneActivity;
import com.deepsea.mua.mine.activity.BindWechatActivity;
import com.deepsea.mua.mine.activity.BlockListActivity;
import com.deepsea.mua.mine.activity.BlueRoseExchangeActivity;
import com.deepsea.mua.mine.activity.CancellationActivity;
import com.deepsea.mua.mine.activity.CodeOfConductActivity;
import com.deepsea.mua.mine.activity.CollectionAccountSettingActivity;
import com.deepsea.mua.mine.activity.CrashWithDrawalDetailsActivity;
import com.deepsea.mua.mine.activity.CrashWithdrawalActivity;
import com.deepsea.mua.mine.activity.ExchangeBlueRoseExchangeActivity;
import com.deepsea.mua.mine.activity.ExchangeMdActivity;
import com.deepsea.mua.mine.activity.ExchangeMdDetailsActivity;
import com.deepsea.mua.mine.activity.FeedResultActivity;
import com.deepsea.mua.mine.activity.FeedbackActivity;
import com.deepsea.mua.mine.activity.FollowAndFanActivity;
import com.deepsea.mua.mine.activity.GuardActivity;
import com.deepsea.mua.mine.activity.GuardRuleActivity;
import com.deepsea.mua.mine.activity.IncomeDetailsActivity;
import com.deepsea.mua.mine.activity.IncomeRedpackageDetailsActivity;
import com.deepsea.mua.mine.activity.InvitationCodeActivity;
import com.deepsea.mua.mine.activity.InvitationMineActivity;
import com.deepsea.mua.mine.activity.InviteAlertActivity;
import com.deepsea.mua.mine.activity.InviteDialogActivity;
import com.deepsea.mua.mine.activity.MarriageSeekingActivity;
import com.deepsea.mua.mine.activity.MePackActivity;
import com.deepsea.mua.mine.activity.MyGuardActivity;
import com.deepsea.mua.mine.activity.MyTagsActivity;
import com.deepsea.mua.mine.activity.PersonalLevelActivity;
import com.deepsea.mua.mine.activity.PresentIdentityAuthActivity;
import com.deepsea.mua.mine.activity.PresentWallActivity;
import com.deepsea.mua.mine.activity.ProfileActivity;
import com.deepsea.mua.mine.activity.ProfileEditActivity;
import com.deepsea.mua.mine.activity.QueryDateDetailsActivity;
import com.deepsea.mua.mine.activity.RechargeActivity;
import com.deepsea.mua.mine.activity.RechargeDialogActivity;
import com.deepsea.mua.mine.activity.SafetyCenterActivity;
import com.deepsea.mua.mine.activity.ScreenDialogActivity;
import com.deepsea.mua.mine.activity.SettingActivity;
import com.deepsea.mua.mine.activity.TaskCenterActivity;
import com.deepsea.mua.mine.activity.TaskCenterDialogActivity;
import com.deepsea.mua.mine.activity.TaskProblemActivity;
import com.deepsea.mua.mine.activity.VisitorsActivity;
import com.deepsea.mua.mine.activity.WalletActivity;
import com.deepsea.mua.mine.activity.WalletRecordActivity;
import com.deepsea.mua.mine.activity.WebActivity;
import com.deepsea.mua.voice.activity.FansRankActivity;
import com.deepsea.mua.voice.activity.HeartRankActivity;
import com.deepsea.mua.voice.activity.MineRoomActivity;
import com.deepsea.mua.voice.activity.RoomActivity;
import com.deepsea.mua.voice.activity.RoomCreateActivity;
import com.deepsea.mua.voice.activity.RoomCreateNewActivity;
import com.deepsea.mua.voice.activity.RoomModeSetActivity;
import com.deepsea.mua.voice.activity.RoomModelHelpActivity;
import com.deepsea.mua.voice.activity.RoomNameSetActivity;
import com.deepsea.mua.voice.activity.RoomRankActivity;
import com.deepsea.mua.voice.activity.RoomReportActivity;
import com.deepsea.mua.voice.activity.RoomSearchActivity;
import com.deepsea.mua.voice.activity.RoomSetActivity;
import com.deepsea.mua.voice.activity.RoomWelSetActivity;
import com.deepsea.mua.voice.activity.SearchMoreActivity;
import com.deepsea.mua.voice.activity.WealthRankActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by JUN on 2019/3/24
 */
@Module
public abstract class ActivitysModuleKit {

    @ActivityScope
    @ContributesAndroidInjector
    abstract MarriageSeekingActivity contributesMarriageSeekingActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract HeartRankActivity contributesHeartRankActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract WealthRankActivity contributesWealthRankActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract SystemMsgActivity contributesSystemMsgActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract AssistActivity contributesAssistActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MePackActivity contributesMePackActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MessageActivity contributesMessageActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract FansRankActivity contributesFansRankActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract WebActivity contributesWebActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ChatActivity contributesChatActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract VisitorsActivity contributesVisitorsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MsgSettingActivity contributesMsgSettingActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract FeedbackActivity contributesFeedbackActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract SettingActivity contributesSettingActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract FeedResultActivity contributesFeedResultActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ExchangeMdActivity contributesExchangeMdActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract WalletRecordActivity contributesWalletRecordActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract WalletActivity contributesWalletActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract FollowAndFanActivity contributesFollowAndFanActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract SearchMoreActivity contributesSearchMoreActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomSearchActivity contributesRoomSearchActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MineRoomActivity contributesMineRoomActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ProfileEditActivity contributesProfileEditActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RechargeActivity contributesRechargeActivity();

//    @ActivityScope
//    @ContributesAndroidInjector
//    abstract LoginActivity contributesLoginActivity();

//    @ActivityScope
//    @ContributesAndroidInjector
//    abstract SplashActivity contributesSplashActivity();

//    @ActivityScope
//    @ContributesAndroidInjector
//    abstract BindPhoneActivity contributesBindPhoneActivity();

//    @ActivityScope
//    @ContributesAndroidInjector
//    abstract MainActivity contributesMainActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomActivity contributesVoiceRoomActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomSetActivity contributesVoiceRoomSetActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomNameSetActivity contributesRoomNameSetActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomWelSetActivity contributesRoomWelSetActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomModeSetActivity contributesRoomModeSetActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomCreateActivity contributesVoiceRoomCreateActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomReportActivity contributesRoomReportActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomRankActivity contributesRoomRankActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ProfileActivity contributesProfileActivity();


    @ActivityScope
    @ContributesAndroidInjector
    abstract PersonalLevelActivity contributesPersonalLevelActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract PresentIdentityAuthActivity contributesPresentIdentityAuthActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract PresentWallActivity contributesPresentWallActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract FriendAddActivity contributesPresentFriendAddActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract FriendRequestActivity contributesPresentFriendRequestActivity();


    @ActivityScope
    @ContributesAndroidInjector
    abstract InvitationCodeActivity contributesPresentInvitationCodeActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract InvitationMineActivity contributesPresentInvitationMineActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract CollectionAccountSettingActivity contributesPresentCollectionAccountSettingActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract CrashWithdrawalActivity contributesPresentCrashWithdrawalActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract CrashWithDrawalDetailsActivity contributesPresentCrashWithDrawalDetailsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract IncomeDetailsActivity contributesPresentIncomeDetailsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract QueryDateDetailsActivity contributesPresentQueryDateDetailsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract InviteDialogActivity contributesPresentInviteDialogActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RechargeDialogActivity contributesPresentRechargeDialogActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract SafetyCenterActivity contributesPresentSafetyCenterActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract CancellationActivity contributesPresentCancellationActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract CodeOfConductActivity contributesPresentCodeOfConductActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ExchangeMdDetailsActivity contributesPresentExchangeMdDetailsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MyTagsActivity contributesMyTagsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract InviteAlertActivity contributesInviteAlertActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MessageMainActivity contributesMessageMainActivity();


    @ActivityScope
    @ContributesAndroidInjector
    abstract BlueRoseExchangeActivity contributesBlueRoseExchangeActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract BindWechatActivity contributesBindWechatActivity();


    @ActivityScope
    @ContributesAndroidInjector
    abstract ExchangeBlueRoseExchangeActivity contributesExchangeBlueRoseExchangeActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomModelHelpActivity contributeRoomModelHelpActivity();



    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomCreateNewActivity contributeRoomCreateNewActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ApplyHostActivity contributeApplyHostActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ScreenDialogActivity contributeScreenDialogActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract GuardActivity contributeGuardActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract GuardRuleActivity contributeGuardRuleActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract TaskCenterActivity contributeTaskCenterActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract TaskProblemActivity contributeTaskProblemActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract TaskCenterDialogActivity contributeTaskCenterDialogActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MyFriendActivity contributeMyFriendActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MyGuardActivity contributeMyGuardActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract BlockListActivity contributeBlockListActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ChatSettingActivity contributeChatSettingActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract BindPhoneActivity contributeBindPhoneActivity();
    @ActivityScope
    @ContributesAndroidInjector
    abstract IncomeRedpackageDetailsActivity contributeIncomeRedpackageDetailsActivity();


}
