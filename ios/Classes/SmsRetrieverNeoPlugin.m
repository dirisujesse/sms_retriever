#import "SmsRetrieverNeoPlugin.h"
#if __has_include(<sms_retriever_neo/sms_retriever_neo-Swift.h>)
#import <sms_retriever_neo/sms_retriever_neo-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "sms_retriever_neo-Swift.h"
#endif

@implementation SmsRetrieverNeoPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSmsRetrieverNeoPlugin registerWithRegistrar:registrar];
}
@end
