#import <React/RCTLog.h>
#import <React/RCTBridgeModule.h>
#import "AwesomeTesting.h"
#import "MQTTClient.h"

@implementation AwesomeTesting

RCT_EXPORT_MODULE()


+ (BOOL) requiresMainQueueSetup{
    return NO;
}


- (NSArray<NSString *> *)supportedEvents {
    return @[ @"mqtt_events" ];
}


- (instancetype)init
{
    if ((self = [super init])) {
        _clients = [[NSMutableDictionary alloc] init];
    }
    return self;
     
}

RCT_EXPORT_METHOD(createClient:(NSDictionary *) options
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    
    RCTLogInfo(@"CREATE CLIENT with option %@", options);
    NSString *clientRef = [[NSProcessInfo processInfo] globallyUniqueString];
    
    MQTTClient *client = [[MQTTClient allocWithZone: nil] createMQTTClient: self options: options clientRef: clientRef];
    
    [[self clients] setObject:client forKey:clientRef];
    resolve(clientRef);
    
}

RCT_EXPORT_METHOD(connect:(nonnull NSString *) clientRef) {
  RCTLogInfo(@"CONNECT WITH CLIENT %@", clientRef);
    [[[self clients] objectForKey:clientRef] connect];
}

RCT_EXPORT_METHOD(disconnect:(nonnull NSString *) clientRef) {
  RCTLogInfo(@"DISCONNECT WITH CLIENT %@", clientRef);
    [[[self clients] objectForKey:clientRef] disconnect];
}

RCT_EXPORT_METHOD(subscribe:(nonnull NSString *) clientRef topic:(NSString *) topic qos: (nonnull NSNumber *) qos) {
  RCTLogInfo(@"SUBCRIBE WITH CLIENT %@", clientRef);
  [[[self clients] objectForKey:clientRef] subscribe: topic
                                                 qos: qos];
}

RCT_EXPORT_METHOD(unsubscribe:(nonnull NSString *) clientRef topic:(NSString *) topic) {
  RCTLogInfo(@"SUBCRIBE WITH CLIENT %@", clientRef);
  [[[self clients] objectForKey:clientRef] unsubscribe: topic];
}

RCT_EXPORT_METHOD(publish:(nonnull NSString *) clientRef topic: (NSString *) topic data: (NSString *) data qos: (nonnull NSNumber *)qos retainedFlag: (BOOL) retained) {
  RCTLogInfo(@"SUBCRIBE WITH CLIENT %@", clientRef);
  [[[self clients] objectForKey:clientRef] publish: topic
                                              data: [data dataUsingEncoding:NSUTF8StringEncoding]
                                               qos: qos
                                            retain: retained];
}

RCT_EXPORT_METHOD(isConnected:(nonnull NSString *) clientRef) {
  RCTLogInfo(@"CLIENT %@ CONNECTING STATUS", clientRef);
  [[[self clients] objectForKey:clientRef] isConnected];
}

RCT_EXPORT_METHOD(isSubscribed:(nonnull NSString *) clientRef topic:(NSString *) topic) {
  RCTLogInfo(@"CLIENT %@ SUBSCRIBE STATUS", clientRef);
  [[[self clients] objectForKey:clientRef] isSubscribed: topic];
}


@end

