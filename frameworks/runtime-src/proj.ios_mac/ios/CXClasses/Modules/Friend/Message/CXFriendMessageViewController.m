//
//  CXFriendMessageViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/10/17.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXFriendMessageViewController.h"
#import "CXFriendMessageCell.h"
#import "CXFriendInviteListViewController.h"
#import "EMChatViewController.h"
#import "EMDateHelper.h"

@interface CXFriendMessageViewController () <EMChatManagerDelegate, UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic, strong) NSMutableArray *dataArrays;

@property (nonatomic, strong) NSMutableDictionary <NSString*, CXFriendInviteModel*> *userDataDict;

@end

@implementation CXFriendMessageViewController

- (void)dealloc {
    
    if (_isConversation) {
        [[EMClient sharedClient].chatManager removeDelegate:self];
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    if (_isConversation) {
        [[EMClient sharedClient].chatManager addDelegate:self delegateQueue:nil];
    }
    
    self.dataArrays = [NSMutableArray array];
    self.userDataDict = [NSMutableDictionary dictionary];

    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    
    [self.tableView registerNib:[UINib nibWithNibName:@"CXFriendMessageCell" bundle:nil] forCellReuseIdentifier:@"CXFriendMessageCellID"];
    self.tableView.tableFooterView = [UIView new];
    kWeakSelf
    self.tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        [weakSelf getFriendListData];
    }];
}

- (void)clearConversationList {
    if (self.dataArrays.count <= 0) {
        return;
    }
    
    [self.dataArrays enumerateObjectsUsingBlock:^(EMConversationModel *  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        EMConversation *conversation = obj.emModel;
        [[EMClient sharedClient].chatManager deleteConversation:conversation.conversationId
        isDeleteMessages:YES
              completion:nil];
    }];
    
    [self getConversationsData];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self getFriendListData];
}

#pragma makr - Http
- (void)getFriendListData {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    kWeakSelf
    [CXHTTPRequest GETWithURL:@"/index.php/Api/Friend/getFriendList" parameters:@{@"signature": signature} callback:^(id responseObject, BOOL isCache, NSError *error) {
        [weakSelf.tableView.mj_header endRefreshing];
        
        if (!error) {
            NSArray *array = [NSArray modelArrayWithClass:[CXFriendInviteModel class] json:responseObject[@"data"][@"list"]];
            
            if (weakSelf.isConversation) {
                NSMutableArray *tempArray = [NSMutableArray array];
                [weakSelf.userDataDict removeAllObjects];
                [array enumerateObjectsUsingBlock:^(CXFriendInviteModel *  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                    [weakSelf.userDataDict setValue:obj forKey:obj.user_id];
                    [tempArray addObject:obj];
                }];
                
                [weakSelf getConversationsData];
            } else {
                weakSelf.dataArrays = [NSMutableArray arrayWithArray:array];
                [weakSelf.tableView reloadData];
            }
        }
    }];
}

- (void)getConversationsData
{
    __weak typeof(self) weakself = self;
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
       NSArray *conversations = [[EMClient sharedClient].chatManager getAllConversations];
       NSArray *sorted = [conversations sortedArrayUsingComparator:^(EMConversation *obj1, EMConversation *obj2) {
            EMMessage *message1 = [obj1 latestMessage];
            EMMessage *message2 = [obj2 latestMessage];
            if(message1.timestamp > message2.timestamp) {
               return(NSComparisonResult)NSOrderedAscending;
            } else {
               return(NSComparisonResult)NSOrderedDescending;
            }
        }];
       
        [weakself.dataArrays removeAllObjects];
        NSMutableArray *tempArray = [NSMutableArray array];
        NSArray *models = [EMConversationHelper modelsFromEMConversations:sorted];
        [weakself.dataArrays addObjectsFromArray:models];
       
        dispatch_async(dispatch_get_main_queue(), ^{
           
           [weakself.tableView reloadData];
        });
    });
}

- (void)_reSortedConversationModelsAndReloadView
{
    NSArray *sorted = [self.dataArrays sortedArrayUsingComparator:^(EMConversationModel *obj1, EMConversationModel *obj2) {
        EMMessage *message1 = [obj1.emModel latestMessage];
        EMMessage *message2 = [obj2.emModel latestMessage];
        if(message1.timestamp > message2.timestamp) {
            return(NSComparisonResult)NSOrderedAscending;
        } else {
            return(NSComparisonResult)NSOrderedDescending;
        }}];

    NSMutableArray *conversationModels = [NSMutableArray array];
    for (EMConversationModel *model in sorted) {
        if (!model.emModel.latestMessage) {
            [EMClient.sharedClient.chatManager deleteConversation:model.emModel.conversationId
                                                 isDeleteMessages:NO
                                                       completion:nil];
            continue;
        }
        [conversationModels addObject:model];
    }
    
    [self.dataArrays removeAllObjects];
    [self.dataArrays addObjectsFromArray:conversationModels];
    [self.tableView reloadData];
}

#pragma mark - UITableViewDataSource/Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataArrays.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSString *CellIdentifier = @"CXFriendMessageCellID";
    CXFriendMessageCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (self.isConversation) {
        cell.isConversation = YES;
        
        EMConversationModel *conversation = self.dataArrays[indexPath.row];
        
        CXFriendInviteModel *model = [_userDataDict objectForKey:conversation.emModel.conversationId];
        cell.model = model;
        
        cell.avatarTapGestureBlock = ^{
            [AppController joinRoom:model.room_id];
        };
        cell.userMessageLabel.hidden = NO;
        cell.userMessageLabel.text = [self _latestMessageTitleForConversationModel:conversation.emModel];
        cell.timeLabel.text = [self _latestMessageTimeForConversationModel:conversation.emModel];
        if (conversation.emModel.unreadMessagesCount == 0) {
            cell.unReadCount.hidden = YES;
        } else {
            cell.unReadCount.hidden = NO;
            cell.unReadCount.text = [NSString stringWithFormat:@"%@", @(conversation.emModel.unreadMessagesCount)];
        }
    } else {
        CXFriendInviteModel *model = self.dataArrays[indexPath.row];
        cell.model = model;
        cell.isConversation = NO;
        cell.userMessageLabel.hidden = YES;
        cell.avatarTapGestureBlock = ^{
            [AppController joinRoom:model.room_id];
        };
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {

    if (_isConversation) {
        EMConversationModel *model = [self.dataArrays objectAtIndex:indexPath.row];
        if (model) {
            EMChatViewController *controller = [[EMChatViewController alloc] initWithCoversationModel:model];
            CXFriendInviteModel *friendModel = [_userDataDict objectForKey:model.emModel.conversationId];
            controller.friendModel = friendModel;
            [self.navigationController pushViewController:controller animated:YES];
        }
    } else {
        CXFriendInviteModel *friendmodel = [self.dataArrays objectAtIndex:indexPath.row];
        EMConversationModel *model = [EMConversationHelper modelFromContact:friendmodel.user_id];
        if (model) {
            EMChatViewController *controller = [[EMChatViewController alloc] initWithCoversationModel:model];
            controller.friendModel = friendmodel;
            [self.navigationController pushViewController:controller animated:YES];
        }
    }
}

#pragma mark - EMChatManagerDelegate

- (void)messagesDidRecall:(NSArray *)aMessages {
    [self getConversationsData];
}

- (void)conversationListDidUpdate:(NSArray *)aConversationList
{
    [self getConversationsData];
}

- (void)messagesDidReceive:(NSArray *)aMessages
{
    [self performSelector:@selector(_reSortedConversationModelsAndReloadView) withObject:nil afterDelay:0.8];
}
#pragma mark - Private
- (NSString *)_latestMessageTitleForConversationModel:(EMConversation *)conversationModel {
    NSString *latestMessageTitle = @"";
    EMMessage *lastMessage = [conversationModel latestMessage];
    if (lastMessage) {
        EMMessageBody *messageBody = lastMessage.body;
        switch (messageBody.type) {
            case EMMessageBodyTypeImage:{
                latestMessageTitle = @"[图片]";
            } break;
            case EMMessageBodyTypeText:{
                NSString *str = [EMEmojiHelper convertEmoji:((EMTextMessageBody *)messageBody).text];
                latestMessageTitle = str;
            } break;
            case EMMessageBodyTypeVoice:{
                latestMessageTitle = @"[音频]";
            } break;
            case EMMessageBodyTypeLocation: {
                latestMessageTitle = @"[位置]";
            } break;
            case EMMessageBodyTypeVideo: {
                latestMessageTitle = @"[视频]";
            } break;
            case EMMessageBodyTypeFile: {
                latestMessageTitle = @"[文件]";
            } break;
            default: {
            } break;
        }
    }
    return latestMessageTitle;
}

- (NSString *)_latestMessageTimeForConversationModel:(EMConversation *)conversationModel
{
    NSString *latestMessageTime = @"";
    EMMessage *lastMessage = [conversationModel latestMessage];;
    if (lastMessage) {
        double timeInterval = lastMessage.timestamp ;
        if(timeInterval > 140000000000) {
            timeInterval = timeInterval / 1000;
        }
        latestMessageTime = [EMDateHelper formattedTimeFromTimeInterval:timeInterval];
//        NSDateFormatter* formatter = [[NSDateFormatter alloc]init];
//        [formatter setDateFormat:@"YYYY-MM-dd"];
//        latestMessageTime = [formatter stringFromDate:[NSDate dateWithTimeIntervalSince1970:timeInterval]];
    }
    return latestMessageTime;
}

@end
