package com.example.documentspicker;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class DocumentsPickerPlugin implements MethodChannel.MethodCallHandler, PluginRegistry.ActivityResultListener, FlutterPlugin, ActivityAware {

    private FlutterPluginBinding pluginBinding;
    private ActivityPluginBinding activityBinding;
    private MethodChannel channel;
    private Activity activity;
    private Result result;
    private ArrayList<String> docPaths = new ArrayList<>();
    private static final int REQUEST_READ_PERMISSION = 786;

    private DocumentsPickerPlugin(Activity activity) {
        this.activity = activity;
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        if (registrar.activity() == null) {
            // If a background flutter view tries to register the plugin, there will be no activity from the registrar,
            // we stop the registering process immediately because the ImagePicker requires an activity.
            return;
        }
        Activity activity = registrar.activity();
        Application application = null;
        if (registrar.context() != null) {
            application = (Application) (registrar.context().getApplicationContext());
        }
        DocumentsPickerPlugin plugin = new DocumentsPickerPlugin(activity);
        plugin.setup(registrar.messenger(), activity, registrar, null);
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

    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("pickDocuments")) {

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
            } else {
                int maxCount = call.argument("maxCount");
                this.result = result;
                FilePickerBuilder.getInstance().setMaxCount(maxCount)
                        .setActivityTheme(R.style.LibAppTheme)
                        .pickFile(activity);
            }
        } else if(call.method.equals("pickImages")){
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
            } else {
                int maxCount = call.argument("maxCount");
                this.result = result;
                FilePickerBuilder.getInstance().setMaxCount(maxCount)
                        .setActivityTheme(R.style.LibAppTheme)
                        .enableCameraSupport(false)
                        .pickPhoto(activity);
            }
        } else {
            result.notImplemented();
        }
    }

    private void setup(
            final BinaryMessenger messenger,
            final Activity activity,
            final PluginRegistry.Registrar registrar,
            final ActivityPluginBinding activityBinding) {
        this.activity = activity;
        DocumentsPickerPlugin plugin = new DocumentsPickerPlugin(registrar.activity());
        channel = new MethodChannel(messenger, "documents_picker");
        channel.setMethodCallHandler(this);
        if (registrar != null) {
            // V1 embedding setup for activity listeners.
            registrar.addActivityResultListener(plugin);
        } else {
            // V2 embedding setup for activity listeners.
            activityBinding.addActivityResultListener(plugin);
        }
    }


    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FilePickerConst.REQUEST_CODE_DOC){
            if (intent != null)
                docPaths = intent.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS);

            this.result.success(docPaths);

            return true;
        }

        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO){
            if (intent != null)
                docPaths = intent.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);

            this.result.success(docPaths);

            return true;
        }

        return false;
    }
}


/**
 * DocumentsPickerPlugin
 * <p>
 * Plugin registration.
 */
/*

public class DocumentsPickerPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener {
  */
/**
 * Plugin registration.
 *//*


  private Activity activity;
  private Result result;

  private DocumentsPickerPlugin(Activity activity) {
    this.activity = activity;
  }

  private ArrayList<String> docPaths = new ArrayList<>();

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "documents_picker");
    DocumentsPickerPlugin plugin = new DocumentsPickerPlugin(registrar.activity());
    channel.setMethodCallHandler(plugin);
    registrar.addActivityResultListener(plugin);
  }

  private static final int REQUEST_READ_PERMISSION = 786;


  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("pickDocuments")) {

      if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
      } else {
        int maxCount = call.argument("maxCount");
        this.result = result;
        FilePickerBuilder.getInstance().setMaxCount(maxCount)
                .setActivityTheme(R.style.LibAppTheme)
                .pickFile(activity);
      }
    } else if(call.method.equals("pickImages")){
      if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
      } else {
        int maxCount = call.argument("maxCount");
        this.result = result;
        FilePickerBuilder.getInstance().setMaxCount(maxCount)
                .setActivityTheme(R.style.LibAppTheme)
                .enableCameraSupport(false)
                .pickPhoto(activity);
      }
    } else {
      result.notImplemented();
    }
  }

  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {

    if (requestCode == FilePickerConst.REQUEST_CODE_DOC){
      if (intent != null)
        docPaths = intent.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS);
    
      this.result.success(docPaths);

      return true;
    }

    if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO){
      if (intent != null)
        docPaths = intent.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);

      this.result.success(docPaths);

      return true;
    }

    return false;
  }
}
*/
