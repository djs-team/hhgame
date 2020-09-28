package com.deepsea.mua.kit.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.deepsea.mua.app.im.mua.MsgSettingViewModel;
import com.deepsea.mua.app.im.mua.SystemMsgViewModel;
import com.deepsea.mua.app.im.viewmodel.AddFriendViewModel;
import com.deepsea.mua.app.im.viewmodel.FriendListViewModel;
import com.deepsea.mua.app.im.viewmodel.FriendRequestViewModel;
import com.deepsea.mua.app.im.viewmodel.GiftChatViewModel;
import com.deepsea.mua.app.im.viewmodel.GiftKnapsackModel;
import com.deepsea.mua.app.im.viewmodel.GiftPanelViewModel;
import com.deepsea.mua.app.im.viewmodel.SysMsgViewModel;
import com.deepsea.mua.core.di.mapkey.ViewModelKey;
import com.deepsea.mua.mine.viewmodel.BlindDateModel;
import com.deepsea.mua.mine.viewmodel.CashWithdrawalDetailsModel;
import com.deepsea.mua.mine.viewmodel.CashWithdrawalModel;
import com.deepsea.mua.mine.viewmodel.CollectionAccountModel;
import com.deepsea.mua.mine.viewmodel.ExchangeMdDetailsModel;
import com.deepsea.mua.mine.viewmodel.ExchangeMdViewModel;
import com.deepsea.mua.mine.viewmodel.H5ViewModel;
import com.deepsea.mua.mine.viewmodel.IncomeDetailsModel;
import com.deepsea.mua.mine.viewmodel.InviteDialogViewModel;
import com.deepsea.mua.mine.viewmodel.ProfileEditViewModel;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.mine.viewmodel.RechargeViewModel;
import com.deepsea.mua.mine.viewmodel.TagsModel;
import com.deepsea.mua.mine.viewmodel.WalletRecordViewModel;
import com.deepsea.mua.mine.viewmodel.WalletViewModel;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.viewmodel.HomeViewModel;
import com.deepsea.mua.voice.viewmodel.RoomCreateViewModel;
import com.deepsea.mua.voice.viewmodel.RoomManagerViewModel;
import com.deepsea.mua.voice.viewmodel.RoomReportViewModel;
import com.deepsea.mua.voice.viewmodel.RoomSearchViewModel;
import com.deepsea.mua.voice.viewmodel.RoomSetViewModel;
import com.deepsea.mua.voice.viewmodel.RoomViewModel;
import com.deepsea.mua.voice.viewmodel.RoomsViewModel;
import com.deepsea.mua.voice.viewmodel.SongOriginalViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by JUN on 2019/3/26
 */
@Module
public abstract class ViewModelModuleKit {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);


    @Binds
    @IntoMap
    @ViewModelKey(SystemMsgViewModel.class)
    abstract ViewModel bindsSystemMsgViewModel(SystemMsgViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(ExchangeMdViewModel.class)
    abstract ViewModel bindsExchangeMdViewModel(ExchangeMdViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WalletRecordViewModel.class)
    abstract ViewModel bindsWalletRecordViewModel(WalletRecordViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WalletViewModel.class)
    abstract ViewModel bindsWalletViewModel(WalletViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(RoomSearchViewModel.class)
    abstract ViewModel bindsRoomSearchViewModel(RoomSearchViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RoomsViewModel.class)
    abstract ViewModel bindsRoomsViewModel(RoomsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindsHomeViewModel(HomeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileEditViewModel.class)
    abstract ViewModel bindsProfileEditViewModel(ProfileEditViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RechargeViewModel.class)
    abstract ViewModel bindsRechargeViewModel(RechargeViewModel viewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(SplashViewModel.class)
//    abstract ViewModel bindsSplashViewModel(SplashViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RoomViewModel.class)
    abstract ViewModel bindVoiceRoomViewModel(RoomViewModel viewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(LoginViewModel.class)
//    abstract ViewModel bindLoginViewModel(LoginViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RoomCreateViewModel.class)
    abstract ViewModel bindVoiceRoomCreateViewModel(RoomCreateViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RoomSetViewModel.class)
    abstract ViewModel bindVoiceRoomSetViewModel(RoomSetViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RoomReportViewModel.class)
    abstract ViewModel bindVoiceRoomReportViewModel(RoomReportViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RoomManagerViewModel.class)
    abstract ViewModel bindRoomManagerViewModel(RoomManagerViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel bindProfileViewModel(ProfileViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(FriendRequestViewModel.class)
    abstract ViewModel bindsFriendRequestViewModel(FriendRequestViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GiftPanelViewModel.class)
    abstract ViewModel bindsGiftPanelViewModel(GiftPanelViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SysMsgViewModel.class)
    abstract ViewModel bindsSysMsgViewModel(SysMsgViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddFriendViewModel.class)
    abstract ViewModel bindAddFriendViewModel(AddFriendViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FriendListViewModel.class)
    abstract ViewModel bindFriendListViewModel(FriendListViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GiftChatViewModel.class)
    abstract ViewModel bindsGiftChatViewModel(GiftChatViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GiftKnapsackModel.class)
    abstract ViewModel bindsGiftKnapsackModel(GiftKnapsackModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(IncomeDetailsModel.class)
    abstract ViewModel bindsIncomeDetailsModel(IncomeDetailsModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CashWithdrawalDetailsModel.class)
    abstract ViewModel bindsCashWithdrawalDetailsModel(CashWithdrawalDetailsModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CashWithdrawalModel.class)
    abstract ViewModel bindsCashWithdrawalModel(CashWithdrawalModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CollectionAccountModel.class)
    abstract ViewModel bindsCollectionAccountModel(CollectionAccountModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(InviteDialogViewModel.class)
    abstract ViewModel bindsInviteDialogViewModel(InviteDialogViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(H5ViewModel.class)
    abstract ViewModel bindsH5ViewModel(H5ViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ExchangeMdDetailsModel.class)
    abstract ViewModel bindsExchangeMdDetailsModel(ExchangeMdDetailsModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BlindDateModel.class)
    abstract ViewModel bindsBlindDateModel(BlindDateModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TagsModel.class)
    abstract ViewModel bindsTagsModel(TagsModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SongOriginalViewModel.class)
    abstract ViewModel bindsSongOriginalViewModel(SongOriginalViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MsgSettingViewModel.class)
    abstract ViewModel bindsMsgSettingViewModel(MsgSettingViewModel viewModel);
}
