package gofereats.dependencies.module;
/**
 * @package com.trioangle.gofereats
 * @subpackage dependencies.module
 * @category AppContainerModule
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gofereats.configs.Constants;
import gofereats.configs.RunTimePermission;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.utils.CalendarDatePickerDialog;
import gofereats.utils.CalendarTimePickerDialog;
import gofereats.utils.CommonMethods;
import gofereats.utils.DateTimeUtility;
import gofereats.utils.ImageUtils;
import gofereats.utils.Validator;
import gofereats.views.customize.CustomDialog;

/*****************************************************************
 App Container Module
 ****************************************************************/
@Module(includes = gofereats.dependencies.module.ApplicationModule.class)
public class AppContainerModule {
    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public CommonMethods providesCommonMethods() {
        return new CommonMethods();
    }

    @Provides
    @Singleton
    public SessionManager providesSessionManager() {
        return new SessionManager();
    }


    @Provides
    @Singleton
    public Validator providesValidator() {
        return new Validator();
    }

    @Provides
    @Singleton
    public RunTimePermission providesRunTimePermission() {
        return new RunTimePermission();
    }

    @Provides
    @Singleton
    public Context providesContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public ArrayList<String> providesStringArrayList() {
        return new ArrayList<>();
    }

    @Provides
    @Singleton
    public JsonResponse providesJsonResponse() {
        return new JsonResponse();
    }

    @Provides
    @Singleton
    public DateTimeUtility providesDateTimeUtility() {
        return new DateTimeUtility();
    }

    @Provides
    @Singleton
    public ImageUtils providesImageUtils() {
        return new ImageUtils();
    }

    @Provides
    @Singleton
    public CalendarDatePickerDialog providesCalendarDatePickerDialog() {
        return new CalendarDatePickerDialog();
    }

    @Provides
    @Singleton
    public CalendarTimePickerDialog providesCalendarTimePickerDialog() {
        return new CalendarTimePickerDialog();
    }

    @Provides
    @Singleton
    public CustomDialog providesCustomDialog() {
        return new CustomDialog();
    }
}
