//
//  CXIPAPurchaseManager.m
//  iOS_Purchase
//  Created by zhanfeng on 2017/6/6.
//  Copyright © 2017年 zhanfeng. All rights reserved.

#import "CXIPAPurchaseManager.h"
#import "SandBoxHelper.h"
#import <StoreKit/StoreKit.h>
#import <StoreKit/SKPaymentTransaction.h>

static NSString * const receiptKey = @"receipt_key";

dispatch_queue_t iap_queue(){
    static dispatch_queue_t as_iap_queue;
    static dispatch_once_t onceToken_iap_queue;
    dispatch_once(&onceToken_iap_queue, ^{
        as_iap_queue = dispatch_queue_create("com.iap.queue", DISPATCH_QUEUE_CONCURRENT);
    });
    
    return as_iap_queue;
    
}

@interface CXIPAPurchaseManager()<SKPaymentTransactionObserver,
SKProductsRequestDelegate>
{
    SKProductsRequest *request;
}

//产品ID
@property (nonnull,copy)NSString * profductId;

@end

static CXIPAPurchaseManager * manager = nil;

@implementation CXIPAPurchaseManager
#pragma mark -- 单例方法
+ (instancetype)manager{
    
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        
        if (!manager) {
            manager = [[CXIPAPurchaseManager alloc] init];
        }
        
    });
    
    return manager;
}

#pragma mark -- 添加内购监听者
-(void)startManager{
    
    dispatch_sync(iap_queue(), ^{
        
        [[SKPaymentQueue defaultQueue] addTransactionObserver:manager];

    });

}

#pragma mark -- 移除内购监听者
- (void)stopManager{
    
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        [[SKPaymentQueue defaultQueue] removeTransactionObserver:self];
        
    });
    
}

#pragma mark -- 发起购买的方法
- (void)inAppPurchaseWithProductID:(NSString *)productID iapResult:(InAppPurchaseResult)iapResult{
    [self startManager];
    
    [MBProgressHUD showHUD];
    [self removeAllUncompleteTransactionsBeforeNewPurchase];
    
    self.iapResultBlock = iapResult;
        
    self.profductId = productID;

    if ([SKPaymentQueue canMakePayments]) {
        
        [self requestProductInfo:self.profductId];
        
    } else {
        [MBProgressHUD hideHUD];
    }
    
}

#pragma mark -- 结束上次未完成的交易
- (void)removeAllUncompleteTransactionsBeforeNewPurchase {
    
    NSArray* transactions = [SKPaymentQueue defaultQueue].transactions;
    
    if (transactions.count >= 1) {

        for (NSInteger count = transactions.count; count > 0; count--) {
            
            SKPaymentTransaction* transaction = [transactions objectAtIndex:count-1];
            
            if (transaction.transactionState == SKPaymentTransactionStatePurchased||transaction.transactionState == SKPaymentTransactionStateRestored) {
                
                [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
            }
        }
        
    } else {
        
         NSLog(@"没有历史未消耗订单");
    }

}


#pragma mark -- 发起购买请求
- (void)requestProductInfo:(NSString *)productID{
    
    NSArray * productArray = [[NSArray alloc]initWithObjects:productID,nil];
    
    NSSet * IDSet = [NSSet setWithArray:productArray];
    
    request = [[SKProductsRequest alloc]initWithProductIdentifiers:IDSet];
    
    request.delegate = self;
    
    [request start];
    
}

#pragma mark -- SKProductsRequestDelegate 查询成功后的回调
- (void)productsRequest:(SKProductsRequest *)request didReceiveResponse:(SKProductsResponse *)response
{
    NSArray *myProduct = response.products;
    
    if (myProduct.count == 0) {
        [MBProgressHUD hideHUD];
        if (self.iapResultBlock) {
            self.iapResultBlock(NO, nil, @"无法获取商品信息，购买失败");
        }
        
        return;
    }
    
    SKProduct * product = nil;
    
    for(SKProduct * pro in myProduct){
        
        if ([pro.productIdentifier isEqualToString:self.profductId]) {
            
            product = pro;
            
            break;
        }
    }
    
    if (product) {
        SKMutablePayment * payment = [SKMutablePayment paymentWithProduct:product];
        //使用苹果提供的属性,将平台订单号复制给这个属性作为透传参数
        if (self.order_sn.length > 0) {
            payment.applicationUsername = self.order_sn;
        }
        
        [[SKPaymentQueue defaultQueue] addPayment:payment];
        
    } else {
        [MBProgressHUD hideHUD];
        NSLog(@"没有此商品信息");
    }
}

//查询失败后的回调
- (void)request:(SKRequest *)request didFailWithError:(NSError *)error {
    [MBProgressHUD hideHUD];
    if (self.iapResultBlock) {
        self.iapResultBlock(NO, nil, [error localizedDescription]);
    }
}

//如果没有设置监听购买结果将直接跳至反馈结束；
- (void)requestDidFinish:(SKRequest *)request{
    [MBProgressHUD hideHUD];
}

#pragma mark -- 监听结果
- (void)paymentQueue:(SKPaymentQueue *)queue updatedTransactions:(NSArray<SKPaymentTransaction *> *)transactions{
    [MBProgressHUD hideHUD];
    //当用户购买的操作有结果时，就会触发下面的回调函数，
    for (SKPaymentTransaction * transaction in transactions) {
        
        switch (transaction.transactionState) {
                
            case SKPaymentTransactionStatePurchased:{
                
                [MBProgressHUD showHUD];
                [self completeTransaction:transaction];
                
            }
                break;
                
            case SKPaymentTransactionStateFailed:{
                
                [self failedTransaction:transaction];
                
            }
                break;
                
            case SKPaymentTransactionStateRestored:{//已经购买过该商品
                
                [self restoreTransaction:transaction];
                
            }
                break;
                
            case SKPaymentTransactionStatePurchasing:{
                
                NSLog(@"正在购买中...");
                
            }
                break;
                
            case SKPaymentTransactionStateDeferred:{
                
                NSLog(@"最终状态未确定");
                
            }break;
                
            default:
                break;
        }
    }
}


- (BOOL)paymentQueue:(SKPaymentQueue *)queue shouldAddStorePayment:(SKPayment *)payment forProduct:(SKProduct *)product{
    
    
//    UIAlertController *  alert  = [UIAlertController alertControllerWithTitle:@"提示" message:@"你有一笔来自appStore的优惠订单未使用,请点击使用,以防失效." preferredStyle:UIAlertControllerStyleAlert];
//    [alert addAction:[UIAlertAction actionWithTitle:@"使用" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//
//        //这里去获取游戏的一些参数
//
//    }]];
    
    return YES;
}

//完成交易
#pragma mark -- 交易完成的回调
- (void)completeTransaction:(SKPaymentTransaction *)transaction {
    if (self.order_sn.length <= 0) {
        self.order_sn = transaction.transactionIdentifier;
    }
//    // 根据存储凭证存储Order
//    [self saveOrderByInAppPurchase:transaction];
//    // 获取购买凭证并且发送服务器验证
//    [self getAndSaveReceipt:transaction];
    
    NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithDictionary:[[NSUserDefaults standardUserDefaults] objectForKey:@"CXIPAPurchaseManager_NSUserDefaults"]];
    if (![dict.allKeys containsObject:transaction.transactionIdentifier]) {
        NSDictionary *item = @{
            @"user_id":self.userid ? self.userid : @"",
            @"order_sn":self.order_sn,
            @"purchaseType": @(self.purchaseType),
        };
        [dict setValue:item forKey:transaction.transactionIdentifier];
        [[NSUserDefaults standardUserDefaults] setObject:dict forKey:@"CXIPAPurchaseManager_NSUserDefaults"];
    }
    
    //获取交易凭证
    NSURL * receiptUrl = [[NSBundle mainBundle] appStoreReceiptURL];
    NSData * receiptData = [NSData dataWithContentsOfURL:receiptUrl];
    NSString * base64String = [receiptData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
    
    [self sendAppStoreRequestToPhpWithReceipt:base64String userId:self.userid paltFormOrder:self.order_sn trans:transaction];
}

#pragma mark -- 去服务器验证购买
- (void)sendAppStoreRequestToPhpWithReceipt:(NSString *)receipt userId:(NSString *)userId paltFormOrder:(NSString * )order trans:(SKPaymentTransaction *)transaction {
    NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithDictionary:[[NSUserDefaults standardUserDefaults] objectForKey:@"CXIPAPurchaseManager_NSUserDefaults"]];
    NSDictionary *item = [dict objectForKey:transaction.transactionIdentifier];
    
    if ([item[@"purchaseType"] integerValue] == LiveBroadcast) { // 直播
        NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
        NSDictionary *param = @{
            @"signature":signature,
            @"receipt":receipt,
            @"user_id":item[@"user_id"],
            @"transaction_id":transaction.transactionIdentifier,
        };
        kWeakSelf
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/ApplePay/getreceiptdata" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
            [MBProgressHUD hideHUD];
            if (!error) {
                //结束交易方法
                [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
                [dict removeObjectForKey:transaction.transactionIdentifier];
                [[NSUserDefaults standardUserDefaults] setObject:dict forKey:@"CXIPAPurchaseManager_NSUserDefaults"];
                if (weakSelf.iapResultBlock) {
                    weakSelf.iapResultBlock(YES, param, nil);
                }
            }
        }];
    } else {
        [MBProgressHUD hideHUD];
        NSString *receiptStr = [receipt stringByAddingPercentEncodingWithAllowedCharacters:[[NSCharacterSet characterSetWithCharactersInString:@"+"] invertedSet]];
        NSDictionary *param = @{
            @"purchaseData":receiptStr,
            @"orderNo": item[@"order_sn"],
        };
        
        if ([CXOCJSBrigeManager manager].paySuccessMethod) {
            if (self.iapResultBlock) {
                self.iapResultBlock(YES, param, nil);
            }
        } else {
//            [AppController applePayCheckOrder:[param jsonStringEncoded]];
            [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
        }
    }
}


#pragma mark -- 处理交易失败回调
- (void)failedTransaction:(SKPaymentTransaction *)transaction{
    [MBProgressHUD hideHUD];
    NSString * error = nil;

    if(transaction.error.code != SKErrorPaymentCancelled) {
        error = [NSString stringWithFormat:@"%@",transaction.error.description];
        
    } else {
        error = [NSString stringWithFormat:@"%@",transaction.error.description];
    }
    
    if (self.iapResultBlock) {
        self.iapResultBlock(NO, nil, error);
    }
    
    [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
}

- (void)restoreTransaction:(SKPaymentTransaction *)transaction{
    [MBProgressHUD hideHUD];
    [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
}

#pragma mark -- 存储成功订单
-(void)SaveIapSuccessReceiptDataWithReceipt:(NSString *)receipt Order:(NSString *)order UserId:(NSString *)userId transId:(NSString *)transactionId{
    
    NSMutableDictionary * mdic = [[NSMutableDictionary alloc]init];
    [mdic setValue:[self getCurrentZoneTime] forKey:@"time"];
    [mdic setValue:order forKey:@"order"];
    [mdic setValue:userId forKey:@"userid"];
    [mdic setValue:receipt forKey:receiptKey];
    NSString * successReceiptPath = [NSString stringWithFormat:@"%@/%@.plist", [SandBoxHelper SuccessIapPath], transactionId];
    //存储购买成功的凭证
    [self insertReceiptWithReceiptByReceipt:receipt withDic:mdic  inReceiptPath:successReceiptPath];
}

#pragma mark -- 写入购买成功的凭证
-(void)insertReceiptWithReceiptByReceipt:(NSString *)receipt withDic:(NSDictionary *)dic inReceiptPath:(NSString *)receiptfilePath{
    
    BOOL isContain = NO;
    
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSError * error;
    NSArray * cacheFileNameArray = [fileManager contentsOfDirectoryAtPath:[SandBoxHelper SuccessIapPath] error:&error];
    
    if (cacheFileNameArray.count == 0) {
        
      BOOL ifWriteSuccess = [dic writeToFile:receiptfilePath atomically:YES];
        
        if (ifWriteSuccess) {
            
            NSLog(@"写入购买凭据成功");
        }
        
    } else {
       
        if (error == nil) {
         
            for (NSString * name in cacheFileNameArray) {

                NSString * filePath = [NSString stringWithFormat:@"%@/%@", [SandBoxHelper SuccessIapPath], name];
                NSMutableDictionary *localdic = [NSMutableDictionary dictionaryWithContentsOfFile:filePath];
                
                if ([localdic.allValues containsObject:receipt]) {
                    
                    isContain = YES;
                    
                }else{
                    
                    continue;
                }
            }
            
        } else {
            
            NSLog(@"读取本文存储凭据失败");
        }
        
    }
    
    if (isContain == NO) {
        
    BOOL results = [dic writeToFile:receiptfilePath atomically:YES];
        
    if (results) {
        
        NSLog(@"写入凭证成功");
        
    }else{
        
        NSLog(@"写入凭证失败");
    }
        
    }else{
        
        NSLog(@"已经存在凭证请勿重复写入");
    }
    
}

#pragma mark -- 获取系统时间的方法
- (NSString *)getCurrentZoneTime {
    
    NSDate * date = [NSDate date];
    NSDateFormatter*formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSString*dateTime = [formatter stringFromDate:date];
    return dateTime;
    
}

#pragma mark -- 根据购买凭证来移除本地凭证的方法
-(void)successConsumptionOfGoodsWithTransId:(NSString * )transcationId{
    
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSError * error;
    if ([fileManager fileExistsAtPath:[SandBoxHelper iapReceiptPath]]) {
        
        NSArray * cacheFileNameArray = [fileManager contentsOfDirectoryAtPath:[SandBoxHelper iapReceiptPath] error:&error];
        
        if (error == nil) {
            
            for (NSString * name in cacheFileNameArray) {
                
                NSString * filePath = [NSString stringWithFormat:@"%@/%@", [SandBoxHelper iapReceiptPath], name];
                
                [self removeReceiptWithPlistPath:filePath BytransId:transcationId];
            }
        }
    }
}

#pragma mark -- 根据订单号来删除存储的凭证
-(void)removeReceiptWithPlistPath:(NSString *)plistPath BytransId:(NSString *)transactionId{
    
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSError * error;
    NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithContentsOfFile:plistPath];
    NSString * localTransId = [dic objectForKey:@"unlock_transactionId"];
    //通过凭证进行对比
    if ([transactionId isEqualToString:localTransId]) {
      
        BOOL ifRemove = [fileManager removeItemAtPath:plistPath error:&error];
        
        if (ifRemove) {
            
            NSLog(@"成功订单移除成功");
            
        }else{
            
            NSLog(@"成功订单移除失败");
        }
        
    }else{
        
        NSLog(@"本地无与之匹配的订单");
    }
}


//#pragma mark -- 存储订单,防止走漏单流程是获取不到Order 且苹果返回order为nil
//- (void)saveOrderByInAppPurchase:(SKPaymentTransaction *)transaction{
//
//    NSMutableDictionary * dic = [[NSMutableDictionary alloc]init];
//    NSString * order = self.order_sn;
//    NSString *savedPath = [NSString stringWithFormat:@"%@/%@.plist", [SandBoxHelper tempOrderPath], order];
//    [dic setValue:order forKey:transaction.transactionIdentifier];
//    BOOL ifWriteSuccess = [dic writeToFile:savedPath atomically:YES];
//
//    if (ifWriteSuccess) {
//
//        NSLog(@"根据事务id存储订单号成功!订单号为:%@  事务id为:%@",order,transaction.transactionIdentifier);
//    }
//}

//#pragma mark -- 根据凭证存储的列表里获取Order
//- (NSString *)getOrderWithTransactionId:(NSString *)transId{
//
//    NSString * order;
//    NSFileManager *fileManager = [NSFileManager defaultManager];
//    NSError * error;
//    NSArray * cacheFileNameArray = [fileManager contentsOfDirectoryAtPath:[SandBoxHelper tempOrderPath] error:&error];
//
//    for (NSString * name in cacheFileNameArray) {
//
//       NSString * filePath = [NSString stringWithFormat:@"%@/%@", [SandBoxHelper tempOrderPath], name];
//        NSMutableDictionary *localdic = [NSMutableDictionary dictionaryWithContentsOfFile:filePath];
//        if ([localdic valueForKey:transId]) {
//            order = [localdic valueForKey:transId];
//
//        } else {
//            continue;
//        }
//    }
//
//    if ([order length]>0) {
//
//      return order;
//
//    } else {
//
//      return @"";
//
//    }
//}
//#pragma mark -- 获取购买凭证
//-(void)getAndSaveReceipt:(SKPaymentTransaction *)transaction{
//
//    //获取交易凭证
//    NSURL * receiptUrl = [[NSBundle mainBundle] appStoreReceiptURL];
//    NSData * receiptData = [NSData dataWithContentsOfURL:receiptUrl];
//    NSString * base64String = [receiptData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
//    //初始化字典
//    NSMutableDictionary * dic = [[NSMutableDictionary alloc]init];
//    NSString * order = transaction.payment.applicationUsername;
//    NSString * userId = self.userid;
//
//    if (userId == nil||[userId length] == 0) {
//
//        userId = @"走漏单流程未传入userId";
//    }
//
//    if (order == nil||[order length] == 0) {
//
//        if (self.order_sn) {
//            order = self.order_sn;
//        } else {
//            order = [self getOrderWithTransactionId:transaction.transactionIdentifier];
//        }
//    }
//
//    //如果这时候
//    [dic setValue: base64String forKey:receiptKey];
//    [dic setValue:transaction.transactionIdentifier forKey:@"unlock_transactionId"];
//    [dic setValue: order forKey:@"order"];
//    [dic setValue:[self getCurrentZoneTime] forKey:@"time"];
//    [dic setValue: userId forKey:@"user_id"];
//
//    NSString *savedPath = [NSString stringWithFormat:@"%@/%@.plist", [SandBoxHelper iapReceiptPath], transaction.transactionIdentifier];
//
//    //这个存储成功与否其实无关紧要
//    BOOL ifWriteSuccess = [dic writeToFile:savedPath atomically:YES];
//
//    if (ifWriteSuccess){
//        NSLog(@"购买凭据存储成功!");
//    } else {
//        NSLog(@"购买凭据存储失败");
//    }
//
//    [self sendAppStoreRequestToPhpWithReceipt:base64String userId:userId paltFormOrder:order trans:transaction];
//
//}

@end
