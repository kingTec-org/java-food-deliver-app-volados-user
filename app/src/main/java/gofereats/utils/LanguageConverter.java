package gofereats.utils;

import android.app.AlertDialog;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;

public class LanguageConverter {
    @Inject
    SessionManager sessionManager;


    private Context context;
    private boolean isCancelable;
    private RecyclerView languageView;
    private LanguageAdapter languageAdapter;
    private String[] languageList;
    private String[] languageCode;
    private static AlertDialog alertDialoglanguage;

    public onSuccessLanguageChangelistner onLanguageChange;

    public interface onSuccessLanguageChangelistner {
        void onSuccess(String language, String languageCode);
    }

    public LanguageConverter(Context context, boolean isCancelable, String[] languageList, String[] languageCode,onSuccessLanguageChangelistner listener) {
        AppController.getAppComponent().inject(this);
        this.context = context;
        this.isCancelable = isCancelable;
        this.languageList = languageList;
        this.languageCode = languageCode;
        this.onLanguageChange=listener;
        showAlert();
    }

    private void showAlert() {
        languageView = new RecyclerView(context);
        languageAdapter = new LanguageAdapter(context, languageList, languageCode, sessionManager.getAppLanguage());
        languageView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        languageView.setAdapter(languageAdapter);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.langugae_header, null);
        TextView T = (TextView) view.findViewById(R.id.header);
        T.setText(context.getResources().getString(R.string.select_lang));

        alertDialoglanguage = new android.app.AlertDialog.Builder(context).setCustomTitle(view).setView(languageView).setCancelable(isCancelable).show();
        languageAdapter.setOnItemClickListner(new LanguageAdapter.OnLanguageClick() {
            @Override
            public void onLanguageClick(String languageCode, String languageName) {
                sessionManager.setAppLanguage(languageName);
                sessionManager.setAppLanguageCode(languageCode);
                alertDialoglanguage.dismiss();
                onLanguageChange.onSuccess(languageName,languageCode);

            }
        });
    }
}
