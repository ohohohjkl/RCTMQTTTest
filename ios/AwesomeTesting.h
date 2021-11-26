#import <Foundation/Foundation.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>

@interface AwesomeTesting : RCTEventEmitter <RCTBridgeModule>
@property NSMutableDictionary *clients;
@end
    