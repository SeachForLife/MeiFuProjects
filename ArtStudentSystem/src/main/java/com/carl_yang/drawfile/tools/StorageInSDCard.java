package com.carl_yang.drawfile.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class StorageInSDCard {
	
	public static boolean IsExternalStorageAvailableAndWriteable() {
		boolean externalStorageAvailable = false;
		boolean externalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		
		if(Environment.MEDIA_MOUNTED.equals(state)) {
			externalStorageAvailable = externalStorageWriteable = true;
		}
		else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			externalStorageAvailable = true;
			externalStorageWriteable = false;
		}
		else {
			externalStorageAvailable = externalStorageWriteable = false;
		}
		return externalStorageAvailable && externalStorageWriteable;
	}
	
	public static void saveBitmapInExternalStorage(Bitmap bitmap,Context context) {
		try {
			if(IsExternalStorageAvailableAndWriteable()) {
				File file = new File(Environment.getExternalStorageDirectory()+"/test.jpg");
//				if (!file.exists()) {
//					return;
//				}
				FileOutputStream fOut = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
			}
			else {
			}
		}
		catch (IOException ioe){
			Log.e("StorageSD","异常:"+ioe);
			ioe.printStackTrace();
		}
	}
	
	public static ArrayList<String> getBitmapsPathFromExternalStorage() {
		ArrayList<String> fileList = new ArrayList<String>();
		if(IsExternalStorageAvailableAndWriteable()) {
			File extStorage = new File(Environment.getExternalStorageDirectory().getPath() +"/drawpics");
			if (!extStorage.exists()) {
				extStorage.mkdirs();				
			}				
			File[] files = extStorage.listFiles();
			if(files != null) {
				for(int i = 1 ;i < files.length ;i++) {
					for(int j = i ;j > 0 ;j--) {
						if(files[j].lastModified() > files[j - 1].lastModified()) {
							File tempfile = files[j];
							files[j] = files[j - 1];
							files[j - 1] = tempfile;
						}
						else {
							break;
						}
					}
				}
				fileList = null;
				fileList = new ArrayList<String>(files.length);
			}
			for(int i=0 ;i<files.length ;i++)
			{
				if(files[i].isFile())
				{					
					String filename = files[i].getName();
					if(filename.endsWith(".jpg")||filename.endsWith(".png")||filename.endsWith(".bmp"))
					{ 
						fileList.add(files[i].getAbsolutePath());
					}						                     
				}               
			}
		}
		else {
		}
		return fileList;
	}	
}
