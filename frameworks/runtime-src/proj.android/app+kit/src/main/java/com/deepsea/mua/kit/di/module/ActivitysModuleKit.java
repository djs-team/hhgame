package com.deepsea.mua.kit.di.module;

import com.deepsea.mua.app.im.mua.ChatSettingActivity;
import com.deepsea.mua.app.im.mua.FriendAddActivity;
import com.deepsea.mua.app.im.mua.FriendRequestActivity;
import com.deepsea.mua.app.im.mua.MessageActivity;
import com.deepsea.mua.app.im.mua.MessageMainActivity;
import com.deepsea.mua.app.im.mua.MyFriendActivity;
import com.deepsea.mua.app.im.mua.SystemMsgActivity;
import com.deepsea.mua.app.im.ui.ChatActivity;
import com.deepsea.mua.kit.di.scope.ActivityScope;
import com.deepsea.mua.mine.activity.ApplyHostActivity;
import com.deepsea.mua.mine.activity.AssistActivity;
import com.deepsea.mua.mine.activity.BindPhoneActivity;
import com.deepsea.mua.mine.activity.BindWechatActivity;
import com.deepsea.mua.mine.activity.BlockListActivity;
import com.deepsea.mua.mine.activity.CollectionAccountSettingActivity;
import com.deepsea.mua.mine.activity.CrashWithDrawalDetailsActivity;
import com.deepsea.mua.mine.activity.CrashWithdrawalActivity;
import com.deepsea.mua.mine.activity.ExchangeMdActivity;
import com.deepsea.mua.mine.activity.ExchangeMdDetailsActivity;
import com.deepsea.mua.mine.activity.FeedResultActivity;
import com.deepsea.mua.mine.activity.FeedbackActivity;
import com.deepsea.mua.mine.activity.GuardActivity;
import com.deepsea.mua.mine.activity.GuardRuleActivity;
import com.deepsea.mua.mine.activity.IncomeDetailsActivity;
import com.deepsea.mua.mine.activity.IncomeRedpackageDetailsActivity;
import com.deepsea.mua.mine.activity.InviteDialogActivity;
import com.deepsea.mua.mine.activity.MarriageSeekingActivity;
import com.deepsea.mua.mine.activity.MyGuardActivity;
import com.deepsea.mua.mine.activity.MyTagsActivity;
import com.deepsea.mua.mine.activity.PresentWallActivity;
import com.deepsea.mua.mine.activity.ProfileActivity;
import com.deepsea.mua.mine.activity.ProfileEditActivity;
import com.deepsea.mua.mine.activity.QueryDateDetailsActivity;
import com.deepsea.mua.mine.activity.RechargeActivity;
import com.deepsea.mua.mine.activity.RechargeDialogActivity;
import com.deepsea.mua.mine.activity.ScreenDialogActivity;
import com.deepsea.mua.mine.activity.UploadPhotoDialogActivity;
import com.deepsea.mua.mine.activity.WalletActivity;
import com.deepsea.mua.mine.activity.WalletRecordActivity;
import com.deepsea.mua.mine.activity.WebActivity;
import com.deepsea.mua.voice.activity.RoomActivity;
import com.deepsea.mua.voice.activity.RoomCreateNewActivity;
import com.deepsea.mua.voice.activity.RoomModeSetActivity;
import com.deepsea.mua.voice.activity.RoomModelHelpActivity;
import com.deepsea.mua.voice.activity.RoomNameSetActivity;
import com.deepsea.mua.voice.activity.RoomReportActivity;
import com.deepsea.mua.voice.activity.RoomSearchActivity;
import com.deepsea.mua.voice.activity.RoomSetActivity;
import com.deepsea.mua.voice.activity.RoomWelSetActivity;
import com.deepsea.mua.voice.activity.SearchMoreActivity;

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
    abstract SystemMsgActivity contributesSystemMsgActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract AssistActivity contributesAssistActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MessageActivity contributesMessageActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract WebActivity contributesWebActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ChatActivity contributesChatActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract FeedbackActivity contributesFeedbackActivity();

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
    abstract SearchMoreActivity contributesSearchMoreActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomSearchActivity contributesRoomSearchActivity();

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
    abstract RoomReportActivity contributesRoomReportActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ProfileActivity contributesProfileActivity();


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
    abstract ExchangeMdDetailsActivity contributesPresentExchangeMdDetailsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MyTagsActivity contributesMyTagsActivity();


    @ActivityScope
    @ContributesAndroidInjector
    abstract MessageMainActivity contributesMessageMainActivity();


    @ActivityScope
    @ContributesAndroidInjector
    abstract BindWechatActivity contributesBindWechatActivity();


    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomModelHelpActivity contributeRoomModelHelpActivity();


    @ActivityScope
    @ContributesAndroidInjector
    abstract RoomCreateNewActivity contributeRoomCreateNewActivity();


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

    @ActivityScope
    @ContributesAndroidInjector
    abstract UploadPhotoDialogActivity contributeUploadPhotoDialogActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ApplyHostActivity contributeApplyHostActivity();


}
