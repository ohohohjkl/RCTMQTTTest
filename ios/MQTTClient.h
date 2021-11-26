//
//  MQTTClient.h
//  testNativeModule
//
//  Created by Tu Nguyen on 12/11/2021.
//

#import <Foundation/Foundation.h>
#import <MQTTClient/MQTTClient.h>
#import <MQTTClient/MQTTSessionManager.h>
#import <React/RCTEventEmitter.h>


@interface MQTTClient : NSObject<MQTTSessionManagerDelegate>

@property (strong, nonatomic) MQTTSessionManager *manager;
@property (nonatomic, strong) NSDictionary *defaultOptions;
@property (nonatomic, retain) NSMutableDictionary *options;
@property (nonatomic, strong) NSString *clientRef;
@property (nonatomic, strong) RCTEventEmitter * emitter;

- (MQTTClient*) createMQTTClient:(RCTEventEmitter *) emitter
                  options:(NSDictionary *) options
                clientRef:(NSString *) clientRef;
- (void) connect;
- (void) disconnect;
- (BOOL) isConnected;
- (BOOL) isSubscribed:(NSString *) topic;
- (void) subscribe:(NSString *)topic qos:(NSNumber *)qos;
- (void) unsubscribe:(NSString *)topic;
- (void) publish:(NSString *) topic data:(NSData *)data qos:(NSNumber *)qos retain:(BOOL) retain;
@end
