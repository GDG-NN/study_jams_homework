package com.harman.ruingriv.sherlockedyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchResults extends AppCompatActivity {
    private ArrayList<SearchItem> mSearchItems;
    WebView mWebView;
    private static final String SEARCH_URL = "https://www.google.com/search?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_activity);
        Intent intent = getIntent();
        mSearchItems = intent.getParcelableArrayListExtra(SearchActivity.SEARCH_DATA);
        Collections.sort(mSearchItems, new Comparator<SearchItem>() {
            public int compare(SearchItem s1, SearchItem s2) {
                return s1.getSpinnerItem().compareToIgnoreCase(s2.getSpinnerItem());
            }
        });
        String searchQuery = composeSearchQuery(mSearchItems);
        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        final Activity activity = this;
        String url = SEARCH_URL + searchQuery;
        mWebView.setWebViewClient(new WebViewClient(){
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        mWebView.loadUrl(url);
//        new JsonSearchTask().execute();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String composeSearchQuery(ArrayList<SearchItem> items){
//        (site:vk.com|site:ru.linkedin.com) inurl:(Ivanov Grigory)
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SampleRecentSuggestionsProvider.AUTHORITY,
                        SampleRecentSuggestionsProvider.MODE);
        StringBuilder sb = new StringBuilder();
        boolean multipleSites = false;
        for (int i = 0; i < mSearchItems.size(); i++) {
            if(!TextUtils.equals(mSearchItems.get(i).getSpinnerItem(), "Site")) {
                if (i+1 < mSearchItems.size()) {
                    if (!TextUtils.equals(mSearchItems.get(i+1).getSpinnerItem(), "Site")) {
                        if (i==0){
                            sb.append("(");
                        }
                        sb.append("inurl:(");
                        sb.append(mSearchItems.get(i).getConditionsList());
                        sb.append(")|");
                    }else {
                        sb.append("inurl:(");
                        sb.append(mSearchItems.get(i).getConditionsList());
                        sb.append(")) ");
                    }
                }
                suggestions.saveRecentQuery(mSearchItems.get(i).getConditionsList(), null);
            }else if (i+1 < mSearchItems.size()){
                if (TextUtils.equals(mSearchItems.get(i+1).getSpinnerItem(), "Site")) {
                    sb.append("(site:");
                    sb.append(mSearchItems.get(i).getConditionsList());
                    sb.append("|");
                    multipleSites = true;
                }
            }else {
                sb.append("site:");
                sb.append(mSearchItems.get(i).getConditionsList());
                if (multipleSites){
                    sb.append(")");
                    multipleSites = false;
                }
            }
        }
        return sb.toString();
    }
}
