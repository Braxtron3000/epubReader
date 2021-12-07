package com.ebookfrenzy.cryptoepub;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    String baseUrl;
    Book book;
    EpubReader epubReader;
    int resourceNum = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /*findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //secondTry();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/

        //initializaton stuff
        webView = findViewById(R.id.webview);
        epubReader = new EpubReader();
        baseUrl = getResources().openRawResource(R.raw.bible).toString();
        book = null;
        try {
            book = book = epubReader.readEpub(getResources().openRawResource(R.raw.bible));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView.loadDataWithBaseURL(baseUrl, getResource(resourceNum,book),"text/html", "utf-8",null);
    }

    //had to make this because android is retarded and doesn't listen to its xml.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.epubnavigation,menu);
        return super.onCreateOptionsMenu(menu);
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId()==R.id.toTableOfContents){
            Log.d(TAG, "onOptionsItemSelected: to table of contents!");
            esketit(getResource(1,book));
            //Toast.makeText(this, "to table of conts!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (item.getItemId()==R.id.nextChapter){
            Log.d(TAG, "onOptionsItemSelected: to next chapter!");
            if (resourceNum!= book.getSpine().getSpineReferences().size()){
                resourceNum+=1;
                esketit(getResource(resourceNum,book));
            }
            else Toast.makeText(this, "end of book", Toast.LENGTH_SHORT).show();

            //Toast.makeText(this, "to next!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (item.getItemId()==R.id.previousChapter){
            Log.d(TAG, "onOptionsItemSelected: to previous chapter!");
            if (resourceNum-1!=0){
                resourceNum-=1;
                esketit(getResource(resourceNum,book));
            }
            else Toast.makeText(this, "this is the beginning!", Toast.LENGTH_SHORT).show();

            //Toast.makeText(this, "to prev!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Log.d(TAG, "onOptionsItemSelected: else statement issue");
            Toast.makeText(this, "ha ha ha u suck!", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }



    private String getResource(int i, Book book){
        String linez = "";
        List<SpineReference> spineReferenceList = book.getSpine().getSpineReferences();
        StringBuilder sb = new StringBuilder();
        Resource resource = book.getSpine().getResource(i);

        try {
            InputStream is = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    Log.d(TAG, "getChapters: "+line);
                    linez = sb.append(line).append("\n").toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linez;
    }


    //shortened load webviewer
    private void esketit(String presenter){
        webView.loadDataWithBaseURL(baseUrl, presenter,"text/html", "utf-8",null);
    }

    //get contents of book chapter wise
    private String readWholeThing(Book book) {
        String linez = "";
        List<SpineReference> spineList = book.getSpine().getSpineReferences();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; spineList.size() > i; i++) {
            Resource resource = book.getSpine().getResource(i);

            try {
                InputStream is = resource.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                try {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        Log.d(TAG, "getChapters: "+line);
                        linez = sb.append(line).append("\n").toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return linez;
    }























    /*
     * Recursively Log the Table of Contents
     *
     * @param tocReferences
     * @param depth
     */

        /*private void logTableOfContents(List<TOCReference> tocReferences,int depth){
            if (tocReferences==null){
                return;
            }
            for (TOCReference tocReference : tocReferences){
                StringBuilder tocString = new StringBuilder();
                for (int i = 0; i < depth; i++) {
                    tocString.append("\t");
                }
                tocString.append(tocReference.getTitle());
                Log.i("epublib",tocString.toString());

                logTableOfContents(tocReference.getChildren(), depth+1);
            }
        }*/

        /*private void secondTry() throws URISyntaxException, IOException {

            //according to http://www.siegmann.nl/epublib. Simple things are simple

            //read epub
            EpubReader epubReader = new EpubReader();
            //URI bibleuri = new URI(getBiblesPath());
            Book book = epubReader.readEpub(getResources().openRawResource(R.raw.bible));//new FileInputStream(new File(bibleuri)));

            //print the first title
            List<String> titles = book.getMetadata().getTitles();
            System.out.println("book title: "+ (titles.isEmpty() ? "book has no title": titles.get(0)));


        }*/

    //first print
    /*private void firstPrint(WebView webView) {
        //copying from the secondTry method that was working.
        EpubReader epubReader = new EpubReader();
        Book book = null;
        try {
            book = epubReader.readEpub(getResources().openRawResource(R.raw.bible));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //if book isn't null go ahead and add it to the webview content.
        if (book != null) {
            try {
                String data = Arrays.toString(book.getContents().get(2).getData());
                String url = getResources().openRawResource(R.raw.bible).toString();
                webView.loadDataWithBaseURL(url, data, "text/html", "UTF-8", null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else Log.d(TAG, "sorry the book was null");
    }*/


    //according to https://stackoverflow.com/questions/12965720/displaying-images-using-epublib
/*    private void getSpineAndStuff(Book book, WebView webView) {
        try {
            //go through each of the indexes of book. (all resources)
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < book.getContents().size(); i++) {
                //inputstream = the spines inputstream at the spinereferences index:i
                InputStream is = book.getSpine().getSpineReferences().get(i).getResource().getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    Log.d(TAG, "display line | " + line);
                }
                is.close();
            }
            webView.loadDataWithBaseURL(getResources().openRawResource(R.raw.bible).toString(), sb.toString(), "text/html", "utf-8", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/





}