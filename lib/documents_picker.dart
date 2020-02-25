import 'dart:async';

import 'package:flutter/services.dart';

class DocumentsPicker {
  static const MethodChannel _channel = const MethodChannel('documents_picker');

  static Future<List<String>> pickDocuments({int maxCount}) async {
    final List<dynamic> docsPaths = await _channel
        .invokeMethod('pickDocuments', {"maxCount": maxCount ?? 1});
    if (docsPaths != null && docsPaths.isEmpty) return [];
    return docsPaths?.cast<String>();
  }

  static Future<List<String>> pickImages({int maxCount}) async {
    final List<dynamic> imagePaths = await _channel
        .invokeListMethod('pickImages', {"maxCount": maxCount ?? 1});
    if (imagePaths != null && imagePaths.isEmpty) return [];
    return imagePaths?.cast<String>();
  }
}
