/*
package com.example.documentspicker;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;

import droidninja.filepicker.FilePickerConst;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class DocumentsPickerDelegate implements PluginRegistry.ActivityResultListener {

    private Activity activity;
    private MethodChannel.Result result;


    public DocumentsPickerDelegate(
            final Activity activity,
            final MethodChannel.Result result
    ) {
        this.activity = activity;
        this.result = result;
    }

    private ArrayList<String> docPaths = new ArrayList<>();

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FilePickerConst.REQUEST_CODE_DOC) {
            if (data != null)
                docPaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS);

            this.result.success(docPaths);

            return true;
        }

        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            if (data != null)
                docPaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);

            this.result.success(docPaths);

            return true;
        }

        return false;
    }
}
}
*/
