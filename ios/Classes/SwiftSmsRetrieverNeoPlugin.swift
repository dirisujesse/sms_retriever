import Flutter
import UIKit

public class SwiftSmsRetrieverNeoPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "sms_retriever_neo", binaryMessenger: registrar.messenger())
    let instance = SwiftSmsRetrieverNeoPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
