package com.sc703.taller.ui.web;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sc703.taller.R;

public class WebFragment extends Fragment {

    WebView webView;
    String url = "https://www.mep.go.cr/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_web, container, false);

        webView = root.findViewById(R.id.webview);
        webView.setWebViewClient(new clienteWeb());
        WebSettings configuracion = webView.getSettings();
        configuracion.setJavaScriptEnabled(true);
        webView.loadUrl(url);

        return root;
    }
    private class clienteWeb extends WebViewClient{
        public boolean urlCargado (WebView webView, String url){
            webView.loadUrl(url);
            return true;
        }
    }
}