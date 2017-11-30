package jacksonmeyer.com.archat.Services;

import jacksonmeyer.com.archat.Constants;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by jacksonmeyer on 11/23/17.
 */

public class TranslationService {
    public static void getTranslatedTextForCurrentUser(String message, String target, String format, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();


        Request request = new Request.Builder()
                .url(Constants.GOOGLE_TRANSLATE_API_ADDRESS +
                        Constants.GOOGLE_TRANSLATE_QUERY + "=" + message + "&" +
                        Constants.GOOGLE_TRANSLATE_TARGET + "=" + target + "&" +
                        Constants.GOOGLE_TRANSLATE_FORMAT + "=" + format + "&" +
                        Constants.GOOGLE_TRANSLATE_API_KEY_KEYNAME + "=" +
                        Constants.GOOGLE_TRANSLATE_API_KEY)
                .get()
                .build();


        okhttp3.Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void getTranslatedTextForOtherUser(String message, String target, String format, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();


        Request request = new Request.Builder()
                .url(Constants.GOOGLE_TRANSLATE_API_ADDRESS +
                        Constants.GOOGLE_TRANSLATE_QUERY + "=" + message + "&" +
                        Constants.GOOGLE_TRANSLATE_TARGET + "=" + target + "&" +
                        Constants.GOOGLE_TRANSLATE_FORMAT + "=" + format + "&" +
                        Constants.GOOGLE_TRANSLATE_API_KEY_KEYNAME + "=" +
                        Constants.GOOGLE_TRANSLATE_API_KEY)
                .get()
                .build();


        okhttp3.Call call = client.newCall(request);
        call.enqueue(callback);
    }
}

