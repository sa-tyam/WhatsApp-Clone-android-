package application.greyhats.whatsappclone;

import android.app.Application;

import com.parse.Parse;

public class ParseStarter extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("w0j2Y0VcvwIPZTvUWbBHPP6qc2edutPq7XfVcCqL")
                .clientKey("gQlFVwBSLB47kHH9vGrKfenYsFdx5c9LTklDgYcr")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
