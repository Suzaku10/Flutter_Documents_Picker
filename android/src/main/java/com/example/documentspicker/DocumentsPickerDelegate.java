package com.example.documentspicker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public final class DocumentsPickerDelegate implements PluginRegistry.ActivityResultListener {
    private MethodChannel.Result channelResult;
    private final Activity activity;
    private ArrayList<String> docPaths = new ArrayList<>();
    private static final int REQUEST_READ_PERMISSION = 786;

    public DocumentsPickerDelegate(Activity activity) {
        this.activity = activity;
    }

    public void pickFromFileManager(MethodCall call, MethodChannel.Result result){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        } else {
            int maxCount = call.argument("maxCount");
            this.channelResult = result;
            FilePickerBuilder.getInstance().setMaxCount(maxCount)
                    .setActivityTheme(R.style.LibAppTheme)
                    .pickFile(activity);
        }
    }

    public void pickFromGallery(MethodCall call, MethodChannel.Result result){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        } else {
            int maxCount = call.argument("maxCount");
            this.channelResult = result;
            FilePickerBuilder.getInstance().setMaxCount(maxCount)
                    .setActivityTheme(R.style.LibAppTheme)
                    .enableCameraSupport(false)
                    .pickPhoto(activity);
        }
    }


    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FilePickerConst.REQUEST_CODE_DOC){
            if (intent != null)
                docPaths = intent.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS);

            this.channelResult.success(docPaths);

            return true;
        }

        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO){
            if (intent != null)
                docPaths = intent.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);

            this.channelResult.success(docPaths);

            return true;
        }

        return false;
    }
}
