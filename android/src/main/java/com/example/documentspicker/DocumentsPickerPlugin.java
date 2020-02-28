package com.example.documentspicker;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class DocumentsPickerPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
    private DocumentsPickerDelegate delegate;
    private FlutterPluginBinding pluginBinding;
    private ActivityPluginBinding activityBinding;
    private MethodChannel channel;

    public static void registerWith(PluginRegistry.Registrar registrar) {
        if (registrar.activity() == null) {
            return;
        }

        DocumentsPickerPlugin plugin = new DocumentsPickerPlugin();
        plugin.setup(registrar.messenger(), registrar, null);
    }


    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        pluginBinding = binding;
    }

    @Override
    public void onDetachedFromEngine(FlutterPluginBinding binding) {
        pluginBinding = null;
    }

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        activityBinding = binding;
        setup(pluginBinding.getBinaryMessenger(), null, activityBinding);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivity() {
        DocumentsPickerDelegate delegate = this.delegate;
        if (delegate != null) {
            activityBinding.removeActivityResultListener(delegate);
        }

        this.delegate = (DocumentsPickerDelegate) null;
        this.channel = (MethodChannel) null;
        this.activityBinding = (ActivityPluginBinding) null;
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        if (call.method.equals("pickDocuments")) {
            delegate.pickFromFileManager(call, result);
        } else if(call.method.equals("pickImages")){
            delegate.pickFromGallery(call, result);
        } else {
            result.notImplemented();
        }
    }

    private void setup(BinaryMessenger messenger,
                       PluginRegistry.Registrar registrar,
                       ActivityPluginBinding activityPluginBinding
    ) {
        DocumentsPickerDelegate delegate = null;

        if (registrar != null) {
            delegate = new DocumentsPickerDelegate(registrar.activity());
            registrar.addActivityResultListener(delegate);
        } else if (activityBinding != null) {
            delegate = new DocumentsPickerDelegate(activityBinding.getActivity());
            activityBinding.addActivityResultListener(delegate);
        }

        this.delegate = delegate;
        channel = new MethodChannel(messenger, "documents_picker");
        channel.setMethodCallHandler(this);
    }
}
