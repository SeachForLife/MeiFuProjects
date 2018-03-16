package com.carl_yang.common;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by carl_yang on 2017/3/27.
 */

public class HttpService {

//    private static InputStream signedForOrderNo(String filepath, String storeName, String signedType, String signedValue) {
//        try {
//            HttpClient client = buildHttpClient();
//            //"http://172.20.1.6:8080/Milk/AppServlet";
//            String urlImage=url.substring(0,url.lastIndexOf("/"))+"/AppPicServlet";
////            System.out.println("URL_--------:"+urlImage);
//            HttpPost post = new HttpPost(urlImage);
//            MultipartEntity entity=new MultipartEntity();
//            entity.addPart("method",new StringBody("signedForOrder", Charset.forName("utf-8")));
//            entity.addPart("storeName",new StringBody(storeName, Charset.forName("utf-8")));
//            entity.addPart("signedType",new StringBody(signedType,Charset.forName("utf-8")));
//            if(filepath.equals("")){
//            }else {
//                entity.addPart("signedValue",new StringBody(signedValue,Charset.forName("utf-8")));
//                File file = new File(filepath);
//                FileBody fileBody = new FileBody(file, "image/jpeg");
//                entity.addPart("image", fileBody);
//            }
//            post.setEntity(entity);
//            HttpResponse response = client.execute(post);
//            int code = response.getStatusLine().getStatusCode();
//            if (code == 200) {
//                InputStream is = response.getEntity().getContent();
//                return is;
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }

}
