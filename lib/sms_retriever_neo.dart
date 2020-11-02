import 'dart:async';
import 'package:flutter/services.dart';
import 'package:sms_retriever_neo/schemas/typedefs.dart';

class SmsRetriever {
  static const MethodChannel _channel = const MethodChannel('sms_retriever');

  static _handleCalls({
    OnResult<String> onSmsReceived,
    OnCall onTimeout,
  }) {
    _channel.setMethodCallHandler((call) {
      switch (call.method) {
        case "sms_received":
          if (onSmsReceived != null) {
            onSmsReceived(call.arguments);
          }
          break;
        case "sms_timeout":
          if (onTimeout != null) {
            onTimeout();
          }
          break;
        default:
          return;
      }
      return null;
    });
  }

  static Future<void> startListening({
    OnResult<String> onSmsReceived,
    OnCall onTimeout,
  }) async {
    try {
      await _channel.invokeMethod('startListening');
      _handleCalls(
        onSmsReceived: onSmsReceived,
        onTimeout: onTimeout,
      );
    } catch (_) {}
  }

  static Future<void> stopListening() async {
    try {
      await _channel.invokeMethod('stopListening');
    } catch (_) {}
  }

  static Future<String> getAppSignature() async {
    try {
      return await _channel.invokeMethod('getAppSignature');
    } on PlatformException catch (e) {
      throw e.message;
    } catch (e) {
      throw e;
    }
  }
}
