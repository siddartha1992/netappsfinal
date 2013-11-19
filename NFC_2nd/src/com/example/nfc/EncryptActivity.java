package com.example.nfc;

import java.io.File;
import java.net.URISyntaxException;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EncryptActivity extends Activity {

	private Button openFile;
	private TextView output;
	private static final int FILE_SELECT_CODE = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_encrypt);
		this.openFile = (Button)findViewById(R.id.button_openFile);
		this.output = (TextView) findViewById(R.id.textView_output);
		this.openFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showFileChooser(); 
					
			}
		});
	}
	
	private void showFileChooser(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*"); 
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		
		try {
	        startActivityForResult(
	                Intent.createChooser(intent, "Select a File to Upload"),
	                FILE_SELECT_CODE);
	    } catch (android.content.ActivityNotFoundException ex) {
	        // Potentially direct the user to the Market with a Dialog
	        Toast.makeText(this, "Please install a File Manager.", 
	                Toast.LENGTH_SHORT).show();
	    }
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case FILE_SELECT_CODE:
				if(resultCode == RESULT_OK){
					Uri uri = data.getData();
//					output.append(uri.toString());
//					output.append("\n");
					try {
						String path = getPath(this,uri);
						File file = new File(path);
						output.append(file.getName());
						output.append("\n\n");
						output.append(file.toString());
						//output.append(getPath(this,uri));
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public static String getPath(Context context, Uri uri) throws URISyntaxException {
	    if ("content".equalsIgnoreCase(uri.getScheme())) {
	        String[] projection = { "_data" };
	        Cursor cursor = null;

	        try {
	            cursor = context.getContentResolver().query(uri, projection, null, null, null);
	            int column_index = cursor.getColumnIndexOrThrow("_data");
	            if (cursor.moveToFirst()) {
	                return cursor.getString(column_index);
	            }
	        } catch (Exception e) {
	            // Eat it
	        }
	    }
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.encrypt, menu);
		return true;
	}

}
